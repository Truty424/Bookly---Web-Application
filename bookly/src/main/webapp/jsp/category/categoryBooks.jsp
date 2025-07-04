<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Books in Category</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
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
      href="${pageContext.request.contextPath}/static/css/pages/listBooks.css"
    />
  </head>
  <body>
    <%@ include file="/html/header.html" %>
    <main class="container my-2">
      <div class="book-wrapper">
        <h2>Books in <c:out value="${category_name}" /></h2>
        <div class="home-button-wrapper">
          <a
            href="${pageContext.request.contextPath}/category"
            class="btn back-btn"
          >
            <i class="fas fa-arrow-left"></i> Back to All Categories
          </a>
        </div>
      </div>
      <c:choose>
        <c:when test="${not empty category_books}">
          <ul class="book-list">
            <c:forEach var="book" items="${category_books}">
              <li class="book-item">
                <div class="book-image">
                  <img
                    src="${pageContext.request.contextPath}/static/img/book/${book.bookId}.jpg"
                    alt="Cover of ${book.title}"
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/static/img/book/default.jpg';"
                  />
                </div>
                <div class="book-details">
                  <h3>
                    <a
                      href="${pageContext.request.contextPath}/book/${book.bookId}"
                      >${book.title}</a
                    >
                  </h3>
                  <p><strong>Price:</strong> <span data-format='price'> ${book.price}</span> </p>
                  <p><strong>Language:</strong> ${book.language}</p>
                  <p data-format="rating">
                    <strong>Average Rating:</strong>
                    <c:choose>
                      <c:when test="${book_ratings[book.bookId] != null}">
                        ${book_ratings[book.bookId]} / 5
                      </c:when>
                      <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                  </p>
                  <form
                    action="${pageContext.request.contextPath}/book/${book.bookId}"
                    method="get"
                  >
                    <button type="submit" class="btn btn-cart">
                      View Details
                    </button>
                  </form>
                </div>
              </li>
            </c:forEach>
          </ul>
        </c:when>
        <c:otherwise>
          <p class="no-results">No books found in this category.</p>
        </c:otherwise>
      </c:choose>
    </main>
    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/format-number.js"></script>
  </body>
</html>
