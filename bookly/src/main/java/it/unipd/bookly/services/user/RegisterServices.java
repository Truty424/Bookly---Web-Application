package it.unipd.bookly.services.user;

import it.unipd.bookly.Resource.User;
import it.unipd.bookly.utilities.ErrorCode;

public class RegisterServices {

    public static boolean usernameValidation(String username, ErrorCode errorCode) {
        boolean flag = true;
        if (username == null || username.trim().isEmpty()) {
            errorCode = ErrorCode.USERNAME_MISSING;
            flag = false;
        }
        return flag;
    }

    public static boolean firstNameValidation(String firstName, ErrorCode errorCode) {
        boolean flag = true;
        if (firstName == null || firstName.trim().isEmpty()) {
            errorCode = ErrorCode.FIRST_NAME_MISSING;
            flag = false;
        }
        return flag;
    }

    public static boolean lastNameValidation(String lastName, ErrorCode errorCode) {
        boolean flag = true;
        if (lastName == null || lastName.trim().isEmpty()) {
            errorCode = ErrorCode.LAST_NAME_MISSING;
            flag = false;
        }
        return flag;
    }

    public static boolean passwordValidation(String password, ErrorCode errorCode) {
        boolean flag = true;
        if (password == null || password.trim().isEmpty()) {
            errorCode = ErrorCode.PASSWORD_MISSING;
            flag = false;
        }
        return flag;
    }

    public static boolean emailValidation(String email, ErrorCode errorCode) {
        boolean flag = true;
        if (email == null || email.trim().isEmpty()) {
            errorCode = ErrorCode.EMAIL_MISSING;
            flag = false;
        }
        return flag;
    }


    public static boolean registerValidation(User user, ErrorCode errorCode) {
        boolean flag = true;

        if (!usernameValidation(user.getUsername(), errorCode)) {
            flag = false;
        } else if (!firstNameValidation(user.getFirstName(), errorCode)) {
            flag = false;
        } else if (!lastNameValidation(user.getLastName(), errorCode)) {
            flag = false;
        } else if (!emailValidation(user.getEmail(), errorCode)) {
            flag = false;
        } else if (!passwordValidation(user.getPassword(), errorCode)) {
            flag = false;
        } 

        if (!flag) {
            errorCode = ErrorCode.USER_REGISTRATION_INVALID;
        }

        return flag;
    }
}
