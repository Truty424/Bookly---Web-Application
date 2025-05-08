<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>Book Details</title>
  <%@ include file="/html/cdn.html" %>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/bookDetails.css" />
</head>

<body>
  <%@ include file="/html/header.html" %>

  <div class="book-details-container">
    <div class="back-button-wrapper">
      <a href="${pageContext.request.contextPath}/book" class="btn back-button">
        <i class="fas fa-arrow-left"></i> Back to All Books
      </a>
    </div>

    <c:choose>
      <c:when test="${not empty book_details}">
        <!-- Book Information -->
        <div class="book-card-horizontal">
          <div class="book-image">
            <img
             src="${pageContext.request.contextPath}/load-book-img?bookId=${book_details.bookId}"
             alt="Cover of ${book_details.title}"
             onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/static/img/book/default.jpg';"
             />
          </div>
          <div class="book-info">
            <h2 class="book-title">${book_details.title}</h2>
            <p><strong>Author(s):</strong>
              <c:choose>
                <c:when test="${not empty authors}">
                  <c:forEach var="author" items="${authors}" varStatus="loop">
                    ${author.name}<c:if test="${!loop.last}">, </c:if>
                  </c:forEach>
                </c:when>
                <c:otherwise>Unknown</c:otherwise>
              </c:choose>
            </p>
            <p><strong>ISBN:</strong> ${book_details.isbn}</p>
            <p><strong>Language:</strong> ${book_details.language}</p>
            <p><strong>Price:</strong> €${book_details.price}</p>
            <p><strong>Average Rating:</strong> ${average_rating} / 5</p>
            <p><strong>Total Reviews:</strong> ${review_count}</p>

            <div class="book-actions">
              <form action="${pageContext.request.contextPath}/cart/add/${book_details.bookId}" method="post">
                <button class="btn btn-cart" type="submit">
                  <i class="fas fa-shopping-cart"></i> Add to Cart
                </button>
              </form>

              <c:choose>
                <c:when test="${isInWishlist}">
                  <form action="${pageContext.request.contextPath}/wishlist" method="post">
                    <input type="hidden" name="action" value="remove" />
                    <input type="hidden" name="book_id" value="${book_details.bookId}" />
                    <button class="btn btn-wishlist" type="submit">
                      <i class="fas fa-heart"></i>
                    </button>
                  </form>
                </c:when>
                <c:otherwise>
                  <form action="${pageContext.request.contextPath}/wishlist" method="post">
                    <input type="hidden" name="action" value="add" />
                    <input type="hidden" name="book_id" value="${book_details.bookId}" />
                    <button class="btn btn-wishlist" type="submit">
                      <i class="far fa-heart"></i>
                    </button>
                  </form>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </div>

        <!-- Reviews Section -->
        <div class="review-section">
          <h2>Reviews</h2>
          <c:choose>
            <c:when test="${not empty reviews}">
              <ul class="review-list">
                <c:forEach var="review" items="${reviews}">
                  <li class="review-item">
                    <p><em>By Username:</em> ${review.username}</p>
                    <p><strong>Rating:</strong> ${review.rating} / 5</p>
                    <p><strong>Comment:</strong> ${review.reviewText}</p>
                    <p><em>Date:</em> ${review.reviewDate}</p>
                    <div class="review-actions">
                      <form action="${pageContext.request.contextPath}/review/like" method="post" style="display: inline">
                        <input type="hidden" name="reviewId" value="${review.reviewId}" />
                        <button type="submit" class="btn-like" title="Like">👍 (${review.numberOfLikes})</button>
                      </form>
                      <form action="${pageContext.request.contextPath}/review/dislike" method="post" style="display: inline">
                        <input type="hidden" name="reviewId" value="${review.reviewId}" />
                        <button type="submit" class="btn-dislike" title="Dislike">👎 (${review.numberOfDislikes})</button>
                      </form>
                    </div>
                    <hr />
                  </li>
                </c:forEach>
              </ul>
            </c:when>
            <c:otherwise>
              <p>No reviews available for this book.</p>
            </c:otherwise>
          </c:choose>

          <!-- Add Review Form -->
          <div class="add-review-section">
            <h3>Add Your Review</h3>
            <form action="${pageContext.request.contextPath}/review/submit" method="post" class="modern-review-form">
              <input type="hidden" name="bookId" value="${book_details.bookId}" />

              <!-- Rating Stars -->
              <div class="rating-stars">
                <label>Select Rating:</label>
                <div class="stars">
                  <c:forEach begin="1" end="5" var="i">
                    <input type="radio" name="rating" id="star${i}" value="${i}" required />
                    <label for="star${i}" title="${i} stars">★</label>
                  </c:forEach>
                </div>
              </div>

              <!-- Comment Text -->
              <div class="form-group">
                <textarea name="reviewText" placeholder="Leave your comment here..." rows="4" required></textarea>
              </div>

              <!-- Submit Button -->
              <div class="form-actions d-flex justify-content-center align-items-center my-4">
                <button type="submit" class="submit-btn">
                  <i class="fas fa-paper-plane"></i> Submit
                </button>
              </div>
            </form>
          </div>
        </div>
      </c:when>

      <c:otherwise>
        <p>Book details not found.</p>
      </c:otherwise>
    </c:choose>
  </div>

  <script src="${pageContext.request.contextPath}/static/js/format-number.js"></script>
  <%@ include file="/html/footer.html" %>
</body>
</html>
