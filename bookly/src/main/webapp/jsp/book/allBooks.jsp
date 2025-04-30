<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
    <title>All Books</title>
    <meta charset="UTF-8" />
    <%@ include file="/html/cdn.html" %>
    <link
      rel="stylesheet"
      href="../../static/css/base/root.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="../../static/css/base/globals.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="../../static/css/pages/allBooks.css"
      type="text/css"
    />
  </head>
  <body>
    <%@ include file="/html/header.html" %>

    <div class="container py-5">
      <div class="books-grid">
        <c:choose>
          <c:when test="${not empty all_books}">
            <c:forEach var="book" items="${all_books}">
              <a
                href="${pageContext.request.contextPath}/book/${book.bookId}"
                class="book-title"
              >
                <div class="book-card">
                  <img
                    src="${pageContext.request.contextPath}/load-book-img?bookId=${book.bookId}"
                    alt="${book.title}"
                    class="book-image"
                  />
                  <h3 class="book-title">${book.title}</h3>
                </div>
              </a>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <p class="no-books-message">No books found.</p>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <%@ include file="/html/footer.html" %>
  </body>
</html>
