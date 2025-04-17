package it.unipd.bookly.dao.discount;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.discount.DiscountQueries.DELETE_DISCOUNT;

/**
 * DAO class to delete a discount by its ID.
 */
public class DeleteDiscountDAO extends AbstractDAO<Boolean> {

    private final int discountId;

    /**
     * Constructor to create DAO instance.
     *
     * @param con the database connection.
     * @param discountId the ID of the discount to delete.
     */
    public DeleteDiscountDAO(final Connection con, final int discountId) {
        super(con);
        this.discountId = discountId;
    }

    @Override
    protected void doAccess() throws Exception {
        try {
            try (PreparedStatement clearDiscount = con.prepareStatement(
                    "UPDATE booklySchema.shoppingcart SET discount_id = NULL WHERE discount_id = ?")) {
                clearDiscount.setInt(1, discountId);
                clearDiscount.executeUpdate();
            }

            try (PreparedStatement stmnt = con.prepareStatement(DELETE_DISCOUNT)) {
                stmnt.setInt(1, discountId);
                int rowsAffected = stmnt.executeUpdate();

                this.outputParam = rowsAffected > 0;

                if (outputParam) {
                    LOGGER.info("Discount with ID {} deleted successfully.", discountId);
                } else {
                    LOGGER.warn("No discount found with ID {}. Nothing was deleted.", discountId);
                }
            }

        } catch (Exception ex) {
            this.outputParam = false;
            LOGGER.error("Error deleting discount with ID {}: {}", discountId, ex.getMessage());
            throw ex;
        }
    }

}
