package it.unipd.bookly.dao.discount;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.discount.DiscountQueries.GET_ALL_DISCOUNTS;

/**
 * DAO class to retrieve all discounts (expired and valid).
 */
public class GetAllDiscountDAO extends AbstractDAO<List<Discount>> {

    /**
     * Constructor to create DAO instance.
     *
     * @param con the database connection.
     */
    public GetAllDiscountDAO(final Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        List<Discount> discounts = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_ALL_DISCOUNTS);
             ResultSet rs = stmnt.executeQuery()) {

            while (rs.next()) {
                Discount discount = new Discount(
                        rs.getInt("discount_id"),
                        rs.getString("code"),
                        rs.getDouble("discount_percentage"),
                        rs.getTimestamp("expired_date")
                );
                discounts.add(discount);
            }

            this.outputParam = discounts;
            LOGGER.info("{} discount(s) retrieved from the database.", discounts.size());

        } catch (Exception ex) {
            LOGGER.error("Error retrieving all discounts: {}", ex.getMessage());
            throw ex;
        }
    }
}
