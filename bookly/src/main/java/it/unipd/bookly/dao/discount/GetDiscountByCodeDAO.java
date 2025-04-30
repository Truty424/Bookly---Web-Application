package it.unipd.bookly.dao.discount;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.discount.DiscountQueries.GET_DISCOUNT_BY_CODE;

/**
 * DAO class to retrieve a discount by its unique code.
 */
public class GetDiscountByCodeDAO extends AbstractDAO<Discount> {

    private final String discountCode;

    /**
     * Constructor to create DAO instance.
     *
     * @param con          the database connection
     * @param discountCode the unique discount code to search for
     */
    public GetDiscountByCodeDAO(final Connection con, final String discountCode) {
        super(con);
        this.discountCode = discountCode != null ? discountCode.trim() : null;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_DISCOUNT_BY_CODE)) {
            stmt.setString(1, discountCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Discount discount = new Discount(
                        rs.getInt("discount_id"),
                        rs.getString("code"),
                        rs.getDouble("discount_percentage"),
                        rs.getTimestamp("expired_date")
                    );
                    this.outputParam = discount;
                    LOGGER.info("Discount retrieved for code '{}'.", discountCode);
                } else {
                    LOGGER.warn("No discount found for code '{}'.", discountCode);
                    this.outputParam = null;
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Error retrieving discount with code '{}': {}", discountCode, ex.getMessage());
            throw ex;
        }
    }
}
