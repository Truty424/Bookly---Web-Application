package it.unipd.bookly.dao.discount;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.discount.DiscountQueries.GET_VALID_DISCOUNT_BY_CODE;

/**
 * DAO class to retrieve a valid (non-expired) discount by its code.
 */
public class GetValidDiscountByCodeDAO extends AbstractDAO<Discount> {

    private final String code;

    /**
     * Constructor to create DAO instance.
     *
     * @param con  the database connection.
     * @param code the discount code to look up.
     */
    public GetValidDiscountByCodeDAO(final Connection con, final String code) {
        super(con);
        this.code = code;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(GET_VALID_DISCOUNT_BY_CODE)) {
            stmnt.setString(1, code);

            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    Discount discount = new Discount(
                            rs.getInt("discount_id"),
                            rs.getString("code"),
                            rs.getDouble("discount_percentage"),
                            rs.getTimestamp("expired_date")
                    );
                    this.outputParam = discount;
                    LOGGER.info("Valid discount found for code '{}'.", code);
                } else {
                    LOGGER.warn("No valid discount found for code '{}'.", code);
                    this.outputParam = null;
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Error retrieving valid discount with code '{}': {}", code, ex.getMessage());
            throw ex;
        }
    }
}
