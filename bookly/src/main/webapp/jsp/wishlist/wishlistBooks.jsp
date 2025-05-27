<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
    <title>Your Wishlists</title>
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
      href="${pageContext.request.contextPath}/static/css/pages/wishlist.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/userDashboard.css"
      type="text/css"
    />
    <%@ include file="/html/cdn.html" %>
  </head>

  <body>
    <div>
      <button class="sidebar-toggle" onclick="toggleSidebar()">☰ Menu</button>
      <div
        class="sidebar-overlay"
        id="sidebarOverlay"
        onclick="toggleSidebar()"
      ></div>
      <div class="d-flex">
        <%@ include file="/html/userSidebar.html" %>
        <c:choose>
          <c:when test="${not empty wishlist_books}">
            <div
              style="
                max-width: 900px;
                margin: 40px auto;
                padding: 30px;
                background-color: #fff;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
              "
            >
              <c:forEach var="book" items="${wishlist_books}">
                <div class="col">
                  <div class="card h-100 shadow-sm p-3">
                    <img
                    src="${pageContext.request.contextPath}/static/img/book/${book.bookId}.jpg"
                    class="card-img-top mb-2" alt=test"
                    onerror="this.onerror=null;
                    this.src='${pageContext.request.contextPath}/static/img/book/default.jpg';"
                    />

                    <div class="card-body">
                      <h5 class="card-title">${book.title}</h5>
                      <p class="card-text text-muted">
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

                      <form
                        action="${pageContext.request.contextPath}/wishlist"
                        method="post"
                        class="d-inline"
                      >
                        <input type="hidden" name="action" value="remove" />
                        <input
                          type="hidden"
                          name="book_id"
                          value="${book.bookId}"
                        />
                        <input
                          type="hidden"
                          name="book_id_redirect"
                          value="${book.bookId}"
                        />
                        <button
                          class="btn btn-outline-danger btn-sm"
                          type="submit"
                          title="Remove from Wishlist"
                        >
                          <i class="fas fa-trash-alt"></i> Remove
                        </button>
                      </form>
                    </div>
                  </div>
                </div>
              </c:forEach>
            </div>
          </c:when>

          <c:otherwise>
            <p class="text-muted">Your wishlist is currently empty.</p>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
    <div class="container py-5"></div>

    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
