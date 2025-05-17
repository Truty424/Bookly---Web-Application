<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Edit User Profile</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/userDashboard.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/editUserProfile.css" />
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <div class="container-fluid p-0">
      <button class="sidebar-toggle" onclick="toggleSidebar()">☰ Menu</button>
      <div class="sidebar-overlay" id="sidebarOverlay" onclick="toggleSidebar()"></div>
      <div class="d-flex">
        <%@ include file="/html/userSidebar.html" %>

        <main class="profile-content">
          <h2>Edit Your Profile</h2>
          <form action="${pageContext.request.contextPath}/user/editUserProfile" method="post">
            <div class="form-group">
              <label for="firstname">First Name:</label>
              <input type="text" id="firstname" name="firstName" value="${user.firstName}" required />
            </div>

            <div class="form-group">
              <label for="lastname">Last Name:</label>
              <input type="text" id="lastname" name="lastName" value="${user.lastName}" required />
            </div>

            <div class="form-group">
              <label for="username">Username:</label>
              <input type="text" id="username" name="username" value="${user.username}" required />
            </div>

            <div class="form-group">
              <label for="email">Email:</label>
              <input type="email" id="email" name="email" value="${user.email}" required />
            </div>

            <div class="form-group">
              <label for="phone">Phone:</label>
              <input type="tel" id="phone" name="phone" value="${user.phone}" />
            </div>

            <div class="form-group">
              <label for="address">Address:</label>
              <input type="text" id="address" name="address" value="${user.address}" />
            </div>

            <button type="submit" class="save-button">Save The Changes</button>
          </form>
        </main>
      </div>
    </div>

    <script>
      function toggleSidebar() {
        const sidebar = document.getElementById("userSidebar");
        const overlay = document.getElementById("sidebarOverlay");

        sidebar.classList.toggle("active");
        overlay.classList.toggle("active");
      }

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
