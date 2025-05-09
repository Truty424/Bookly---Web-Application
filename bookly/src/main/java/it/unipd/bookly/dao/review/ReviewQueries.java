package it.unipd.bookly.dao.review;

public final class ReviewQueries {

    private ReviewQueries() {
    }

    // --- CREATE ---
    public static final String INSERT_REVIEW
            = "INSERT INTO booklySchema.reviews (user_id, book_id, comment, rating, number_of_likes, number_of_dislikes, parent_review_id) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    // --- READ ---
    public static final String GET_REVIEW_BY_ID
            = "SELECT * FROM booklySchema.reviews WHERE review_id = ?";

    public static final String GET_REVIEWS_BY_BOOK
            = "SELECT r.review_id, r.user_id, r.book_id, r.rating, r.comment, r.review_date, "
            + "r.number_of_likes, r.number_of_dislikes, u.username "
            + "FROM booklySchema.reviews r "
            + "JOIN booklySchema.users u ON r.user_id = u.user_id "
            + "WHERE r.book_id = ? "
            + "ORDER BY r.review_date DESC";

    public static final String GET_REVIEWS_BY_USER
            = "SELECT r.*, b.title, b.language "
            + "FROM booklySchema.reviews r "
            + "JOIN booklySchema.books b ON r.book_id = b.book_id "
            + "WHERE r.user_id = ? ORDER BY r.review_date DESC";

    public static final String GET_AVG_RATING_FOR_BOOK
            = "SELECT ROUND(AVG(rating), 2) AS avg_rating FROM booklySchema.reviews WHERE book_id = ?";

    public static final String COUNT_REVIEWS_FOR_BOOK
            = "SELECT COUNT(*) FROM booklySchema.reviews WHERE book_id = ?";

    // --- UPDATE ---
    public static final String UPDATE_COMMENT_AND_RATING
            = "UPDATE booklySchema.reviews SET comment = ?, rating = ?, review_date = CURRENT_TIMESTAMP "
            + "WHERE review_id = ?";

    public static final String UPDATE_REVIEW_LIKES
            = "UPDATE booklySchema.reviews SET number_of_likes = ? WHERE review_id = ?";
    public static final String UPDATE_REVIEW_DISLIKES
            = "UPDATE booklySchema.reviews SET number_of_dislikes = ? WHERE review_id = ?";
}
