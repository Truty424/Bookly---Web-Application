package it.unipd.bookly.dao.discount;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.discount.DiscountQueries.GET_ALL_ACTIVE_DISCOUNTS;

/**
 * DAO class to retrieve all active (non-expired) discounts.
 */
public class GetAllActiveDiscountsDAO extends AbstractDAO<List<Discount>> {

    /**
     * Constructor to create DAO instance.
     *
     * @param con the database connection.
     */
    public GetAllActiveDiscountsDAO(final Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        List<Discount> activeDiscounts = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_ALL_ACTIVE_DISCOUNTS);
             ResultSet rs = stmnt.executeQuery()) {

            while (rs.next()) {
                Discount discount = new Discount(
                        rs.getInt("discount_id"),
                        rs.getString("code"),
                        rs.getDouble("discount_percentage"),
                        rs.getTimestamp("expired_date")
                );
                activeDiscounts.add(discount);
            }

            this.outputParam = activeDiscounts;
            LOGGER.info("{} active discount(s) retrieved from the database.", activeDiscounts.size());

        } catch (Exception ex) {
            LOGGER.error("Error retrieving active discounts: {}", ex.getMessage());
            throw ex;
        }
    }
}
