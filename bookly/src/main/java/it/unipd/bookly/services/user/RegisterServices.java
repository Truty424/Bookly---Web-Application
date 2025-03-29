package it.unipd.bookly.services.user;

import it.unipd.bookly.Resource.User;
import it.unipd.bookly.utilities.ErrorCode;

public class RegisterServices {

    public static boolean usernameValidation(String username, ErrorCode errorCode) {
        if (username == null || username.trim().isEmpty()) {
            errorCode.setCode(ErrorCode.USERNAME_MISSING.getCode());
            return false;
        }
        return true;
    }

    public static boolean firstNameValidation(String firstName, ErrorCode errorCode) {
        if (firstName == null || firstName.trim().isEmpty()) {
            errorCode.setCode(ErrorCode.FIRST_NAME_MISSING.getCode());
            return false;
        }
        return true;
    }

    public static boolean lastNameValidation(String lastName, ErrorCode errorCode) {
        if (lastName == null || lastName.trim().isEmpty()) {
            errorCode.setCode(ErrorCode.LAST_NAME_MISSING.getCode());
            return false;
        }
        return true;
    }

    public static boolean passwordValidation(String password, ErrorCode errorCode) {
        if (password == null || password.trim().isEmpty()) {
            errorCode.setCode(ErrorCode.PASSWORD_MISSING.getCode());
            return false;
        }
        return true;
    }

    public static boolean emailValidation(String email, ErrorCode errorCode) {
        if (email == null || email.trim().isEmpty()) {
            errorCode.setCode(ErrorCode.EMAIL_MISSING.getCode());
            return false;
        }
        return true;
    }

    public static boolean passwordConfirmation(String password, String rePassword, ErrorCode errorCode) {
        if (rePassword == null || !rePassword.equals(password)) {
            errorCode.setCode(ErrorCode.DIFFERENT_PASSWORDS.getCode());
            return false;
        }
        return true;
    }

    public static boolean registerValidation(User user, String rePassword, ErrorCode errorCode) {
        boolean valid = true;

        if (!usernameValidation(user.getUsername(), errorCode) ||
            !firstNameValidation(user.getFirstName(), errorCode) ||
            !lastNameValidation(user.getLastName(), errorCode) ||
            !emailValidation(user.getEmail(), errorCode) ||
            !passwordValidation(user.getPassword(), errorCode) ||
            !passwordConfirmation(user.getPassword(), rePassword, errorCode)) {

            errorCode.setCode(ErrorCode.USER_REGISTRATION_INVALID.getCode());
            valid = false;
        }

        return valid;
    }
}
