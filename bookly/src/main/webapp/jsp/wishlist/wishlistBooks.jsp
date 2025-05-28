<%@ page contentType="text/html;charset=UTF-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Your Wishlist</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <%@ include file="/html/cdn.html" %>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/wishlist.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/userDashboard.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/allBooks.css" />
  </head>

  <body>
    <div>
      <button class="sidebar-toggle" onclick="toggleSidebar()">☰ Menu</button>
      <div class="sidebar-overlay" id="sidebarOverlay" onclick="toggleSidebar()"></div>
      <div class="d-flex">
        <%@ include file="/html/userSidebar.html" %>

        <main class="container py-5" style="flex: 1;">
          <h2 class="all-books-title">Your Wishlist</h2>

          <c:choose>
            <c:when test="${not empty wishlist_books}">
              <div class="books-grid">
                <c:forEach var="book" items="${wishlist_books}">
                  <div class="book-card">
                    <a href="${pageContext.request.contextPath}/book/${book.bookId}" class="book-title">
                      <img
                        src="${pageContext.request.contextPath}/static/img/book/${book.bookId}.jpg"
                        alt="${book.title}"
                        class="book-image"
                        onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/static/img/book/default.jpg';"
                      />
                      <h3 class="book-title">${book.title}</h3>
                    </a>

                    <p class="text-muted" style="font-size: 13px; margin: 5px 0 12px;">
                      <strong>Author(s):</strong>
                      <c:choose>
                        <c:when test="${not empty wishlist_authors[book.bookId]}">
                          <c:forEach var="author" items="${wishlist_authors[book.bookId]}" varStatus="loop">
                            ${author.name}<c:if test="${!loop.last}">, </c:if>
                          </c:forEach>
                        </c:when>
                        <c:otherwise>Unknown</c:otherwise>
                      </c:choose>
                    </p>

                    <p style="font-weight: bold; color: #FC8C59; margin-bottom: 8px;">
                      €${book.price}
                    </p>


                    <form
                      action="${pageContext.request.contextPath}/wishlist"
                      method="post"
                      class="d-inline"
                      style="margin-top: 12px;"
                    >
                      <input type="hidden" name="action" value="remove" />
                      <input type="hidden" name="book_id" value="${book.bookId}" />
                      <input type="hidden" name="redirect_to" value="${pageContext.request.contextPath}/wishlist" />
                      <button class="btn btn-outline-danger btn-sm" type="submit">
                        <i class="fas fa-trash-alt"></i> Remove
                      </button>
                    </form>
                  </div>
                </c:forEach>
              </div>
            </c:when>

            <c:otherwise>
              <p class="text-muted text-center">Your wishlist is currently empty.</p>
            </c:otherwise>
          </c:choose>
        </main>
      </div>
    </div>

    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
