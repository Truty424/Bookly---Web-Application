package it.unipd.bookly.dao.order;

import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.order.OrderQueries.GET_ORDER_BY_ID;

/**
 * DAO to retrieve an order by its ID.
 */
public class GetOrderByIdDAO extends AbstractDAO<Order> {

    private final int orderId;

    public GetOrderByIdDAO(Connection con, int orderId) {
        super(con);
        this.orderId = orderId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_ORDER_BY_ID)) {
            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = new Order(
                        rs.getInt("order_id"),
                        rs.getDouble("total_amount"),
                        rs.getString("payment_method"),
                        rs.getTimestamp("payment_date"),
                        rs.getString("status")
                    );
                }
            }
        }
    }
}