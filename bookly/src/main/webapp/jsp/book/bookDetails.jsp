<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
  <title>Book Details</title>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <%@ include file="/html/cdn.html" %>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/bookDetails.css" />
</head>

<body>
<%@ include file="/html/header.html" %>

<main class="container">
  <div class="back-button-wrapper">
    <a href="${pageContext.request.contextPath}/book" class="btn back-btn">
      <i class="fas fa-arrow-left"></i> Back to All Books
    </a>
  </div>

  <c:choose>
    <c:when test="${not empty book_details}">
      <div class="book-details-card">
        <div class="book-card-horizontal">

          <!-- Book Cover -->
          <div class="book-image">
            <img src="${pageContext.request.contextPath}/static/img/book/${book_details.bookId}.jpg"
                 alt="Cover of ${book_details.title}"
                 onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/static/img/book/default.jpg';" />
          </div>

          <!-- Title -->
          <h1 class="book-title">${book_details.title}</h1>

          <!-- Book Meta Info -->
          <div class="book-main-info">
            <div class="book-meta-columns">
              <div class="column">
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
              </div>
              <div class="column">
                <p><strong>Edition:</strong> ${book_details.edition}</p>
                <p><strong>Publication Year:</strong> ${book_details.publication_year}</p>
                <p><strong>Pages:</strong> ${book_details.number_of_pages}</p>
              </div>
            </div>
          </div>

          <!-- Summary -->
          <div class="book-summary-container">
            <div class="book-summary">
              <p><strong>Summary:</strong></p>
              <p>${book_details.summary}</p>
            </div>
          </div>

          <!-- Ratings & Price -->
          <div class="book-extra-info">
            <div class="top-info-wrapper">
              <div class="top-info">
                <p><strong>Average Rating:</strong></p>
                <div class="review-star-rating">
                  <c:forEach begin="1" end="5" var="i">
                    <i class="<c:choose>
                                <c:when test='${i <= average_rating}'>fas fa-star</c:when>
                                <c:otherwise>far fa-star</c:otherwise>
                              </c:choose>"></i>
                  </c:forEach>
                  <span>(${average_rating} / 5)</span>
                </div>
                <p>Based on ${review_count} reviews</p>
                <p><strong>Stock:</strong> ${book_details.stockQuantity}</p>
                <p class="book-price"><strong>Price:</strong> €${book_details.price}</p>
              </div>
            </div>

            <!-- Actions -->
            <div class="book-actions-wrapper">
              <div class="book-actions">
                <!-- Add to Cart -->
                <form action="${pageContext.request.contextPath}/cart/add/${book_details.bookId}" method="post">
                  <button class="btn btn-cart" type="submit">
                    <i class="fas fa-shopping-cart"></i> Add to Cart
                  </button>
                </form>

           
                <form action="${pageContext.request.contextPath}/wishlist" method="post">
                  <input type="hidden" name="action" value="add" />
                  <input type="hidden" name="book_id" value="${book_details.bookId}" />
                  <input type="hidden" name="book_id_redirect" value="${book_details.bookId}" />
                  <button class="btn btn-wishlist" type="submit">
                    <i class="far fa-heart"></i>
                  </button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Reviews Section -->
      <div class="review-section">
        <div class="review-section-card">
          <h2>Reviews</h2>
          <c:choose>
            <c:when test="${not empty reviews}">
              <ul class="review-list">
                <c:forEach var="review" items="${reviews}">
                  <li class="review-item-modern">
                    <div class="review-avatar">
                      <img src="${pageContext.request.contextPath}/static/img/user/${review.userId}.jpg"
                           alt="User Avatar"
                           class="avatar"
                           onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/static/img/user/default.jpg';" />
                    </div>
                    <div class="review-main">
                      <div class="review-main-top">
                        <span class="review-username">${review.username}</span>
                        <span class="review-date">at ${review.reviewDate}</span>
                      </div>
                      <div class="review-main-bottom">
                        <p>${fn:escapeXml(review.reviewText)}</p>
                      </div>
                    </div>
                    <div class="review-meta">
                      <div class="review-meta-top">
                        <div class="review-star-rating">
                          <c:forEach begin="1" end="5" var="i">
                            <i class="<c:choose>
                                        <c:when test='${i <= review.rating}'>fas fa-star</c:when>
                                        <c:otherwise>far fa-star</c:otherwise>
                                     </c:choose>"></i>
                          </c:forEach>
                        </div>
                      </div>
                      <div class="review-meta-bottom">
                        <form action="${pageContext.request.contextPath}/review/like" method="post" class="inline-form">
                          <input type="hidden" name="reviewId" value="${review.reviewId}" />
                          <button type="submit" class="emoji-button" title="Like">👍 <span>${review.numberOfLikes}</span></button>
                        </form>
                        <form action="${pageContext.request.contextPath}/review/dislike" method="post" class="inline-form">
                          <input type="hidden" name="reviewId" value="${review.reviewId}" />
                          <button type="submit" class="emoji-button" title="Dislike">👎 <span>${review.numberOfDislikes}</span></button>
                        </form>
                      </div>
                    </div>
                  </li>
                </c:forEach>
              </ul>
            </c:when>
            <c:otherwise>
              <p>No reviews available for this book.</p>
            </c:otherwise>
          </c:choose>
        </div>

        <!-- Add Review Form -->
        <div class="add-review-section">
          <h3>Add Your Review</h3>
          <form action="${pageContext.request.contextPath}/review/submit" method="post" class="modern-review-form">
            <input type="hidden" name="bookId" value="${book_details.bookId}" />
            <label>Select Your Rating:</label>
            <div class="rating-stars">
              <div class="stars" style="direction: rtl;">
                <c:forEach begin="1" end="5" var="i">
                  <input type="radio" name="rating" id="star${i}" value="${6 - i}" required />
                  <label for="star${i}" title="${6 - i} stars">★</label>
                </c:forEach>
              </div>
            </div>
            <div class="form-group">
              <textarea name="reviewText" placeholder="Leave your comment here..." rows="4" required></textarea>
            </div>
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
</main>

<%@ include file="/html/footer.html" %>
<script src="${pageContext.request.contextPath}/static/js/format-number.js"></script>
<script src="${pageContext.request.contextPath}/static/js/main.js"></script>
<script src="${pageContext.request.contextPath}/static/js/header.js"></script>
</body>
</html>
