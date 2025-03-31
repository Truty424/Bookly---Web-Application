package it.unipd.bookly.dao.order;

import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.order.OrderQueries.GET_ORDERS_BY_STATUS;

/**
 * DAO to retrieve orders by their status.
 */
public class GetOrdersByStatusDAO extends AbstractDAO<List<Order>> {

    private final String status;

    public GetOrdersByStatusDAO(Connection con, String status) {
        super(con);
        this.status = status;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_ORDERS_BY_STATUS)) {
            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(new Order(
                        rs.getInt("order_id"),
                        rs.getDouble("total_amount"),
                        rs.getString("payment_method"),
                        rs.getTimestamp("payment_date"),
                        rs.getString("status")
                    ));
                }
            }
        }

        this.outputParam = orders;
    }
}