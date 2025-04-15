package it.unipd.bookly.Resource;

import java.sql.Timestamp;

public class Review {

    private int reviewId;
    private int userId;
    private int book_id;
    private String reviewText;
    private int rating;
    private int numberOfLikes;
    private int numberOfDislikes;
    private Timestamp reviewDate;

    // Optional fields for JOIN operations
    private String username;
    private String bookTitle;

    // Constructors
    public Review() {
        // Default constructor
    }

    public Review(int reviewId, int userId, int book_id, String reviewText, int rating,
            int numberOfLikes, int numberOfDislikes, Timestamp reviewDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.book_id = book_id;
        this.reviewText = reviewText;
        this.rating = rating;
        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;
        this.reviewDate = reviewDate;
    }

    public Review(int userId, int book_id, String reviewText, int rating) {
        this.userId = userId;
        this.book_id = book_id;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    // Getters
    public int getReviewId() {
        return reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return book_id;
    }

    public String getReviewText() {
        return reviewText;
    }

    public int getRating() {
        return rating;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public int getNumberOfDislikes() {
        return numberOfDislikes;
    }

    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public String getUsername() {
        return username;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    // Setters
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setbook_id(int book_id) {
        this.book_id = book_id;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public void setNumberOfDislikes(int numberOfDislikes) {
        this.numberOfDislikes = numberOfDislikes;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

}
