package it.unipd.bookly.dao.order;

import it.unipd.bookly.model.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetUserOrdersDAO {
    private static final String GET_USER_ORDERS =
        "SELECT order_id, total_price, payment_method, order_date, address, shipment_code, status " +
        "FROM booklySchema.orders WHERE order_id IN " +
        "(SELECT order_id FROM booklySchema.shoppingcart WHERE user_id = ?)";

    public List<Order> getUserOrders(Connection connection, int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(GET_USER_ORDERS)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new Order(
                        rs.getInt("order_id"),
                        rs.getBigDecimal("total_price"),
                        rs.getString("payment_method"),
                        rs.getTimestamp("order_date"),
                        rs.getString("address"),
                        rs.getString("shipment_code"),
                        rs.getString("status")
                    ));
                }
            }
        }
        return orders;
    }
}