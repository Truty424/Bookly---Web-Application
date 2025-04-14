package it.unipd.bookly.dao.discount;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static it.unipd.bookly.dao.discount.DiscountQueries.INSERT_DISCOUNT;

/**
 * DAO class to insert a new discount into the database.
 */
public class InsertDiscountDAO extends AbstractDAO<Boolean> {

    private final Discount discount;

    /**
     * Constructs a DAO to insert a discount.
     *
     * @param con      the database connection.
     * @param discount the Discount object containing code, percentage, and expiration date.
     */
    public InsertDiscountDAO(final Connection con, final Discount discount) {
        super(con);
        this.discount = discount;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_DISCOUNT)) {
            stmt.setString(1, discount.getCode());
            stmt.setDouble(2, discount.getDiscountPercentage());
            stmt.setTimestamp(3, discount.getExpiredDate());

            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;

            if (rowsAffected > 0) {
                LOGGER.info("Discount '{}' inserted successfully.", discount.getCode());
            } else {
                LOGGER.warn("No rows inserted for discount '{}'.", discount.getCode());
            }

        } catch (SQLException ex) {
            LOGGER.error("Error inserting discount '{}': {}", discount.getCode(), ex.getMessage());
            throw ex;
        }
    }
}
