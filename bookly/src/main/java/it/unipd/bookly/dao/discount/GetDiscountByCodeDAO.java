package it.unipd.bookly.dao.discount;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import static it.unipd.bookly.dao.discount.DiscountQueries.GET_DISCOUNT_BY_ID;

/**
 * DAO class to retrieve a discount by its ID.
 */
public class GetDiscountByIDDAO extends AbstractDAO<Discount> {

    private final int discountId;

    /**
     * Constructor to create DAO instance.
     *
     * @param con         the database connection.
     * @param discountId  the ID of the discount to retrieve.
     */
    public GetDiscountByIDDAO(final Connection con, final int discountId) {
        super(con);
        this.discountId = discountId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(GET_DISCOUNT_BY_ID)) {
            stmnt.setInt(1, discountId);

            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    Discount discount = new Discount(
                            rs.getInt("discount_id"),
                            rs.getString("code"),
                            rs.getDouble("discount_percentage"),
                            rs.getTimestamp("expired_date") // This line changed
                    );
                    this.outputParam = discount;
                    LOGGER.info("Discount retrieved for ID {}.", discountId);
                } else {
                    LOGGER.warn("No discount found for ID {}.", discountId);
                    this.outputParam = null;
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Error retrieving discount with ID {}: {}", discountId, ex.getMessage());
            throw ex;
        }
    }
}
