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
    <div class="container py-5">
      <h2>Your Wishlist</h2>
      <c:choose>
        <c:when test="${not empty wishlist_books}">
          <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
            <c:forEach var="book" items="${wishlist_books}">
              <div class="col">
                <div class="card h-100 shadow-sm">
                  <img
                    src="${pageContext.request.contextPath}/static/img/book/${book.bookId}.jpg"
                    class="card-img-top"
                    alt="${book.title}"
                    onerror="this.src='${pageContext.request.contextPath}/static/img/book/default.jpg';"
                  />
                  <div class="card-body">
                    <h5 class="card-title">${book.title}</h5>
                    <p class="card-text">
                      <strong>ISBN:</strong> ${book.isbn} <br />
                      <strong>Price:</strong> €${book.price}
                    </p>
                    <form
                      action="${pageContext.request.contextPath}/wishlist"
                      method="post"
                    >
                      <input type="hidden" name="action" value="remove" />
                      <input
                        type="hidden"
                        name="book_id"
                        value="${book.bookId}"
                      />
                      <button class="btn btn-danger btn-sm" type="submit">
                        Remove
                      </button>
                    </form>
                  </div>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:when>
        <c:otherwise>
          <p class="text-muted mt-4">Your wishlist is currently empty.</p>
        </c:otherwise>
      </c:choose>

      <div class="mt-4">
        <a
          href="${pageContext.request.contextPath}/book"
          class="btn btn-primary"
          >Browse Books</a
        >
      </div>
    </div>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
