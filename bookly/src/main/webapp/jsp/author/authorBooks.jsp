<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Books by Author</title>
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
    <main class="container">
      <h1>Books by <c:out value="${author}" /></h1>

      <c:choose>
        <c:when test="${not empty author_books}">
          <ul class="book-list">
            <c:forEach var="book" items="${author_books}">
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
                  <p><strong>Publisher:</strong> ${book.edition}</p>
                  <p><strong>Price:</strong> $${book.price}</p>
                  <p><strong>Language:</strong> ${book.language}</p>
                  <p><strong>Average Rating:</strong> ${book.average_rate}/5</p>
                  <form
                    action="${pageContext.request.contextPath}/book/${book.bookId}"
                    method="get"
                  >
                    <button type="submit" class="btn btn-primary">
                      View Details
                    </button>
                  </form>
                </div>
              </li>
            </c:forEach>
          </ul>
        </c:when>
        <c:otherwise>
          <p class="no-results">No books found by this author.</p>
        </c:otherwise>
      </c:choose>

      <div class="home-button-wrapper">
        <a
          href="${pageContext.request.contextPath}/author"
          class="btn btn-primary"
        >
          <i class="fas fa-arrow-left"></i> Back to All Authors
        </a>
      </div>
    </main>
    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
