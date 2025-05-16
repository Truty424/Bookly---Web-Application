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
          <h1>My Profile :</h1>

          <c:if test="${not empty user}">
            <div class="profile-card">
              <!-- Profile Image Section -->
              <div class="profile-img-wrapper">
               <c:choose>
                <c:when test="${not empty user.userId}">
                 <img
                  src="${pageContext.request.contextPath}/user/image/${user.userId}"
                  alt="Profile Image"
                  class="profile-img"
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
  </body>
</html>
