package it.unipd.bookly.dao.book;

/**
 * Contains SQL queries related to books in the Bookly system. This class
 * provides constants for creating, reading, updating, and deleting book records
 * in the database, as well as associating books with authors, categories, and
 * publishers.
 */
public final class BookQueries {

    /**
     * Private constructor to prevent instantiation.
     */
    private BookQueries() {
    }

    // --- CREATE ---
    /**
     * SQL query to insert a new book into the database.
     */
    public static final String INSERT_BOOK = """
        INSERT INTO booklySchema.books
        (title, language, isbn, price, edition, publication_year, number_of_pages, stock_quantity, average_rate, summary)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    /**
     * SQL query to insert a book image into the database.
     */
    public static final String INSERT_BOOK_IMAGE = """
        INSERT INTO booklySchema.book_image (book_id, image, image_type)
        VALUES (?, ?, ?)
        """;

    // --- READ ---
    /**
     * SQL query to retrieve a book by its ID, including image.
     */
    public static final String GET_BOOK_BY_ID = """
        SELECT b.*, i.image, i.image_type
        FROM booklySchema.books b
        LEFT JOIN booklySchema.book_image i ON b.book_id = i.book_id
        WHERE b.book_id = ?
        """;

    /**
     * SQL query to retrieve all books from the database, including images.
     */
    public static final String GET_ALL_BOOKS = """
        SELECT b.*, i.image, i.image_type
        FROM booklySchema.books b
        LEFT JOIN booklySchema.book_image i ON b.book_id = i.book_id
        """;

    /**
     * SQL query to search for books by their title, including images.
     */
    public static final String SEARCH_BOOK_BY_TITLE = """
        SELECT b.*, i.image, i.image_type
        FROM booklySchema.books b
        LEFT JOIN booklySchema.book_image i ON b.book_id = i.book_id
        WHERE LOWER(b.title) LIKE LOWER(?)
        """;

    /**
     * SQL query to retrieve books by a specific category ID, including images.
     */
    public static final String GET_BOOKS_BY_CATEGORY_ID = """
        SELECT b.*, i.image, i.image_type
        FROM booklySchema.books b
        JOIN booklySchema.category_belongs cb ON b.book_id = cb.book_id
        LEFT JOIN booklySchema.book_image i ON b.book_id = i.book_id
        WHERE cb.category_id = ?
        """;

    /**
     * SQL query to retrieve books by a specific author ID, including images.
     */
    public static final String GET_BOOKS_BY_AUTHOR_ID = """
        SELECT b.*, i.image, i.image_type
        FROM booklySchema.books b
        JOIN booklySchema.writes w ON b.book_id = w.book_id
        LEFT JOIN booklySchema.book_image i ON b.book_id = i.book_id
        WHERE w.author_id = ?
        """;

    /**
     * SQL query to retrieve books by a specific publisher ID, including images.
     */
    public static final String GET_BOOKS_BY_PUBLISHER_ID = """
        SELECT b.*, i.image, i.image_type
        FROM booklySchema.books b
        JOIN booklySchema.published_by pb ON b.book_id = pb.book_id
        LEFT JOIN booklySchema.book_image i ON b.book_id = i.book_id
        WHERE pb.publisher_id = ?
        """;

    /**
     * SQL query to retrieve top-rated books, including images.
     */
    public static final String GET_TOP_RATED_BOOKS = """
    SELECT b.book_id, b.title, b.language, b.isbn, b.price, b.edition,
           b.publication_year, b.number_of_pages, b.stock_quantity,
           b.summary,
           i.image, i.image_type,
           AVG(r.rating) AS average_rate
    FROM booklySchema.books b
    LEFT JOIN booklySchema.book_image i ON b.book_id = i.book_id
    JOIN booklySchema.reviews r ON b.book_id = r.book_id
    GROUP BY b.book_id, b.title, b.language, b.isbn, b.price, b.edition,
             b.publication_year, b.number_of_pages, b.stock_quantity,
             b.summary, i.image, i.image_type
    HAVING AVG(r.rating) >= ?
    ORDER BY AVG(r.rating) DESC
    LIMIT 10
    """;

    // --- UPDATE ---
    /**
     * SQL query to update a book's details in the database.
     */
    public static final String UPDATE_BOOK = """
        UPDATE booklySchema.books
        SET title = ?, language = ?, isbn = ?, price = ?, edition = ?, publication_year = ?, number_of_pages = ?, stock_quantity = ?, average_rate = ?, summary = ?
        WHERE book_id = ?
        """;

    /**
     * SQL query to update a book's stock quantity.
     */
    public static final String UPDATE_BOOK_STOCK = """
        UPDATE booklySchema.books
        SET stock_quantity = ?
        WHERE book_id = ?
        """;

    // --- DELETE ---
    /**
     * SQL query to delete a book from the database.
     */
    public static final String DELETE_BOOK = """
        DELETE FROM booklySchema.books
        WHERE book_id = ?
        """;

    /**
     * SQL query to delete a book image from the database.
     */
    public static final String DELETE_BOOK_IMAGE = """
        DELETE FROM booklySchema.book_image
        WHERE book_id = ?
        """;
}
