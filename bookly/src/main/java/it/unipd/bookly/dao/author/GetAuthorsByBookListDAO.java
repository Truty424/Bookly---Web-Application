package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.author.AuthorQueries.GET_AUTHORS_BY_BOOK_LIST;

/**
 * DAO to retrieve authors for a list of book IDs in a single query.
 * Returns a map of bookId -> list of associated authors.
 */
public class GetAuthorsByBookListDAO extends AbstractDAO<Map<Integer, List<Author>>> {

    private final List<Integer> bookIds;

    public GetAuthorsByBookListDAO(Connection con, List<Integer> bookIds) {
        super(con);
        this.bookIds = bookIds;
    }

    @Override
    protected void doAccess() throws Exception {
        Map<Integer, List<Author>> result = new HashMap<>();

        if (bookIds == null || bookIds.isEmpty()) {
            this.outputParam = result;
            return;
        }

        // Generate placeholders (?, ?, ?, ...)
        String placeholders = String.join(",", Collections.nCopies(bookIds.size(), "?"));
        String query = String.format(GET_AUTHORS_BY_BOOK_LIST, placeholders);

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            for (int i = 0; i < bookIds.size(); i++) {
                stmt.setInt(i + 1, bookIds.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    Author author = new Author(
                            rs.getInt("author_id"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("biography"),
                            rs.getString("nationality")
                    );

                    result.computeIfAbsent(bookId, k -> new ArrayList<>()).add(author);
                }
            }
        }

        this.outputParam = result;
    }
}
