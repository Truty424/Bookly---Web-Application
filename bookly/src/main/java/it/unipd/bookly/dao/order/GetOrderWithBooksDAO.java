package it.unipd.bookly.dao.order;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.order.OrderQueries.GET_ORDER_WITH_BOOKS;

/**
 * DAO class to retrieve an order with its associated books.
 */
public class GetOrderWithBooksDAO extends AbstractDAO<Order> {

    private final int orderId;

    public GetOrderWithBooksDAO(final Connection con, final int orderId) {
        super(con);
        this.orderId = orderId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();
        Order order = null;

        try (PreparedStatement stmnt = con.prepareStatement(GET_ORDER_WITH_BOOKS)) {
            stmnt.setInt(1, orderId);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    if (order == null) {
                        order = new Order();
                        order.setOrderId(rs.getInt("order_id"));
                        order.setPaymentMethod(rs.getString("payment_method"));
                        order.setTotalPrice(rs.getDouble("total_price"));
                        order.setStatus(rs.getString("status"));
                        order.setAddress(rs.getString("address"));
                        order.setOrderDate(rs.getTimestamp("order_date"));
                        order.setShipmentCode(rs.getString("shipment_code"));
                    }

                    Book book = new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("language"),
                            rs.getString("isbn"),
                            rs.getDouble("price"),
                            rs.getString("edition"),
                            rs.getInt("publication_year"),
                            rs.getInt("number_of_pages"),
                            rs.getInt("stock_quantity"),
                            rs.getDouble("average_rate"),
                            rs.getString("summary")
                    );
                    books.add(book);
                }
            }

            if (order != null) {
                order.setBooks(books);
            }

            this.outputParam = order;

            LOGGER.info("Order ID {} loaded with {} book(s).", orderId, books.size());
        } catch (Exception ex) {
            this.outputParam = null;
            LOGGER.error("Error retrieving order with ID {}: {}", orderId, ex.getMessage(), ex);
            throw ex;
        }
    }
}
