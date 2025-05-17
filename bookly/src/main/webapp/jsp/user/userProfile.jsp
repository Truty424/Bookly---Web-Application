<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- TODO: MERGE USER DASHBOARD WITH USER PROFILE PAGE (CSS) -->
<html>
  <head>
    <title>User Profile</title>
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
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/userProfile.css"
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
          <h1>My Profile :</h1>

          <c:if test="${not empty user}">
            <div class="profile-card">
              <!-- Profile Image Section -->
              <div class="profile-img-wrapper">
               <c:choose>
                <c:when test="${not empty user.userId}">
                 <img
                  src="${pageContext.request.contextPath}/static/img/user/${user.userId}.jpg"
                  alt="Profile Image"
                  class="profile-img"
                  onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/static/img/user/default.jpg';"
                 />
                </c:when>
               <c:otherwise>
               <div class="profile-img-placeholder">
                <i class="fas fa-user fa-2x"></i>
                 <span>Profile Image</span>
               </div>
               </c:otherwise>
               </c:choose>
              </div>

              <div>
                <p>
                  <i class="fas fa-user-circle"></i
                  ><strong> First Name : </strong> ${user.firstName}
                </p>
                <p>
                  <i class="fas fa-id-badge"></i
                  ><strong> Last Name : </strong> ${user.lastName}
                </p>
                <p>
                  <i class="fas fa-user"></i
                  ><strong> Username : </strong> ${user.username}
                </p>
                <p>
                  <i class="fas fa-envelope"></i
                  ><strong> Email : </strong> ${user.email}
                </p>
                <p>
                  <i class="fas fa-phone"></i
                  ><strong> Phone : </strong> ${user.phone}
                </p>
                <p>
                  <i class="fas fa-map-marker-alt"></i
                  ><strong> Address : </strong> ${user.address}
                </p>
              </div>
              <!-- Upload Profile Image -->
              <div class="upload-profile-image">
                <h2>Upload/Change Profile Image</h2>
                <form
                  action="${pageContext.request.contextPath}/user/uploadProfileImage"
                  method="post"
                  enctype="multipart/form-data"
                >
                  <input type="hidden" name="userId" value="${user.userId}" />

                  <div class="form-group">
                    <label for="profileImage">Select an image to upload:</label>
                    <input
                      type="file"
                      name="profileImage"
                      id="profileImage"
                      accept="image/*"
                      required
                    />
                  </div>

                  <button type="submit" class="btn btn-primary">
                    Upload Image
                  </button>
                </form>

                <c:if test="${not empty upload_error}">
                  <p class="error">${upload_error}</p>
                </c:if>
                <c:if test="${not empty upload_success}">
                  <p class="success">${upload_success}</p>
                </c:if>
              </div>
            </div>

            <h2>My Order History :</h2>
            <c:if test="${not empty user_orders}">
              <table class="order-table">
                <tr>
                  <th>Order ID</th>
                  <th>Status</th>
                  <th>Total (€)</th>
                  <th>Date</th>
                </tr>
                <c:forEach var="order" items="${user_orders}">
                  <tr>
                    <td>${order.orderId}</td>
                    <td>${order.status}</td>
                    <td>${order.totalPrice}</td>
                    <td>${order.orderDate}</td>
                  </tr>
                </c:forEach>
              </table>
            </c:if>
            <c:if test="${empty user_orders}">
              <p>No orders found.</p>
            </c:if>
          </c:if>

          <c:if test="${not empty order_error}">
            <p class="error">${order_error}</p>
          </c:if>
          <c:if test="${empty user}">
            <p class="error">Error: User session not available.</p>
          </c:if>
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
      // Close sidebar when clicking on any link inside it (on mobile)
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
