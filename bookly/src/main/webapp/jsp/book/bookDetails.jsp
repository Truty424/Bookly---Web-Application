<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
    <title>Book Details</title>
    <%@ include file="/html/cdn.html" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" type="text/css" />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/bookDetails.css"
    />
  </head>
  <body>
    <%@ include file="/html/header.html" %>

    <div class="container">
      <c:choose>
        <c:when test="${not empty book_details}">
          <h2 class="book-subheading">${book_details.title}</h2>

          <p class="book-paragraph">
            <strong class="book-label">ISBN:</strong> ${book_details.isbn}
          </p>
          <p class="book-paragraph">
            <strong class="book-label">Price:</strong> €${book_details.price}
          </p>
          <p class="book-paragraph">
            <strong class="book-label">Language:</strong>
            ${book_details.language}
          </p>

          <p class="book-paragraph">
            <strong class="book-label">Author(s):</strong>
            <c:choose>
              <c:when test="${not empty authors}">
                <c:forEach var="author" items="${authors}" varStatus="loop">
                  ${author.name}<c:if test="${!loop.last}">, </c:if>
                </c:forEach>
              </c:when>
              <c:otherwise>Unknown</c:otherwise>
            </c:choose>
          </p>

          <form
            class="book-action-form"
            action="${pageContext.request.contextPath}/cart/add/${book_details.bookId}"
            method="post"
          >
            <button class="book-action-button" type="submit">Add to Cart</button>
          </form>

          <form
            action="${pageContext.request.contextPath}/wishlist/add/${book_details.bookId}"
            method="post"
            style="margin-top: 10px"
          >
            <button type="submit">Add to Wishlist</button>
          </form>
        </c:when>
        <c:otherwise>
          <p>Book details not found.</p>
        </c:otherwise>
      </c:choose>

      <h2>Reviews</h2>
      <c:choose>
        <c:when test="${not empty reviews}">
          <ul class="review-list">
            <c:forEach var="review" items="${reviews}">
              <li class="review-item">
                <p><strong>Rating:</strong> ${review.rating} / 5</p>
                <p><strong>Comment:</strong> ${review.reviewText}</p>
                <p><em>By User ID:</em> ${review.userId}</p>
                <p><em>Date:</em> ${review.reviewDate}</p>
                <p>
                  👍 ${review.numberOfLikes} | 👎 ${review.numberOfDislikes}
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

      <p>
        <a href="${pageContext.request.contextPath}/book"
          >← Back to All Books</a
        >
      </p>
    </div>

    <%@ include file="/html/footer.html" %>
  </body>
</html>
