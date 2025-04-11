package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.book.BookQueries.DELETE_BOOK;

public class DeleteBookDAO extends AbstractDAO<Boolean> {

    private final int book_id;

    public DeleteBookDAO(final Connection con, final int book_id) {
        super(con);
        this.book_id = book_id;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement ps = con.prepareStatement(DELETE_BOOK)) {
            ps.setInt(1, book_id);
            int affectedRows = ps.executeUpdate();
            this.outputParam = affectedRows > 0;
        } catch (Exception e) {
            LOGGER.error("Error deleting book {}: {}", book_id, e.getMessage());
        }
    }
}
