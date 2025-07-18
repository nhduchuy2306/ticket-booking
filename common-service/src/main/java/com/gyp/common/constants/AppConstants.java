package com.gyp.common.constants;

import java.time.format.DateTimeFormatter;

/**
 * Centralized application constants
 *
 * <p>Contains all application-wide constants grouped by functionality.</p>
 */
public final class AppConstants {

	// Private constructor to prevent instantiation
	private AppConstants() {
	}

	// ========== AUTHENTICATION & SECURITY ==========
	public static final String APP_TOKEN_PREFIX = "Bearer ";
	public static final String APP_HEADER_USER_PAYLOAD = "X-User-Payload";
	public static final String APP_HEADER_USER_SUBJECT = "X-User-Subject";
	public static final String APP_HEADER_SCOPE = "Scope_";
	public static final int TOKEN_EXPIRATION_HOURS = 24;
	public static final String DEFAULT_USER_ROLE = "ROLE_USER";
	public static final String ADMIN_ROLE = "ROLE_ADMIN";

	// ========== SERVICE ENDPOINTS ==========
	public static final String AUTH_SERVICE_API = "http://localhost:8888/AUTH-SERVICE";
	public static final String EVENT_SERVICE_API = "http://localhost:8888/EVENT-SERVICE";
	public static final String NOTIFICATION_SERVICE_API = "http://localhost:8888/NOTIFICATION-SERVICE";
	public static final String FALLBACK_SERVICE_URL = "http://localhost:8080/fallback";

	// ========== DATE/TIME FORMATS ==========
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
	public static final DateTimeFormatter API_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT);
	public static final String TIMEZONE_UTC = "UTC";

	// ========== CONFIGURATION KEYS ==========
	public static final String CONFIG_MAX_PAGE_SIZE = "app.pagination.max-page-size";
	public static final String CONFIG_CACHE_TTL = "app.cache.time-to-live";
	public static final String CONFIG_RETRY_ATTEMPTS = "app.retry.attempts";

	// ========== DEFAULT VALUES ==========
	public static final int DEFAULT_PAGE_SIZE = 20;
	public static final int MAX_PAGE_SIZE = 100;
	public static final int DEFAULT_CACHE_TTL_MINUTES = 30;
	public static final String DEFAULT_LANGUAGE = "en-US";
	public static final String UNKNOWN_USER = "anonymous";

	// ========== ERROR MESSAGES ==========
	public static final String ERROR_INVALID_TOKEN = "Invalid or expired token";
	public static final String ERROR_ACCESS_DENIED = "Access denied. Required role: %s";
	public static final String ERROR_SERVICE_UNAVAILABLE = "Service temporarily unavailable";
	public static final String ERROR_INVALID_INPUT = "Invalid input for field: %s";

	// ========== COMMON DELIMITERS/SYMBOLS ==========
	public static final String COMMA = ",";
	public static final String SEMICOLON = ";";
	public static final String COLON = ":";
	public static final String SLASH = "/";
	public static final String PIPE = "|";
	public static final String UNDERSCORE = "_";
	public static final String DASH = "-";
	public static final String DOT = ".";
	public static final String MINUS = "-";

	// ========== BRACKETS ==========
	public static final String SQUARE_BRACKET_OPEN = "[";
	public static final String SQUARE_BRACKET_CLOSE = "]";
	public static final String CURLY_BRACKET_OPEN = "{";
	public static final String CURLY_BRACKET_CLOSE = "}";

	// ========== HTTP RELATED ==========
	public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
	public static final String HEADER_REQUEST_ID = "X-Request-ID";
	public static final int HTTP_TIMEOUT_MILLIS = 5000;

	// ========== CACHE NAMES ==========
	public static final String CACHE_USER_PROFILES = "userProfiles";
	public static final String CACHE_APPLICATION_SETTINGS = "appSettings";

	// ========== VALIDATION CONSTANTS ==========
	public static final int MAX_EMAIL_LENGTH = 254;
	public static final int MIN_PASSWORD_LENGTH = 8;
	public static final int MAX_PASSWORD_LENGTH = 128;
	public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

	// ========== PAGINATION CONSTANTS ==========
	public static final String DEFAULT_SORT_DIRECTION = "ASC";
	public static final String DEFAULT_SORT_FIELD = "id";
	public static final int DEFAULT_PAGE_NUMBER = 0;

	// ========== ENVIRONMENT PROFILES ==========
	public static final String PROFILE_DEV = "dev";
	public static final String PROFILE_TEST = "test";
	public static final String PROFILE_PROD = "prod";

	// ========== AUDIT LOGGING ==========
	public static final String AUDIT_SYSTEM_USER = "SYSTEM";
	public static final String AUDIT_ACTION_CREATE = "CREATE";
	public static final String AUDIT_ACTION_UPDATE = "UPDATE";
	public static final String AUDIT_ACTION_DELETE = "DELETE";

	// ========== FILE/STORAGE CONSTANTS ==========
	public static final long MAX_FILE_SIZE_BYTES = 10_485_760; // 10MB
	public static final String[] ALLOWED_FILE_TYPES = { "image/jpeg", "image/png", "application/pdf" };
}