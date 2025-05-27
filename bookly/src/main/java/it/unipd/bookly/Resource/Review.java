package it.unipd.bookly.Resource;

import java.sql.Timestamp;

/**
 * Represents a review for a book.
 * A review includes details such as the review ID, user ID, book ID, review text,
 * rating, number of likes, number of dislikes, review date, and optional fields
 * for the username and book title.
 */
public class Review {

    private int reviewId;
    private int userId;
    private int book_id;
    private String comment;
    private int rating;
    private int numberOfLikes;
    private int numberOfDislikes;
    private Timestamp reviewDate;
    private Integer parentReviewId;

    // Optional fields for JOIN operations
    private String username;
    private String bookTitle;

    /**
     * Default constructor for Review.
     */
    public Review() {
        // Default constructor
    }

    /**
     * Constructs a Review with some attributes.
     *
     * @param reviewId        The ID of the review.
     * @param userId          The ID of the user who wrote the review.
     * @param book_id         The ID of the book being reviewed.
     * @param comment      The text of the review.
     * @param rating          The rating given to the book.
     * @param numberOfLikes   The number of likes the review has received.
     * @param numberOfDislikes The number of dislikes the review has received.
     * @param reviewDate      The date the review was written.
     */
    public Review(int reviewId, int userId, int book_id, String comment, int rating,
            int numberOfLikes, int numberOfDislikes, Timestamp reviewDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.book_id = book_id;
        this.comment = comment;
        this.rating = rating;
        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;
        this.reviewDate = reviewDate;
    }

    /**
     * Constructs a Review with all attributes.
     *
     * @param reviewId        The ID of the review.
     * @param userId          The ID of the user who wrote the review.
     * @param book_id         The ID of the book being reviewed.
     * @param comment      The text of the review.
     * @param rating          The rating given to the book.
     * @param numberOfLikes   The number of likes the review has received.
     * @param numberOfDislikes The number of dislikes the review has received.
     * @param reviewDate      The date the review was written.
     * @param parentReviewId  The id of parent's review.
     */
    public Review(int reviewId, int userId, int book_id, String comment, int rating,
                int numberOfLikes, int numberOfDislikes, Timestamp reviewDate, Integer parentReviewId) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.book_id = book_id;
        this.comment = comment;
        this.rating = rating;
        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;
        this.reviewDate = reviewDate;
        this.parentReviewId = parentReviewId;
    }
    /**
     * Constructs a Review with minimal attributes.
     *
     * @param userId     The ID of the user who wrote the review.
     * @param book_id    The ID of the book being reviewed.
     * @param comment The text of the review.
     * @param rating     The rating given to the book.
     */
    public Review(int userId, int book_id, String comment, int rating) {
        this.userId = userId;
        this.book_id = book_id;
        this.comment = comment;
        this.rating = rating;
    }

    /**
     * Gets the ID of the review.
     *
     * @return The review ID.
     */
    public int getReviewId() {
        return reviewId;
    }

    public Integer getParentReviewId() {
        return parentReviewId;
    }

    public void setParentReviewId(Integer parentReviewId) {
        this.parentReviewId = parentReviewId;
    }

    /**
     * Gets the ID of the user who wrote the review.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets the ID of the book being reviewed.
     *
     * @return The book ID.
     */
    public int getBookId() {
        return book_id;
    }

    /**
     * Gets the text of the review.
     *
     * @return The review text.
     */
    public String getReviewText() {
        return comment;
    }

    /**
     * Gets the rating given to the book.
     *
     * @return The rating.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Gets the number of likes the review has received.
     *
     * @return The number of likes.
     */
    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    /**
     * Gets the number of dislikes the review has received.
     *
     * @return The number of dislikes.
     */
    public int getNumberOfDislikes() {
        return numberOfDislikes;
    }

    /**
     * Gets the date the review was written.
     *
     * @return The review date.
     */
    public Timestamp getReviewDate() {
        return reviewDate;
    }

    /**
     * Gets the username of the user who wrote the review.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the title of the book being reviewed.
     *
     * @return The book title.
     */
    public String getBookTitle() {
        return bookTitle;
    }

    /**
     * Sets the ID of the review.
     *
     * @param reviewId The review ID.
     */
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    /**
     * Sets the ID of the user who wrote the review.
     *
     * @param userId The user ID.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Sets the ID of the book being reviewed.
     *
     * @param book_id The book ID.
     */
    public void setbookId(int book_id) {
        this.book_id = book_id;
    }

    /**
     * Sets the text of the review.
     *
     * @param comment The review text.
     */
    public void setReviewText(String comment) {
        this.comment = comment;
    }

    /**
     * Sets the rating given to the book.
     *
     * @param rating The rating.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Sets the number of likes the review has received.
     *
     * @param numberOfLikes The number of likes.
     */
    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    /**
     * Sets the number of dislikes the review has received.
     *
     * @param numberOfDislikes The number of dislikes.
     */
    public void setNumberOfDislikes(int numberOfDislikes) {
        this.numberOfDislikes = numberOfDislikes;
    }

    /**
     * Sets the date the review was written.
     *
     * @param reviewDate The review date.
     */
    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }

    /**
     * Sets the username of the user who wrote the review.
     *
     * @param username The username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the title of the book being reviewed.
     *
     * @param bookTitle The book title.
     */
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}
