package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.cart.CartQueries.*;
import static it.unipd.bookly.dao.discount.DiscountQueries.*;

/**
 * DAO class to apply a discount to a shopping cart and return the discounted price.
 */
public class ApplyDiscountToCartDAO extends AbstractDAO<BigDecimal> {

    private final int cartId;
    private final int discountId;

    public ApplyDiscountToCartDAO(final Connection con, final int cartId, final int discountId) {
        super(con);
        this.cartId = cartId;
        this.discountId = discountId;
    }

    @Override
    protected void doAccess() throws Exception {
        BigDecimal totalPrice;
        BigDecimal discountPercentage;

        try (
                PreparedStatement getTotalStmt = con.prepareStatement(GET_CART_TOTAL);
                PreparedStatement getDiscountStmt = con.prepareStatement(GET_DISCOUNT_PERCENTAGE);
                PreparedStatement applyStmt = con.prepareStatement(APPLY_DISCOUNT)
        ) {
            // Get cart total price
            getTotalStmt.setInt(1, cartId);
            try (ResultSet rs = getTotalStmt.executeQuery()) {
                if (rs.next()) {
                    totalPrice = rs.getBigDecimal("total_price");
                } else {
                    throw new Exception("Cart not found for ID: " + cartId);
                }
            }

            // Get discount percentage
            getDiscountStmt.setInt(1, discountId);
            try (ResultSet rs = getDiscountStmt.executeQuery()) {
                if (rs.next()) {
                    discountPercentage = rs.getBigDecimal("discount_percentage");
                } else {
                    throw new Exception("Discount not found for ID: " + discountId);
                }
            }

            // Link discount to cart
            applyStmt.setInt(1, discountId);
            applyStmt.setInt(2, cartId);
            applyStmt.executeUpdate();

            // Calculate: total * (1 - discount / 100)
            BigDecimal discountFactor = BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            BigDecimal discountedPrice = totalPrice.multiply(discountFactor);

            this.outputParam = discountedPrice;

            LOGGER.info("Discount {} applied to cart {}. Discounted price: {}", discountId, cartId, discountedPrice);

        } catch (Exception ex) {
            LOGGER.error("Error applying discount ID {} to cart {}: {}", discountId, cartId, ex.getMessage());
            throw ex;
        }
    }
}
