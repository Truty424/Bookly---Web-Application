package it.unipd.bookly.dao.order;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import static it.unipd.bookly.dao.order.OrderQueries.UPDATE_ORDER_PAYMENT_INFO;

/**
 * DAO to update the payment information of an order.
 */
public class UpdateOrderPaymentInfoDAO extends AbstractDAO<Boolean> {

    private final int orderId;
    private final double totalAmount;
    private final String paymentMethod;
    private final Timestamp paymentDate;

    public UpdateOrderPaymentInfoDAO(Connection con, int orderId, double totalAmount, String paymentMethod, Timestamp paymentDate) {
        super(con);
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_ORDER_PAYMENT_INFO)) {
            stmt.setDouble(1, totalAmount);
            stmt.setString(2, paymentMethod);
            stmt.setTimestamp(3, paymentDate);
            stmt.setInt(4, orderId);

            int rowsUpdated = stmt.executeUpdate();
            this.outputParam = rowsUpdated > 0;
        }
    }
}