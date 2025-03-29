package it.unipd.bookly.services.user;

import it.unipd.bookly.utilities.ErrorCode;

/**
 * Service class for user login validation logic.
 */
public class LoginServices {

    /**
     * Validates the username or email for login.
     *
     * @param usernameOrEmail the provided username or email.
     * @param errorCode       the error code reference to update on failure.
     * @return true if valid, false otherwise.
     */
    public static boolean usernameOrEmailValidation(String usernameOrEmail, ErrorCode errorCode) {
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
            errorCode.setCode(ErrorCode.USERNAME_OR_EMAIL_MISSING.getCode());
            return false;
        }
        return true;
    }

    /**
     * Validates the password for login.
     *
     * @param password  the provided password.
     * @param errorCode the error code reference to update on failure.
     * @return true if valid, false otherwise.
     */
    public static boolean passwordValidation(String password, ErrorCode errorCode) {
        if (password == null || password.trim().isEmpty()) {
            errorCode.setCode(ErrorCode.PASSWORD_MISSING.getCode());
            return false;
        }
        return true;
    }

    /**
     * Performs overall login validation.
     *
     * @param usernameOrEmail the username or email.
     * @param password        the password.
     * @param errorCode       the error code reference to update on failure.
     * @return true if all fields are valid, false otherwise.
     */
    public static boolean loginValidation(String usernameOrEmail, String password, ErrorCode errorCode) {
        boolean validUsername = usernameOrEmailValidation(usernameOrEmail, errorCode);
        boolean validPassword = passwordValidation(password, errorCode);

        if (!validUsername || !validPassword) {
            errorCode.setCode(ErrorCode.USERNAME_PASSWORD_MISSING.getCode());
            return false;
        }
        return true;
    }
}
