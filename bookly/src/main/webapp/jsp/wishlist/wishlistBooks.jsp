<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
    <title>My Wishlist</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta charset="ISO-8859-1" />
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
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <div class="container-fluid p-0">
      <div class="d-flex">
        <aside class="sidebar">
          <div class="sidebar-content">
            <ul class="nav-top">
              <li>
                <a href="${pageContext.request.contextPath}/user/profile"
                  ><i class="fas fa-user"></i> My profile</a
                >
              </li>
              <li>
                <a
                  href="${pageContext.request.contextPath}/user/editUserProfile"
                  ><i class="fas fa-edit"></i> Edit Profile</a
                >
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/user/changePassword"
                  ><i class="fas fa-lock"></i> Edit Password</a
                >
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/wishlist"
                  ><i class="fas fa-heart"></i> My Wishlist</a
                >
              </li>
            </ul>

            <ul class="nav-bottom">
              <li>
                <a href="${pageContext.request.contextPath}/"
                  ><i class="fas fa-home"></i> Home Page</a
                >
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/logout"
                  ><i class="fas fa-sign-out-alt"></i> Logout</a
                >
              </li>
            </ul>
          </div>
        </aside>
        <main class="profile-content">
          <h2>My Wishlist</h2>

          <c:choose>
            <c:when test="${not empty wishlist_books}">
              <ul class="book-list">
                <c:forEach var="wishlist" items="${wishlist_books}">
                  <c:forEach var="book" items="${wishlist.books}">
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
                </c:forEach>
              </ul>
            </c:when>
            <c:otherwise>
              <p>Your wishlist is currently empty.</p>
            </c:otherwise>
          </c:choose>
        </main>
      </div>
    </div>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
