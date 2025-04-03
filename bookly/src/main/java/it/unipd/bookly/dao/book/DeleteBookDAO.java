package it.unipd.bookly.dao.book;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.book.BookQueries.DELETE_BOOK;

public class DeleteBookDAO extends AbstractDAO<Boolean> {

    private final int bookId;

    public DeleteBookDAO(final Connection con, final int bookId) {
        super(con);
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement ps = con.prepareStatement(DELETE_BOOK)) {
            ps.setInt(1, bookId);
            int affectedRows = ps.executeUpdate();
            this.outputParam = affectedRows > 0;
        } catch (Exception e) {
            LOGGER.error("Error deleting book {}: {}", bookId, e.getMessage());
        }
    }
}
