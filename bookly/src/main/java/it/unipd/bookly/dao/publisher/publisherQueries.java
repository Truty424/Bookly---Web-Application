package it.unipd.bookly.dao.publisher;

public final class PublisherQueries {

    private PublisherQueries() {}

    // --- CREATE ---
    public static final String INSERT_PUBLISHER =
            "INSERT INTO booklySchema.publishers (publisher_name, phone, address) VALUES (?, ?, ?)";

    public static final String ADD_PUBLISHER_TO_BOOK =
            "INSERT INTO booklySchema.published_by (book_id, publisher_id) VALUES (?, ?)";

    // --- READ ---
    public static final String GET_PUBLISHER_BY_ID =
            "SELECT * FROM booklySchema.publishers WHERE publisher_id = ?";

    public static final String GET_ALL_PUBLISHERS =
            "SELECT * FROM booklySchema.publishers ORDER BY publisher_name";

    public static final String GET_PUBLISHERS_BY_BOOK =
            "SELECT p.* FROM booklySchema.publishers p " +
            "JOIN booklySchema.published_by pb ON p.publisher_id = pb.publisher_id " +
            "WHERE pb.book_id = ?";

    public static final String GET_BOOKS_BY_PUBLISHER =
            "SELECT b.* FROM booklySchema.books b " +
            "JOIN booklySchema.published_by pb ON b.book_id = pb.book_id " +
            "WHERE pb.publisher_id = ? ORDER BY b.title";

    public static final String COUNT_BOOKS_BY_PUBLISHER =
            "SELECT COUNT(*) FROM booklySchema.published_by WHERE publisher_id = ?";

    // --- UPDATE ---
    public static final String UPDATE_PUBLISHER =
            "UPDATE booklySchema.publishers SET publisher_name = ?, phone = ?, address = ? WHERE publisher_id = ?";

    // --- DELETE ---
    public static final String DELETE_PUBLISHER =
            "DELETE FROM booklySchema.publishers WHERE publisher_id = ?";

    public static final String REMOVE_PUBLISHER_FROM_BOOK =
            "DELETE FROM booklySchema.published_by WHERE book_id = ? AND publisher_id = ?";
}
