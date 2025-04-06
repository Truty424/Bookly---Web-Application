package it.unipd.bookly.services.user;

import it.unipd.bookly.utilities.ErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Service for user authentication validation
 */
public class LoginServices {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_]{5,20}$");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    /**
     * Validates username or email format.
     */
    public static boolean usernameOrEmailValidation(String usernameOrEmail, ErrorCode errorCode) {
        boolean flag = true;

        if (StringUtils.isBlank(usernameOrEmail)) {
            errorCode = ErrorCode.USERNAME_OR_EMAIL_MISSING;
            flag = false;
        } else if (usernameOrEmail.contains("@")) {
            if (!EMAIL_PATTERN.matcher(usernameOrEmail).matches()) {
                errorCode = ErrorCode.INVALID_EMAIL_FORMAT;
                flag = false;
            }
        } else {
            if (!USERNAME_PATTERN.matcher(usernameOrEmail).matches()) {
                errorCode = ErrorCode.INVALID_USERNAME_FORMAT;
                flag = false;
            }
        }

        return flag;
    }

    /**
     * Validates password format.
     */
    public static boolean passwordValidation(String password, ErrorCode errorCode) {
        boolean flag = true;

        if (StringUtils.isBlank(password)) {
            errorCode = ErrorCode.PASSWORD_MISSING;
            flag = false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errorCode = ErrorCode.WEAK_PASSWORD;
            flag = false;
        }

        return flag;
    }

    /**
     * Overall login input validation.
     */
    public static boolean loginValidation(String usernameOrEmail, String password, ErrorCode errorCode) {
        boolean flag = true;

        if (!usernameOrEmailValidation(usernameOrEmail, errorCode)) {
            flag = false;
        } else if (!passwordValidation(password, errorCode)) {
            flag = false;
        }

        return flag;
    }
}
