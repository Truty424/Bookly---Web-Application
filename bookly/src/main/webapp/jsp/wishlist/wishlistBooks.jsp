<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ page
contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
    <title>My Wishlist</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
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
      href="${pageContext.request.contextPath}/static/css/pages/userDashboard.css"
      type="text/css"
    />
  </head>

  <body>
    <div class="container">
      <h2>My Wishlist</h2>
      <c:choose>
        <c:when test="${not empty wishlist_books}">
          <ul class="book-list">
            <c:forEach var="book" items="${wishlist_books}">
              <li class="book-item">
                <div class="book-details">
                  <a
                    class="book-title"
                    href="${pageContext.request.contextPath}/book?id=${book.bookId}"
                  >
                    ${book.title}
                  </a>
                  <p>Price: €${book.price}</p>
                </div>
                <form
                  action="${pageContext.request.contextPath}/wishlist/remove/${book.bookId}"
                  method="post"
                >
                  <button type="submit" class="remove-btn">
                    <i class="fas fa-trash-alt"></i> Remove
                  </button>
                </form>
              </li>
            </c:forEach>
          </ul>
        </c:when>
        <c:otherwise>
          <p>Your wishlist is currently empty.</p>
        </c:otherwise>
      </c:choose>
      <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
      <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
    </div>
  </body>
</html>
