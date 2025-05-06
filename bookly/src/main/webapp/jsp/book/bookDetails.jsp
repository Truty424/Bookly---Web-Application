<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
    <title>Book Details</title>
    <%@ include file="/html/cdn.html" %>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/root.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/globals.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/bookDetails.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
  </head>
  <body>
    <%@ include file="/html/header.html" %>

    <div class="book-details-container">
      <div class="back-button-wrapper">
        <a
          href="${pageContext.request.contextPath}/book"
          class="btn btn-secondary back-button"
        >
          <i class="fas fa-arrow-left"></i> Back to All Books
        </a>
      </div>
      <c:choose>
        <c:when test="${not empty book_details}">
          <div class="book-card-horizontal">
            <div class="book-image">
              <img
                src="${pageContext.request.contextPath}/load-book-img?bookId=${book_details.bookId}"
                alt="Cover of ${book_details.title}"
              />
            </div>
            <div class="book-info">
              <h2 class="book-title">${book_details.title}</h2>
              <p>
                <strong>Author(s):</strong>
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
              <p><strong>Price:</strong> €${book_details.price}</p>
              <p><strong>Language:</strong> ${book_details.language}</p>
              <div class="book-actions">
                <form
                  action="${pageContext.request.contextPath}/cart/add/${book_details.bookId}"
                  method="post"
                >
                  <button class="btn btn-cart" type="submit">
                    <i class="fas fa-shopping-cart"></i> Add to Cart
                  </button>
                </form>
                <c:if test="${isInWishlist}">
                    <form action="${pageContext.request.contextPath}/wishlist/remove/${book_details.bookId}" method="post">
                        <button class="btn btn-wishlist" type="submit">
                            <i class="fas fa-heart"></i>
                        </button>
                    </form>
                </c:if>
                <c:if test="${not isInWishlist}">
                    <form action="${pageContext.request.contextPath}/wishlist/add/${book_details.bookId}" method="post">
                        <button class="btn btn-wishlist" type="submit">
                            <i class="far fa-heart"></i> 
                        </button>
                    </form>
                </c:if>
              </div>
            </div>
          </div>

          <div class="review-section">
            <h2>Reviews</h2>
            <c:choose>
              <c:when test="${not empty reviews}">
                <ul class="review-list">
                  <c:forEach var="review" items="${reviews}">
                    <li class="review-item">
                      <p>
                        <strong>Rating:</strong> ${book_details.average_rate} /
                        5
                      </p>
                      <p><strong>Comment:</strong> ${review.reviewText}</p>
                      <p><em>By User ID:</em> ${review.userId}</p>
                      <p><em>Date:</em> ${review.reviewDate}</p>
                      <p>
                        👍 ${review.numberOfLikes} | 👎
                        ${review.numberOfDislikes}
                      </p>
                      <hr />
                    </li>
                  </c:forEach>
                </ul>
              </c:when>
              <c:otherwise>
                <p>No reviews available for this book.</p>
              </c:otherwise>
            </c:choose>
          </div>
        </c:when>
        <c:otherwise>
          <p>Book details not found.</p>
        </c:otherwise>
      </c:choose>
    </div>

    <%@ include file="/html/footer.html" %>
  </body>
</html>
