<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Books by Publisher</title>
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
    <main class="container py-4">
      <div class="d-flex justify-content-between align-items-center py-2">
        <h2>Books by <c:out value="${publisher_name}" /></h2>
        <a href="${pageContext.request.contextPath}/publisher" class="btn back-btn">
          <i class="fas fa-arrow-left"></i> Back to All Publishers
        </a>
      </div>
      <c:choose>
        <c:when test="${not empty publisher_books}">
          <ul class="book-list">
            <c:forEach var="book" items="${publisher_books}">
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
                  <p><strong>Publisher:</strong> ${publisher_name}</p>
                  <p><strong>Price:</strong> $${book.price}</p>
                  <p><strong>Language:</strong> ${book.language}</p>
                  <p><strong>Average Rating:</strong> ${book.average_rate}/5</p>
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
          <p class="no-results">No books found for this publisher.</p>
        </c:otherwise>
      </c:choose>
    </main>
    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/format-number.js"></script>
  </body>
</html>
