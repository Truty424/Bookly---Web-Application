package it.unipd.bookly.dao.order;

import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.order.OrderQueries.GET_LATEST_ORDER_FOR_USER;

/**
 * DAO to retrieve the latest order for a specific user.
 */
public class GetLatestOrderForUserDAO extends AbstractDAO<Order> {

    private final int userId;

    public GetLatestOrderForUserDAO(Connection con, int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_LATEST_ORDER_FOR_USER)) {
            stmt.setInt(1, userId);

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