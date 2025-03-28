package it.unipd.bookly.dao.author;

public final class AuthorQueries {
    private AuthorQueries() {}

    // Create
    public static final String INSERT_AUTHOR =
        "INSERT INTO booklySchema.authors (first_name, last_name, biography, nationality) VALUES (?, ?, ?, ?)";

    // Read
    public static final String GET_AUTHOR_BY_ID =
        "SELECT * FROM booklySchema.authors WHERE author_id = ?";

    public static final String GET_ALL_AUTHORS =
        "SELECT * FROM booklySchema.authors";

    public static final String GET_AUTHORS_BY_BOOK =
        "SELECT a.* FROM booklySchema.authors a " +
        "JOIN booklySchema.writes w ON a.author_id = w.author_id " +
        "WHERE w.book_id = ?";

    public static final String ADD_AUTHOR_TO_BOOK =
    "INSERT INTO booklySchema.writes (book_id, author_id) VALUES (?, ?)";

    // Update
    public static final String UPDATE_AUTHOR =
        "UPDATE booklySchema.authors SET first_name = ?, last_name = ?, biography = ?, nationality = ? WHERE author_id = ?";

    // Delete
    public static final String DELETE_AUTHOR =
        "DELETE FROM booklySchema.authors WHERE author_id = ?";
}
