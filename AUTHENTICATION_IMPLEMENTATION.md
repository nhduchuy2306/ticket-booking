# Authentication Implementation Examples

## Complete RS256 Implementation (Copy & Paste Ready)

### Step 1: Generate Keys

```bash
# Generate 2048-bit RSA private key
openssl genrsa -out private_key.pem 2048

# Extract public key from private key
openssl rsa -in private_key.pem -pubout -out public_key.pem

# Convert to PKCS8 format (Spring prefers this)
openssl pkcs8 -topk8 -inform PEM -outform PEM -in private_key.pem -out private_key_pkcs8.pem -nocrypt

# Output examples:
# private_key_pkcs8.pem: -----BEGIN PRIVATE KEY-----
# public_key.pem:        -----BEGIN PUBLIC KEY-----
```

---

## 1. Updated JwtTokenProvider.java (Auth Service)

```java
package com.gyp.authservice.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.permission.ApplicationPermission;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtTokenProvider {

    @Value("${jwt.private-key}")
    private String privateKeyPem;

    public static final String AUTH_CODE = "auth_code";
    private static final String JTI_CLAIM = "jti";  // JWT ID for revocation

    /**
     * Generate JWT token pair: access token (15 min) + refresh token (7 days)
     */
    public TokenPair generateTokenPair(UserAccountResponseDto dto) {
        try {
            String accessToken = generateAccessToken(dto);
            String refreshToken = generateRefreshToken(dto);
            return TokenPair.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(900)  // 15 minutes in seconds
                    .tokenType("Bearer")
                    .build();
        } catch (Exception e) {
            log.error("Error generating token pair", e);
            throw new RuntimeException("Token generation failed", e);
        }
    }

    /**
     * Short-lived access token (15 minutes)
     * Used for API requests
     */
    private String generateAccessToken(UserAccountResponseDto dto) throws JOSEException {
        Map<String, List<String>> permissions = getAppIdAndActions(dto);
        String jti = UUID.randomUUID().toString();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(dto.getId())
                .issuer("auth-service")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli()))
                .jwtID(jti)  // For revocation tracking
                .claim("organizationId", dto.getOrganizationId())
                .claim("username", dto.getUsername())
                .claim("permissions", permissions)
                .claim("scope", buildScopeString(permissions))
                .build();

        return signJwt(claims);
    }

    /**
     * Long-lived refresh token (7 days)
     * Used only to request new access tokens
     */
    private String generateRefreshToken(UserAccountResponseDto dto) throws JOSEException {
        String jti = UUID.randomUUID().toString();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(dto.getId())
                .issuer("auth-service")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()))
                .jwtID(jti)
                .claim("organizationId", dto.getOrganizationId())
                .claim("tokenType", "refresh")  // Identify as refresh token
                .build();

        return signJwt(claims);
    }

    /**
     * Sign JWT with RS256 (asymmetric)
     */
    private String signJwt(JWTClaimsSet claims) throws JOSEException {
        try {
            PrivateKey privateKey = loadPrivateKey();
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .type(JOSEObjectType.JWT)
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claims);
            signedJWT.sign(new RSASSASigner(privateKey));

            return signedJWT.serialize();
        } catch (Exception e) {
            log.error("JWT signing failed", e);
            throw new JOSEException("JWT signing failed", e);
        }
    }

    /**
     * Load private key from PEM format
     */
    private PrivateKey loadPrivateKey() throws Exception {
        String keyContent = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decodedKey = Base64.getDecoder().decode(keyContent);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * Extract JWT claims (for validation)
     */
    public Object getClaim(String token, String key) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        if (claimsSet == null) {
            throw new IllegalArgumentException("JWT claims set is null");
        }
        return claimsSet.getClaim(key);
    }

    /**
     * Get JTI (for blacklist lookups)
     */
    public String getJti(String token) throws JOSEException, ParseException {
        return (String) getClaim(token, JTI_CLAIM);
    }

    /**
     * Build permission map from user groups
     */
    private Map<String, List<String>> getAppIdAndActions(UserAccountResponseDto dto) {
        Map<String, List<String>> appIdAndActions = new HashMap<>();
        var userGroupList = dto.getUserGroupList();

        // Check if admin
        if (userGroupList.size() >= 1) {
            var firstGroup = userGroupList.getFirst();
            if (BooleanUtils.isTrue(firstGroup.getAdministrator())) {
                appIdAndActions.put(ApplicationPermission.ADMIN.getApplicationId(), List.of());
                return appIdAndActions;  // Admin has all permissions
            }
        }

        // Collect permissions from all groups
        userGroupList.forEach(userGroup -> {
            var permissionItems = userGroup.getUserGroupPermissions().getPermissionItems();
            permissionItems.forEach(item -> {
                List<String> actions = item.getActions()
                        .stream()
                        .map(Enum::name)
                        .toList();
                appIdAndActions.put(item.getApplicationId(), actions);
            });
        });

        return appIdAndActions;
    }

    /**
     * Build OAuth 2.0 scope string
     * Example: "event:read event:create ticket:read"
     */
    private String buildScopeString(Map<String, List<String>> permissions) {
        return permissions.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(action -> entry.getKey().toLowerCase() + ":" + action.toLowerCase()))
                .reduce((a, b) -> a + " " + b)
                .orElse("");
    }
}

// DTO for token pair response
@Getter
@Setter
@Builder
public class TokenPair {
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private String tokenType;
}
```

---

## 2. Updated AuthServiceImpl.java (Error Handling)

```java
package com.gyp.authservice.services.impl;

import com.gyp.authservice.dtos.auth.*;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.exceptions.AuthenticationFailedException;
import com.gyp.authservice.mappers.UserAccountMapper;
import com.gyp.authservice.repositories.UserAccountRepository;
import com.gyp.authservice.services.AuthService;
import com.gyp.authservice.services.JwtTokenProvider;
import com.gyp.authservice.services.TokenPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAccountMapper userAccountMapper;
    private final AuditService auditService;  // New

    /**
     * Login with username/password
     * Returns access token + refresh token
     */
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto, String clientIp) {
        try {
            UserAccountEntity user = userAccountRepository.findByUsername(loginRequestDto.getUsername())
                    .orElseThrow(() -> {
                        // Log failed attempt (generic message to avoid user enumeration)
                        auditService.logLoginAttempt(
                                loginRequestDto.getUsername(), 
                                clientIp, 
                                false, 
                                "User not found"
                        );
                        return new AuthenticationFailedException("Invalid credentials");
                    });

            // Verify password
            if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
                auditService.logLoginAttempt(
                        user.getId(), 
                        clientIp, 
                        false, 
                        "Invalid password"
                );
                throw new AuthenticationFailedException("Invalid credentials");
            }

            // Check if account is active
            if (!user.isActive()) {
                auditService.logLoginAttempt(
                        user.getId(), 
                        clientIp, 
                        false, 
                        "Account disabled"
                );
                throw new AuthenticationFailedException("Account is disabled");
            }

            // Generate tokens
            UserAccountResponseDto dto = userAccountMapper.toResponseDto(user);
            TokenPair tokenPair = jwtTokenProvider.generateTokenPair(dto);

            // Log successful login
            auditService.logLoginAttempt(
                    user.getId(), 
                    clientIp, 
                    true, 
                    "Login successful"
            );

            return LoginResponseDto.builder()
                    .accessToken(tokenPair.getAccessToken())
                    .refreshToken(tokenPair.getRefreshToken())
                    .expiresIn(tokenPair.getExpiresIn())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .organizationId(user.getOrganizationId())
                    .build();

        } catch (AuthenticationFailedException e) {
            throw e;
        } catch (Exception e) {
            log.error("Login error", e);
            throw new RuntimeException("Login failed", e);
        }
    }

    /**
     * Register new user
     */
    @Override
    public UserAccountResponseDto register(RegisterRequestDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (userAccountRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        UserAccountEntity user = userAccountMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActive(true);

        UserAccountEntity saved = userAccountRepository.save(user);

        auditService.logUserRegistration(saved.getId(), saved.getUsername());

        return userAccountMapper.toResponseDto(saved);
    }

    /**
     * Refresh tokens
     * Validates refresh token and issues new access token + refresh token
     */
    @Override
    public TokenPair refreshToken(String refreshToken) {
        try {
            // Verify refresh token signature and expiry
            SignedJWT jwt = SignedJWT.parse(refreshToken);
            // (verification happens in JwtDecoder; this is post-verification)

            String userId = jwt.getJWTClaimsSet().getSubject();
            String jti = jwtTokenProvider.getJti(refreshToken);

            // Check if refresh token is blacklisted
            if (auditService.isTokenBlacklisted(jti)) {
                throw new AuthenticationFailedException("Refresh token has been revoked");
            }

            // Load user and generate new tokens
            UserAccountEntity user = userAccountRepository.findById(userId)
                    .orElseThrow(() -> new AuthenticationFailedException("User not found"));

            UserAccountResponseDto dto = userAccountMapper.toResponseDto(user);
            TokenPair newTokenPair = jwtTokenProvider.generateTokenPair(dto);

            // Blacklist old refresh token
            auditService.blacklistToken(jti);

            return newTokenPair;

        } catch (Exception e) {
            log.error("Token refresh error", e);
            throw new AuthenticationFailedException("Token refresh failed");
        }
    }

    /**
     * Logout: blacklist all tokens
     */
    @Override
    public void logout(String accessToken) {
        try {
            String jti = jwtTokenProvider.getJti(accessToken);
            auditService.blacklistToken(jti);
        } catch (Exception e) {
            log.warn("Error blacklisting token on logout", e);
        }
    }
}
```

---

## 3. Updated JwtDecoderEncoderConfiguration.java (All Services)

```java
package com.gyp.common.configurations;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtDecoderEncoderConfiguration {

    /**
     * Decode and verify JWT signed with RS256
     * Uses public key only (can be published/shareable)
     */
    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.public-key}") String publicKeyPem) {
        try {
            PublicKey publicKey = loadPublicKey(publicKeyPem);
            return NimbusJwtDecoder
                    .withPublicKey((java.security.interfaces.RSAPublicKey) publicKey)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize JWT decoder", e);
        }
    }

    /**
     * Load RSA public key from PEM format
     */
    private PublicKey loadPublicKey(String publicKeyPem) throws Exception {
        String keyContent = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decodedKey = Base64.getDecoder().decode(keyContent);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
```

---

## 4. Token Blacklist Service

```java
package com.gyp.authservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String BLACKLIST_PREFIX = "token:blacklist:";

    /**
     * Blacklist a token (for logout/revocation)
     * TTL = original token expiry time (waste no Redis memory)
     */
    public void blacklistToken(String jti, long expiryTimestamp) {
        try {
            long remainingSeconds = (expiryTimestamp - System.currentTimeMillis()) / 1000;
            if (remainingSeconds > 0) {
                redisTemplate.opsForValue().set(
                        BLACKLIST_PREFIX + jti,
                        "revoked",
                        Duration.ofSeconds(remainingSeconds)
                );
                log.info("Token {} blacklisted until {}", jti, expiryTimestamp);
            }
        } catch (Exception e) {
            log.error("Error blacklisting token", e);
        }
    }

    /**
     * Check if token is blacklisted
     */
    public boolean isBlacklisted(String jti) {
        try {
            return Boolean.TRUE.equals(
                    redisTemplate.hasKey(BLACKLIST_PREFIX + jti)
            );
        } catch (Exception e) {
            log.error("Error checking token blacklist", e);
            return false;
        }
    }
}
```

---

## 5. Updated CustomPermissionEvaluator.java (with Blacklist Check)

```java
package com.gyp.common.configurations;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.gyp.common.enums.permission.ActionPermission;
import com.gyp.common.enums.permission.ApplicationPermission;
import com.gyp.authservice.services.TokenBlacklistService;  // New
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component("permissionEvaluator")
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final TokenBlacklistService tokenBlacklistService;  // New

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || permission == null) {
            return false;
        }

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            var jwt = jwtAuthenticationToken.getTokenAttributes();

            // Check if token is blacklisted (NEW)
            String jti = (String) jwt.get("jti");
            if (jti != null && tokenBlacklistService.isBlacklisted(jti)) {
                return false;
            }

            // Check if access token (not refresh token) (NEW)
            String tokenType = (String) jwt.get("tokenType");
            if ("refresh".equals(tokenType)) {
                return false;  // Refresh tokens can't be used for API access
            }

            @SuppressWarnings("unchecked")
            Map<String, List<String>> items = (Map<String, List<String>>) jwt.get("permissions");

            if (items == null || items.isEmpty()) {
                return false;
            }

            // Check for ADMIN permission
            if (items.containsKey(ApplicationPermission.ADMIN.getApplicationId())) {
                return true;
            }

            // Check specific permission
            if (targetDomainObject instanceof ApplicationPermission applicationPermission
                    && permission instanceof ActionPermission actionPermission) {
                String appId = applicationPermission.getApplicationId();
                String action = actionPermission.name();

                List<String> actions = items.get(appId);
                return actions != null && actions.contains(action);
            }
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
            Object permission) {
        return false;
    }
}
```

---

## 6. Updated AuthController.java (Token Endpoints)

```java
package com.gyp.authservice.controllers;

import com.gyp.authservice.dtos.auth.*;
import com.gyp.authservice.exceptions.AuthenticationFailedException;
import com.gyp.authservice.services.AuthService;
import com.gyp.authservice.services.TokenPair;
import com.gyp.common.controllers.AbstractController;
import com.gyp.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class AuthController extends AbstractController {

    private static final String LOGIN_PATH = "/login";
    private static final String REGISTER_PATH = "/register";
    private static final String REFRESH_TOKEN_PATH = "/refresh-token";
    private static final String LOGOUT_PATH = "/logout";

    private final AuthService authService;

    @PostMapping(LOGIN_PATH)
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest request) {
        try {
            String clientIp = getClientIp(request);
            
            // Login returns token pair
            LoginResponseDto loginResponse = authService.login(loginRequestDto, clientIp);

            // Set refresh token as HTTPOnly cookie
            ResponseCookie refreshTokenCookie = ResponseCookie
                    .from("refreshToken", loginResponse.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)  // HTTPS only in production
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)  // 7 days
                    .sameSite("Strict")
                    .build();

            // Return access token in response body
            Map<String, Object> response = new HashMap<>();
            response.put("access_token", loginResponse.getAccessToken());
            response.put("token_type", "Bearer");
            response.put("expires_in", 900);  // 15 minutes
            response.put("user", Map.of(
                    "id", loginResponse.getUserId(),
                    "username", loginResponse.getUsername(),
                    "organizationId", loginResponse.getOrganizationId()
            ));

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body(response);

        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "invalid_credentials", "message", "Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "server_error", "message", "Login failed"));
        }
    }

    @PostMapping(REGISTER_PATH)
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {
        try {
            var userDto = authService.register(registerRequestDto);
            return ResponseEntity.status(201).body(userDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "validation_error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "server_error", "message", "Registration failed"));
        }
    }

    @PostMapping(REFRESH_TOKEN_PATH)
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        try {
            TokenPair tokenPair = authService.refreshToken(refreshToken);

            // Update refresh token cookie
            ResponseCookie newRefreshTokenCookie = ResponseCookie
                    .from("refreshToken", tokenPair.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Strict")
                    .build();

            Map<String, Object> response = new HashMap<>();
            response.put("access_token", tokenPair.getAccessToken());
            response.put("token_type", "Bearer");
            response.put("expires_in", 900);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
                    .body(response);

        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "invalid_token", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "server_error", "message", "Token refresh failed"));
        }
    }

    @PostMapping(LOGOUT_PATH)
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            authService.logout(token);

            // Clear refresh token cookie
            ResponseCookie clearCookie = ResponseCookie
                    .from("refreshToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(0)
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                    .body(Map.of("message", "Logged out successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "server_error", "message", "Logout failed"));
        }
    }

    // Helper to extract client IP
    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }
}
```

---

## 7. application.properties Configuration

```properties
# auth-service/src/main/resources/application.properties

# JWT Configuration (RS256)
# Private key: Stored in secure vault (Keycloak, HashiCorp Vault, K8s Secret)
jwt.private-key=${JWT_PRIVATE_KEY}

# Public key: Can be in config (it's public)
jwt.public-key=${JWT_PUBLIC_KEY}

# Token expiry
jwt.access-token.expiry=900       # 15 minutes
jwt.refresh-token.expiry=604800   # 7 days

# ===============================
# Redis Configuration (for blacklist)
# ===============================
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=12345
spring.data.redis.database=0
```

---

## 8. API Gateway Rate Limiting (application.yml)

```yaml
# api-gateway/src/main/resources/application-prod.yml

spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://AUTH-SERVICE
          predicates:
            - Path=/auths/**
          filters:
            - RewritePath=/auths(?<segment>/?.*), ${segment}
            # Rate limiting: 10 requests per minute per IP
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
                key-resolver: "#{@ipAddressKeyResolver}"
        
        - id: event-service
          uri: lb://EVENT-SERVICE
          # ... other routes with higher rate limits
```

```java
// Configuration class for rate limiter
@Configuration
public class RateLimiterConfiguration {
    
    @Bean
    public KeyResolver ipAddressKeyResolver() {
        return exchange -> Mono.just(
            exchange.getRequest().getHeaders().getFirst("X-Forwarded-For") != null ?
            exchange.getRequest().getHeaders().getFirst("X-Forwarded-For") :
            exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        );
    }
}
```

---

## Database Migration (Flyway)

Create `auth-service/src/main/resources/db/migration/V1__add_token_info.sql`:

```sql
-- Add fields to track login attempts and token info
ALTER TABLE user_account 
ADD COLUMN last_login TIMESTAMP NULL,
ADD COLUMN failed_login_attempts INT DEFAULT 0,
ADD COLUMN account_locked BOOLEAN DEFAULT FALSE,
ADD COLUMN account_locked_until TIMESTAMP NULL,
ADD COLUMN active BOOLEAN DEFAULT TRUE;

-- Create audit table
CREATE TABLE auth_audit (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    event_type VARCHAR(50),  -- LOGIN_SUCCESS, LOGIN_FAILURE, LOGOUT, TOKEN_REFRESH, etc.
    client_ip VARCHAR(45),
    user_agent VARCHAR(500),
    details JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE SET NULL
);

CREATE INDEX idx_auth_audit_user_id ON auth_audit(user_id);
CREATE INDEX idx_auth_audit_created_at ON auth_audit(created_at);
```

---

## Summary of Changes

| Component | Change | Priority |
|-----------|--------|----------|
| JwtTokenProvider | RS256 signing + token pair | 🔴 P0 |
| JwtDecoderEncoderConfiguration | Use public key only | 🔴 P0 |
| AuthServiceImpl | Exceptions instead of null | 🔴 P0 |
| CustomPermissionEvaluator | Blacklist + token type check | 🔴 P0 |
| AuthController | Token endpoints + logout | 🔴 P0 |
| API Gateway | Rate limiting on auth routes | 🟠 P1 |
| Redis | Token blacklist storage | 🟠 P1 |
| Audit Service | Login/access denial tracking | 🟠 P1 |
| Database | Add audit tables + user fields | 🟠 P1 |

**Total effort**: ~3 days for basic implementation + 1 day for testing

---

**Next**: Would you like me to help integrate this code into your repository?

