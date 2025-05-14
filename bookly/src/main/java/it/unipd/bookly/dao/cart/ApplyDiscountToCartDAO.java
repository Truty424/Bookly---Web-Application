package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.dao.discount.DiscountQueries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.cart.CartQueries.GET_CART_TOTAL;
import static it.unipd.bookly.dao.discount.DiscountQueries.GET_DISCOUNT_PERCENTAGE;
import static it.unipd.bookly.dao.cart.CartQueries.APPLY_DISCOUNT;

/**
 * DAO class to apply a discount to a shopping cart and return the discounted price using double.
 */
public class ApplyDiscountToCartDAO extends AbstractDAO<Double> {

    private final int cartId;
    private final int discountId;

    public ApplyDiscountToCartDAO(final Connection con, final int cartId, final int discountId) {
        super(con);
        this.cartId = cartId;
        this.discountId = discountId;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false); // Begin transaction

        try (
                PreparedStatement getTotalStmt = con.prepareStatement(CartQueries.GET_CART_TOTAL);
                PreparedStatement getDiscountStmt = con.prepareStatement(DiscountQueries.GET_DISCOUNT_PERCENTAGE);
                PreparedStatement updateCartStmt = con.prepareStatement(CartQueries.UPDATE_CART_TOTAL_WITH_DISCOUNT)
        ) {
            // 1. Get cart total price
            double totalPrice;
            getTotalStmt.setInt(1, cartId);
            try (ResultSet rs = getTotalStmt.executeQuery()) {
                if (rs.next()) {
                    totalPrice = rs.getDouble("total_price");
                } else {
                    throw new Exception("Cart not found for ID: " + cartId);
                }
            }

            // 2. Get discount percentage
            double discountPercentage;
            getDiscountStmt.setInt(1, discountId);
            try (ResultSet rs = getDiscountStmt.executeQuery()) {
                if (rs.next()) {
                    discountPercentage = rs.getDouble("discount_percentage");
                } else {
                    throw new Exception("Discount not found for ID: " + discountId);
                }
            }

            // 3. Calculate the discounted price
            double discountedPrice = totalPrice * (1 - discountPercentage / 100.0);

            // 4. Update the cart with discounted price and discount ID
            updateCartStmt.setDouble(1, discountedPrice);
            updateCartStmt.setInt(2, discountId);
            updateCartStmt.setInt(3, cartId);
            updateCartStmt.executeUpdate();

            // 5. Set the discounted price as output and commit
            this.outputParam = discountedPrice;
            con.commit();

            LOGGER.info("Discount {} applied to cart {}. Final price: {}", discountId, cartId, discountedPrice);

        } catch (Exception ex) {
            con.rollback(); // Roll back all operations if anything fails
            LOGGER.error("Failed to apply discount {} to cart {}: {}", discountId, cartId, ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(originalAutoCommit); // Restore original autocommit setting
        }
    }
}