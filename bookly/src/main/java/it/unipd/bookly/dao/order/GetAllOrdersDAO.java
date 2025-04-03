package it.unipd.bookly.dao.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.order.OrderQueries.GET_ALL_ORDERS;

/**
 * DAO to retrieve all orders from the database.
 */
public class GetAllOrdersDAO extends AbstractDAO<List<Order>> {

    public GetAllOrdersDAO(Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_ALL_ORDERS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                double totalPrice = rs.getDouble("total_price");
                String paymentMethod = rs.getString("payment_method");
                java.sql.Timestamp orderDate = rs.getTimestamp("order_date");
                String address = rs.getString("address");
                String shipmentCode = rs.getString("shipment_code");
                String status = rs.getString("status");

                Order order = new Order(
                        orderId,
                        totalPrice,
                        paymentMethod,
                        orderDate,
                        address,
                        shipmentCode,
                        status
                );

                orders.add(order);
            }

            this.outputParam = orders;

        } catch (Exception e) {
            LOGGER.error("Error retrieving all orders: {}", e.getMessage());
        }
    }
}