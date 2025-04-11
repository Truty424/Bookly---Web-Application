package it.unipd.bookly.dao.book;

public final class BookQueries {

    private BookQueries() {
    }

    // --- CREATE ---
    public static final String INSERT_BOOK
            = "INSERT INTO booklySchema.books "
            + "(title, language, isbn, price, edition, publication_year, number_of_pages, stock_quantity, average_rate, summary) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String INSERT_BOOK_IMAGE = "INSERT INTO booklySchema.book_images (title, book_pic, book_pic_type) VALUES (?, ?, ?)";

    // --- READ ---
    public static final String GET_BOOK_BY_ID
            = "SELECT * FROM booklySchema.books WHERE book_id = ?";

    public static final String GET_ALL_BOOKS_WITH_AUTHORS
            = "SELECT * FROM booklySchema.books";

    public static final String SEARCH_BOOK_BY_TITLE
            = "SELECT * FROM booklySchema.books WHERE LOWER(title) LIKE LOWER(?)";

    public static final String GET_BOOKS_BY_CATEGORY_ID
            = "SELECT b.* FROM booklySchema.books b "
            + "JOIN booklySchema.category_belongs cb ON b.book_id = cb.book_id "
            + "WHERE cb.category_id = ?";

    public static final String GET_BOOKS_BY_AUTHOR_ID
            = "SELECT b.* FROM booklySchema.books b "
            + "JOIN booklySchema.writes w ON b.book_id = w.book_id "
            + "WHERE w.author_id = ?";

    public static final String GET_BOOKS_BY_PUBLISHER_ID
            = "SELECT b.* FROM booklySchema.books b "
            + "JOIN booklySchema.published_by pb ON b.book_id = pb.book_id "
            + "WHERE pb.publisher_id = ?";

    public static final String GET_TOP_RATED_BOOKS
            = "SELECT * FROM booklySchema.books WHERE average_rate >= ? ORDER BY average_rate DESC";

    // --- UPDATE ---
    public static final String UPDATE_BOOK
            = "UPDATE booklySchema.books SET "
            + "title = ?, language = ?, isbn = ?, price = ?, edition = ?, publication_year = ?, "
            + "number_of_pages = ?, stock_quantity = ?, average_rate = ?, summary = ? "
            + "WHERE book_id = ?";

    public static final String UPDATE_BOOK_STOCK
            = "UPDATE booklySchema.books SET stock_quantity = ? WHERE book_id = ?";

    // --- DELETE ---
    public static final String DELETE_BOOK
            = "DELETE FROM booklySchema.books WHERE book_id = ?";
}
