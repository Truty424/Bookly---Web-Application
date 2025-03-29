package it.unipd.bookly.services.cart;

import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.utilities.ErrorCode;

public class CartValidation {

    public static boolean cartIdValidation(int cartId, ErrorCode errorCode) {
        if (cartId <= 0) {
            errorCode.setCode(ErrorCode.INVALID_CART_ID.getCode());
            return false;
        }
        return true;
    }

    public static boolean userIdValidation(int userId, ErrorCode errorCode) {
        if (userId <= 0) {
            errorCode.setCode(ErrorCode.INVALID_USER_ID.getCode());
            return false;
        }
        return true;
    }

    public static boolean bookIdValidation(int bookId, ErrorCode errorCode) {
        if (bookId <= 0) {
            errorCode.setCode(ErrorCode.INVALID_BOOK_ID.getCode());
            return false;
        }
        return true;
    }

    public static boolean quantityValidation(int quantity, ErrorCode errorCode) {
        if (quantity <= 0) {
            errorCode.setCode(ErrorCode.INVALID_QUANTITY.getCode());
            return false;
        }
        return true;
    }

    public static boolean validateCart(Cart cart, ErrorCode errorCode) {
        boolean isValid = true;

        if (!userIdValidation(cart.getUserId(), errorCode)) {
            isValid = false;
        }

        if (!bookIdValidation(cart.getBookId(), errorCode)) {
            isValid = false;
        }

        if (!quantityValidation(cart.getQuantity(), errorCode)) {
            isValid = false;
        }

        return isValid;
    }
}
