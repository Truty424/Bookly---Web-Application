<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <div class="container-fluid p-0">
      <button class="sidebar-toggle" onclick="toggleSidebar()">☰ Menu</button>
      <div class="sidebar-overlay" id="sidebarOverlay" onclick="toggleSidebar()"></div>

      <div class="d-flex">
        <%@ include file="/html/userSidebar.html" %>

        <main class="profile-content">
          <h2>Your Wishlists</h2>

          <c:choose>
            <c:when test="${not empty user_wishlists}">
              <ul class="wishlist-list">
                <c:forEach var="wishlist" items="${user_wishlists}">
                  <li class="wishlist-item">
                    <a href="${pageContext.request.contextPath}/wishlist/${wishlist.wishlistId}">
                      Wishlist #${wishlist.wishlistId} — Created: ${wishlist.createdAt}
                    </a>
                  </li>
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

    <%@ include file="/html/footer.html" %>

    <script>
      function toggleSidebar() {
        const sidebar = document.getElementById("userSidebar");
        const overlay = document.getElementById("sidebarOverlay");
        sidebar.classList.toggle("active");
        overlay.classList.toggle("active");
      }

      // Auto-close on mobile
      document.querySelectorAll("#userSidebar a").forEach(link => {
        link.addEventListener("click", () => {
          if (window.innerWidth <= 768) {
            document.getElementById("userSidebar").classList.remove("active");
            document.getElementById("sidebarOverlay").classList.remove("active");
          }
        });
      });
    </script>
  </body>
</html>
