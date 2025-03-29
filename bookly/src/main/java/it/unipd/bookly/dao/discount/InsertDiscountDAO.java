package it.unipd.bookly.dao.discount;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import static it.unipd.bookly.dao.discount.DiscountQueries.INSERT_DISCOUNT;

/**
 * DAO class to insert a new discount into the database.
 */
public class InsertDiscountDAO extends AbstractDAO<Void> {

    private final String code;
    private final double discountPercentage;
    private final Timestamp expiredDate;

    /**
     * Constructor to create DAO instance.
     *
     * @param con                the database connection.
     * @param code               the discount code.
     * @param discountPercentage the percentage value of the discount.
     * @param expiredDate        the expiration date of the discount.
     */
    public InsertDiscountDAO(final Connection con, final String code, final double discountPercentage, final Timestamp expiredDate) {
        super(con);
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.expiredDate = expiredDate;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(INSERT_DISCOUNT)) {
            stmnt.setString(1, code);
            stmnt.setDouble(2, discountPercentage);
            stmnt.setTimestamp(3, expiredDate);

            stmnt.executeUpdate();

            LOGGER.info("New discount inserted: code={}, discountPercentage={}, expiredDate={}", code, discountPercentage, expiredDate);
        } catch (Exception ex) {
            LOGGER.error("Error inserting discount with code {}: {}", code, ex.getMessage());
            throw ex;
        }
    }
}
