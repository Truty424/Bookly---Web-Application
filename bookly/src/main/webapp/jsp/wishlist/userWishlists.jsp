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
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <div class="container">
      <div class="d-flex">
        <aside class="sidebar">
          <div class="sidebar-content">
            <ul class="nav-top">
              <li>
                <a href="${pageContext.request.contextPath}/user/profile"
                  ><i class="fas fa-tachometer-alt"></i> Dashboard</a
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
                  ><i class="fas fa-heart"></i> Wishlist</a
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
          <h2>Your Wishlists</h2>

          <c:choose>
            <c:when test="${not empty user_wishlists}">
              <ul class="wishlist-list">
                <c:forEach var="wishlist" items="${user_wishlists}">
                  <li class="wishlist-item">
                    <a
                      href="${pageContext.request.contextPath}/wishlist/${wishlist.wishlistId}"
                    >
                      Wishlist #${wishlist.wishlistId} — Created:
                      ${wishlist.createdAt}
                    </a>
                  </li>
                </c:forEach>
              </ul>
            </c:when>
            <c:otherwise>
              <p>No wishlists found.</p>
            </c:otherwise>
          </c:choose>
        </main>
      </div>
    </div>

    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
