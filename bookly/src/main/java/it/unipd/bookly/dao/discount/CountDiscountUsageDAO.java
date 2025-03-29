package it.unipd.bookly.dao.discount;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.discount.DiscountQueries.COUNT_DISCOUNT_USAGE;

/**
 * DAO class to count how many times a discount has been used in shopping carts.
 */
public class CountDiscountUsageDAO extends AbstractDAO<Integer> {

    private final int discountId;

    /**
     * Constructor to create DAO instance.
     *
     * @param con         the database connection.
     * @param discountId  the discount ID to count usage for.
     */
    public CountDiscountUsageDAO(final Connection con, final int discountId) {
        super(con);
        this.discountId = discountId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(COUNT_DISCOUNT_USAGE)) {
            stmnt.setInt(1, discountId);

            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    this.outputParam = count;
                    LOGGER.info("Discount with ID {} has been used {} time(s).", discountId, count);
                } else {
                    LOGGER.warn("No count result for discount ID {} — setting to 0.", discountId);
                    this.outputParam = 0;
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Error counting usage of discount ID {}: {}", discountId, ex.getMessage());
            throw ex;
        }
    }
}
