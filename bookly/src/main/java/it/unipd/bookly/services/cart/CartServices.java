package it.unipd.bookly.services.cart;

import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.utilities.ErrorCode;

public class CartServices {

    /**
     * Validates if a cart object is valid before adding it to the database.
     *
     * @param cart       The cart to validate.
     * @param errorCode  The error code holder for reporting validation errors.
     * @return true if valid, false otherwise.
     */
    public static boolean validateNewCartEntry(Cart cart, ErrorCode errorCode) {
        return CartValidation.validateCart(cart, errorCode);
    }

    /**
     * Validates if a cart item can be updated (mainly checking quantity).
     *
     * @param quantity   The new quantity to validate.
     * @param errorCode  The error code holder.
     * @return true if valid, false otherwise.
     */
    public static boolean validateUpdateQuantity(int quantity, ErrorCode errorCode) {
        return CartValidation.quantityValidation(quantity, errorCode);
    }

    /**
     * Validates identifiers for cart removal (e.g., book and user).
     *
     * @param bookId     The book ID to remove.
     * @param userId     The user performing the action.
     * @param errorCode  The error code holder.
     * @return true if both IDs are valid.
     */
    public static boolean validateCartRemoval(int bookId, int userId, ErrorCode errorCode) {
        return CartValidation.bookIdValidation(bookId, errorCode)
                && CartValidation.userIdValidation(userId, errorCode);
    }

    /**
     * Basic validation for loading a user's cart.
     *
     * @param userId     The user ID whose cart is being loaded.
     * @param errorCode  The error code holder.
     * @return true if valid.
     */
    public static boolean validateCartRetrieval(int userId, ErrorCode errorCode) {
        return CartValidation.userIdValidation(userId, errorCode);
    }
}
