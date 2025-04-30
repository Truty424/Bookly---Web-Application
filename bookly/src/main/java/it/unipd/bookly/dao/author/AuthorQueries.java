package it.unipd.bookly.dao.author;

/**
 * Contains SQL queries related to authors in the Bookly system.
 * This class provides constants for creating, reading, updating, and deleting
 * author records in the database, as well as associating authors with books.
 */
public final class AuthorQueries {

    /**
     * Private constructor to prevent instantiation.
     */
    private AuthorQueries() {}

    // Create

    /**
     * SQL query to insert a new author into the database.
     */
    public static final String INSERT_AUTHOR =
        "INSERT INTO booklySchema.authors (firstName, lastName, biography, nationality) VALUES (?, ?, ?, ?)";


    /**
     * SQL query to retrieve an author by their ID.
     */
    public static final String GET_AUTHOR_BY_ID =
        "SELECT * FROM booklySchema.authors WHERE author_id = ?";

    /**
     * SQL query to retrieve all authors from the database.
     */
    public static final String GET_ALL_AUTHORS =
        "SELECT * FROM booklySchema.authors";

    /**
     * SQL query to retrieve authors associated with a specific book.
     */
    public static final String GET_AUTHORS_BY_BOOK =
        "SELECT a.* FROM booklySchema.authors a " +
        "JOIN booklySchema.writes w ON a.author_id = w.author_id " +
        "WHERE w.book_id = ?";

    /**
     * SQL query to associate an author with a book.
     */
    public static final String ADD_AUTHOR_TO_BOOK =
        "INSERT INTO booklySchema.writes (book_id, author_id) VALUES (?, ?)";


    /**
     * SQL query to update an author's details in the database.
     */
    public static final String UPDATE_AUTHOR =
        "UPDATE booklySchema.authors SET firstName = ?, lastName = ?, biography = ?, nationality = ? WHERE author_id = ?";


    /**
     * SQL query to delete an author from the database.
     */
    public static final String DELETE_AUTHOR =
        "DELETE FROM booklySchema.authors WHERE author_id = ?";

    /**
     * SQL query to retrieve authors for a list of book IDs.
     */
    public static final String GET_AUTHORS_BY_BOOK_LIST =
            "SELECT w.book_id, a.* " +
                    "FROM booklySchema.authors a " +
                    "JOIN booklySchema.writes w ON a.author_id = w.author_id " +
                    "WHERE w.book_id IN (%s)";
}