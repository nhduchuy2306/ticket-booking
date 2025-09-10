# Authentication Implementation Review & Improvement Recommendations

## Current Implementation Summary

Your system uses **JWT-based authentication with permission delegation via UserGroup matrix**. Here's the flow:

1. **Auth Service** generates JWT tokens containing user ID, organization ID, and a permissions map
2. **Each microservice** validates JWT signature using shared secret (HS512) and extracts claims
3. **Permission checks** via `@PreAuthorize` annotations evaluate actions against embedded permissions
4. **Keycloak** is configured in docker-compose but **not actively integrated** (cosmetic)

---

## ✅ Strengths

1. **Stateless Design**: JWT allows horizontal scaling without session replication
2. **Fine-Grained Permissions**: Permission matrix per app/action enables resource-level control
3. **Per-Service Security**: Each service validates independently (no single gateway bottleneck)
4. **Password Hashing**: BCrypt(10) for storage; proper password verification in login
5. **Spring Security Integration**: @PreAuthorize, CustomPermissionEvaluator leverage Spring's built-in validation

---

## ❌ Critical Issues

### 1. **Symmetric JWT Signing (HS512) with Shared Secret**
**Problem**: All services share the same secret key for JWT signing/verification.
- If any service is compromised, attacker can forge tokens
- No way to revoke/rotate without all services restarting simultaneously
- Single point of failure for entire auth chain

**Current Code**:
```java
// JwtTokenProvider.java (Auth Service)
signedJWT.sign(new MACSigner(jwtSecretKey.getBytes()));

// JwtDecoderEncoderConfiguration (All Services)
SecretKeySpec secretKeySpec = new SecretKeySpec(jwtSecretKey.getBytes(), JWSAlgorithm.HS512.getName());
```

**Recommendation**: Use **asymmetric signing (RS256 / ECDSA)**
- Auth Service signs with **private key**
- All services verify with **public key** (no secrets shared, can be public)
- Allows key rotation: issue new tokens with new key without invalidating old tokens

---

### 2. **No Token Expiration Handling on Refresh**
**Problem**: `refreshToken()` generates new token with same 1-day expiry, no refresh token rotation.

```java
// AuthServiceImpl.java line ~105
TokenResponse tokenResponse = authService.refreshToken(userId);
// Issues new token, but no old token invalidation
```

- Old tokens remain valid until expiry → silent token reuse window
- No refresh token concept → cannot short-live access tokens

**Recommendation**: Implement **token pair pattern**
- **Access Token**: 15-30 min expiry (short-lived)
- **Refresh Token**: 7-30 days, stored in HTTP-only cookie or secure storage
- Refresh endpoint validates refresh token, issues new access token
- Optionally blacklist old tokens upon refresh

---

### 3. **No Logout / Token Revocation Mechanism**
**Problem**: Once issued, JWT tokens cannot be revoked. Users cannot "logout" effectively.

- User deletes API key → JWT still valid for 24 hours
- User changes password → old tokens still work
- Compromised account → wait 24 hours for natural expiry

**Recommendation**: Implement **token blacklist** (short-term solution) or **JTI claim + database lookup** (robust)

**Option A: Redis Blacklist** (simple for this architecture)
```java
// On logout:
redisTemplate.opsForValue().set("blacklist:jwt:" + jti, true, Duration.ofDays(1));

// On validation (in CustomPermissionEvaluator):
if (redisTemplate.hasKey("blacklist:jwt:" + jti)) {
    return false; // Token is revoked
}
```

**Option B: Keycloak Integration** (recommended for production)
- Keycloak already in docker-compose but unused
- Delegate auth to Keycloak, validate tokens against Keycloak's public key endpoint
- Gain token revocation, multi-factor auth, session management, user federation

---

### 4. **Hardcoded JWT Secret in Environment Variable**
**Problem**: Secret passed as plain environment variable through docker-compose/CI/CD logs.

```properties
jwt.secret.token=${JWT_SECRET_TOKEN}
```

- Git history, logs, environment printouts expose secret
- No secret rotation mechanism

**Recommendation**: Use **secure secret management**
- **Development**: HashiCorp Vault, AWS Secrets Manager, or Azure Key Vault
- **Docker**: Secret mount (Docker Swarm) or Kubernetes Secret
- Rotate secret monthly; issue new keys without redeploying

---

### 5. **Permission Claim Serialization Fragility**
**Problem**: Complex nested JSON `permissions: {APP_ID: [actions]}` serialized/deserialized manually.

```java
// JwtTokenProvider.java line ~133
claimsBuilder.claim("permissions", appIdAndActions);
// appIdAndActions is Map<String, List<String>>

// CustomPermissionEvaluator.java line ~26
Map<String, List<String>> items = (Map<String, List<String>>)jwt.get("permissions");
// Unchecked cast; fails silently if structure changes
```

**Risks**:
- Jackson serialization differences between services
- Type casting fails without error; returns `{}` permissions (denies all)
- Hard to debug why user suddenly loses access

**Recommendation**: Use **standardized claims**
```json
{
  "sub": "user-id",
  "organizationId": "org-id",
  "scope": "event:read event:create ticket:read",  // OAuth 2.0 standard
  "roles": ["ORGANIZER", "CUSTOMER"],              // Simple roles
  "iss": "https://auth.example.com"
}
```

Or embed permissions as flat array:
```json
{
  "permissions": [
    "event:read",
    "event:create",
    "event:update",
    "event:delete",
    "ticket:read"
  ]
}
```

---

### 6. **Null Returns Instead of Exceptions**
**Problem**: Auth operations return `null` on failure instead of throwing exceptions.

```java
// AuthServiceImpl.java line ~30
if(userAccountEntity != null) {
    if(passwordEncoder.matches(...)) {
        // success
    }
}
return null;  // Silent failure; caller must check null

// AuthController.java line ~28
LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
if(loginResponseDto == null) {
    return ResponseEntity.badRequest().body("Invalid username or password");
}
```

**Issues**:
- Attacker learns whether username exists (timing side-channel)
- Null checks scattered across controllers → inconsistent error handling
- No audit logging of failed attempts

**Recommendation**: Throw checked exceptions
```java
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}

// AuthServiceImpl.java
UserAccountEntity userAccount = userAccountRepository.findByUsername(username)
    .orElseThrow(() -> new AuthenticationFailedException("Invalid credentials"));

if (!passwordEncoder.matches(password, userAccount.getPassword())) {
    throw new AuthenticationFailedException("Invalid credentials");
}
```

---

### 7. **No Rate Limiting on Auth Endpoints**
**Problem**: Login/register endpoints are wide open to brute force attacks.

```java
// AuthController.java
@PostMapping(LOGIN_PATH)
public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
    // No rate limit; any IP can attempt unlimited logins
}
```

**Recommendation**: Add **Spring Cloud Gateway rate limiting or Spring Security**
```java
// Add to api-gateway application.yml
spring.cloud.gateway.routes:
  - id: auth-service
    predicates:
      - Path=/auths/**
    filters:
      - name: RequestRateLimiter
        args:
          redis-rate-limiter.replenishRate: 5
          redis-rate-limiter.burstCapacity: 10
          key-resolver: "#{@ipAddressKeyResolver}"
```

Or use Spring Security custom filter:
```java
@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {
    private final RateLimitService rateLimitService;
    
    protected void doFilterInternal(HttpServletRequest request, 
            HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        if (request.getRequestURI().contains("/login")) {
            String clientIp = getClientIp(request);
            if (!rateLimitService.allowRequest(clientIp, "login")) {
                response.sendError(429, "Too many login attempts");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

---

### 8. **Missing Audit Logging**
**Problem**: No tracking of who accessed what when, or failed auth attempts.

```java
// No logging of:
// - Successful logins (user ID, timestamp, IP)
// - Failed attempts (username, timestamp, IP)
// - Permission violations (what resource, what action)
// - Token generation/refresh
```

**Recommendation**: Add centralized audit logging
```java
@Component
public class AuditService {
    public void logLogin(String userId, String username, String ip, boolean success) {
        // Log to Kafka topic: audit.login
        // Or write to audit database table
    }
    
    public void logAccessDenied(String userId, String resource, String action) {
        // Trigger alert if repeated denials
    }
}

// In AuthServiceImpl
if (login successful) {
    auditService.logLogin(userId, username, getClientIp(request), true);
} else {
    auditService.logLogin(null, username, getClientIp(request), false);
}

// In CustomPermissionEvaluator
if (!hasPermission) {
    auditService.logAccessDenied(userId, resource, action);
}
```

---

### 9. **CORS Configuration at Multiple Levels**
**Problem**: Both API Gateway and individual services define CORS rules independently.

```java
// api-gateway/application.yml
spring.cloud.gateway.global-cors.cors-configurations...

// common-service/CorsConfiguration.java
@Configuration
public class CorsConfiguration { ... }
```

- Conflicting rules; harder to debug which applies
- Accidental open CORS (allow all origins `"*"`)

**Recommendation**: Centralize at **API Gateway only**; remove from services
```yaml
# api-gateway/application-prod.yml
spring.cloud.gateway.global-cors:
  cors-configurations:
    '[/**]':
      allowed-origin-patterns: "https://yourdomain.com"  # Explicit, not *
      allowed-methods: [GET, POST, PUT, DELETE]
      allowed-headers: [Content-Type, Authorization]
      allow-credentials: true
      max-age: 3600
```

---

### 10. **No HTTPS/TLS Configuration**
**Problem**: Tokens transmitted over HTTP in dev; docker-compose has no TLS termination.

- JWT headers, org ID, user ID visible in plaintext
- Vulnerable to man-in-the-middle attacks

**Recommendation**: Enforce HTTPS in production
```yaml
# application-prod.yml
server.servlet.session.cookie.secure: true
server.servlet.session.cookie.http-only: true
server.ssl.enabled: true
server.ssl.key-store: /run/secrets/keystore.p12
server.ssl.key-store-password: ${KEYSTORE_PASSWORD}
```

Or use **reverse proxy** (Nginx, HAProxy) with SSL termination.

---

## Proposed Improved Authentication Flow

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ POST /auth/login {username, password}
       │
       ▼
┌─────────────────────────┐
│   API Gateway           │
│   - Rate limit          │
│   - Log attempt         │
└──────┬──────────────────┘
       │
       ▼
┌──────────────────────────────────────┐
│   Auth Service                       │
│   ✓ Verify credentials               │
│   ✓ Check if account active          │
│   ✓ Generate RS256 signed tokens:    │
│     - ACCESS_TOKEN (15 min)          │
│     - REFRESH_TOKEN (7 days)         │
│   ✓ Log successful login             │
│   ✓ Return tokens                    │
└──────┬───────────────────────────────┘
       │
       │ Response:
       │ {
       │   "access_token": "jwt...",
       │   "refresh_token": "jwt...",
       │   "expires_in": 900
       │ }
       │
       ▼
┌─────────────────────────────────────┐
│   Client Storage (Secure)            │
│   - AccessToken → Memory             │
│   - RefreshToken → HTTPOnly cookie   │
└─────────────────────────────────────┘
       │
       │ Subsequent Request:
       │ Authorization: Bearer <access_token>
       │
       ▼
┌──────────────────────────────────────┐
│   API Gateway                        │
│   - Extract Bearer token             │
│   - Pass to service                  │
└──────┬───────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────┐
│   Microservice                       │
│   - Verify signature (public key)    │
│   - Check expiry                     │
│   - Check blacklist (Redis)          │
│   - @PreAuthorize evaluate perms     │
│   - Audit log access                 │
└──────────────────────────────────────┘

Token Refresh Flow:
POST /auth/refresh
  Cookie: refresh_token=...
  ↓
Auth Service verifies refresh token
  ↓
Generates new access token + refresh token
  ↓
Updates blacklist (revokes old refresh token)
```

---

## Implementation Roadmap (Priority Order)

### Phase 1: Immediate (Security-Critical)
1. ✅ Implement RS256 (asymmetric signing) instead of HS512
2. ✅ Add token blacklist for logout/revocation (Redis)
3. ✅ Add rate limiting on login endpoint
4. ✅ Replace null returns with exceptions + audit logging
5. ✅ Move secret to secure vault (Vault, AWS Secrets)

### Phase 2: Short-Term (1-2 Weeks)
6. ✅ Implement access token (15 min) + refresh token (7 days) pair
7. ✅ Add audit logging for auth events
8. ✅ Implement HTTPOnly cookie for refresh token
9. ✅ Standardize permission claims (scope or flat array)
10. ✅ Remove CORS config from individual services

### Phase 3: Medium-Term (1 Month)
11. ✅ Integrate Keycloak (SSO, MFA, account linking)
12. ✅ Add HTTPS/TLS termination in production
13. ✅ Implement multi-factor authentication (TOTP, email)
14. ✅ Add brute-force detection (IP blocking, CAPTCHA)
15. ✅ Centralized audit log with Elasticsearch/ELK

### Phase 4: Long-Term (Ongoing)
16. ✅ OAuth 2.0 / OpenID Connect for third-party integrations
17. ✅ API key authentication for service-to-service calls
18. ✅ Security audit + penetration testing
19. ✅ Compliance (GDPR, CCPA, PCI-DSS if handling payments)

---

## Quick Start: First Fix (RS256 Implementation)

**Step 1**: Generate RSA key pair
```bash
openssl genrsa -out private.pem 2048
openssl rsa -in private.pem -pubout -out public.pem
```

**Step 2**: Update `JwtTokenProvider.java` (Auth Service)
```java
import com.nimbusds.jose.crypto.RSASSASigner;

public String generateTokenWithPermissions(UserAccountResponseDto dto) {
    try {
        RSAKey rsaKey = (RSAKey) JWK.parse(readPrivateKeyFile());
        JWSSigner signer = new RSASSASigner(rsaKey.toPrivateKey());
        
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
            .subject(dto.getId())
            .issuer("auth-service")
            .expirationTime(...)
            .claim("permissions", getAppIdAndActions(dto))
            .claim("organizationId", dto.getOrganizationId())
            .build();
        
        SignedJWT signedJWT = new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.RS256).build(), 
            claims
        );
        signedJWT.sign(signer);
        return signedJWT.serialize();
    } catch (...) { ... }
}
```

**Step 3**: Update `JwtDecoderEncoderConfiguration.java` (All Services)
```java
@Bean
public JwtDecoder jwtDecoder(@Value("${jwt.public-key}") String publicKey) {
    RSAKey rsaKey = (RSAKey) JWK.parse(publicKey);
    return NimbusJwtDecoder
        .withPublicKey(rsaKey.toRSAPublicKey())
        .build();
}
```

**Step 4**: Store keys safely
```properties
# Secure storage (not in code)
jwt.public-key=${JWT_PUBLIC_KEY}  # Can be in config; it's public
jwt.private-key=${JWT_PRIVATE_KEY}  # Must be in vault
```

---

## Summary

**Current state**: Functional but vulnerable (symmetric key, no revocation, no refresh token rotation, missing rate limiting/audit).

**Risk level**: 🔴 **HIGH** for production use.

**Recommended approach**: 
1. Implement RS256 + token pair (Access/Refresh) immediately
2. Integrate Keycloak (leverage already-deployed docker container)
3. Add audit logging + rate limiting
4. Remove architectural redundancy (CORS in single layer)

This will bring you to **industry-standard OAuth 2.0 / OpenID Connect compliance** with minimal additional complexity.

