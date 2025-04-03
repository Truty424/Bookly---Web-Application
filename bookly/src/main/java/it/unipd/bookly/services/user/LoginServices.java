package it.unipd.bookly.services.user;

import it.unipd.bookly.utilities.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Pattern;

/**
 * Service for user authentication validation
 */
public class LoginServices {
    // Validation patterns
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_]{5,20}$");
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    /**
     * Validates username/email format
     */
    public static boolean usernameOrEmailValidation(String usernameOrEmail, ErrorCode errorCode) {
        if (StringUtils.isBlank(usernameOrEmail)) {
            errorCode.setCode(ErrorCode.USERNAME_OR_EMAIL_MISSING.getCode());
            return false;
        }
        
        // Check if input is email or username
        if (usernameOrEmail.contains("@")) {
            if (!EMAIL_PATTERN.matcher(usernameOrEmail).matches()) {
                errorCode.setCode(ErrorCode.INVALID_EMAIL_FORMAT.getCode());
                return false;
            }
        } else {
            if (!USERNAME_PATTERN.matcher(usernameOrEmail).matches()) {
                errorCode.setCode(ErrorCode.INVALID_USERNAME_FORMAT.getCode());
                return false;
            }
        }
        return true;
    }

    /**
     * Validates password meets security requirements
     */
    public static boolean passwordValidation(String password, ErrorCode errorCode) {
        if (StringUtils.isBlank(password)) {
            errorCode.setCode(ErrorCode.PASSWORD_MISSING.getCode());
            return false;
        }
        
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errorCode.setCode(ErrorCode.WEAK_PASSWORD.getCode());
            return false;
        }
        return true;
    }

    /**
     * Complete login validation
     */
    public static boolean loginValidation(String usernameOrEmail, String password, ErrorCode errorCode) {
        if (!usernameOrEmailValidation(usernameOrEmail, errorCode) || 
            !passwordValidation(password, errorCode)) {
            // Specific error codes already set by individual validation methods
            return false;
        }
        return true;
    }
}