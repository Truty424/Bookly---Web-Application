package it.unipd.bookly.services.cart;

import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.Resource.CartItem;
import it.unipd.bookly.utilities.ErrorCode;

import java.util.List;

public class CartServices {
    private static final int MAX_ITEMS_PER_CART = 20;
    private static final double MAX_CART_TOTAL = 1000.00;

    /**
     * Validates all cart fields.
     */
    public static boolean validateCart(Cart cart, ErrorCode errorCode) {
        boolean flag = true;

        if (cart == null) {
            errorCode = ErrorCode.INVALID_CART_OBJECT;
            flag = false;
        }

        if (!validateUserId(cart.getUserId(), errorCode)) flag = false;
        if (!validateItems(cart.getItems(), errorCode)) flag = false;
        if (!validateTotalAmount(cart.getTotalPrice(), errorCode)) flag = false;

        return flag;
    }

    /**
     * Validates user ID is positive.
     */
    public static boolean validateUserId(int userId, ErrorCode errorCode) {
        boolean flag = true;

        if (userId <= 0) {
            errorCode = ErrorCode.INVALID_USER_ID;
            flag = false;
        }

        return flag;
    }

    /**
     * Validates cart items.
     */
    public static boolean validateItems(List<CartItem> items, ErrorCode errorCode) {
        boolean flag = true;

        if (items == null || items.isEmpty()) {
            errorCode = ErrorCode.CART_EMPTY;
            flag = false;
        } else if (items.size() > MAX_ITEMS_PER_CART) {
            errorCode = ErrorCode.CART_LIMIT_EXCEEDED;
            flag = false;
        } else {
            for (CartItem item : items) {
                if (item.getQuantity() <= 0) {
                    errorCode = ErrorCode.INVALID_ITEM_QUANTITY;
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }

    /**
     * Validates total amount is reasonable.
     */
    public static boolean validateTotalAmount(double total, ErrorCode errorCode) {
        boolean flag = true;

        if (total <= 0) {
            errorCode = ErrorCode.INVALID_CART_TOTAL;
            flag = false;
        } else if (total > MAX_CART_TOTAL) {
            errorCode = ErrorCode.CART_TOTAL_TOO_HIGH;
            flag = false;
        }

        return flag;
    }

    /**
     * Validates cart is ready for checkout.
     */
    public static boolean validateForCheckout(Cart cart, ErrorCode errorCode) {
        boolean flag = validateCart(cart, errorCode);

        if (flag && (cart.getShipmentMethod() == null || cart.getShipmentMethod().isEmpty())) {
            errorCode = ErrorCode.MISSING_SHIPPING_METHOD;
            flag = false;
        }

        return flag;
    }
}
