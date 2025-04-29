<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <div class="container-fluid p-0">
      <div class="d-flex">
        <aside class="sidebar">
            <div class="sidebar-content">
                <ul class="nav-top">
                    <li><a href="${pageContext.request.contextPath}/user/profile"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/editUserProfile"><i class="fas fa-edit"></i> Edit Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/changePassword"><i class="fas fa-lock"></i> Edit Password</a></li>
                    <li><a href="${pageContext.request.contextPath}/wishlist"><i class="fas fa-heart"></i> Wishlist</a></li>
                </ul>
        
                <ul class="nav-bottom">
                    <li><a href="${pageContext.request.contextPath}/"><i class="fas fa-home"></i> Home Page</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                </ul>
            </div>
        </aside>
        
        <main class="profile-content">
          <h1>User Profile</h1>

          <c:if test="${not empty user}">
            <div class="profile-card">
              <img
                class="profile-img"
                src="${pageContext.request.contextPath}/user/image/${user.userId}"
                alt="Profile Image"
              />
              <div>
                <p><strong>First Name:</strong> ${user.firstName}</p>
                <p><strong>Last Name:</strong> ${user.lastName}</p>
                <p><strong>Username:</strong> ${user.username}</p>
                <p><strong>Email:</strong> ${user.email}</p>
                <p><strong>Phone:</strong> ${user.phone}</p>
                <p><strong>Address:</strong> ${user.address}</p>
              </div>
            </div>


            <h2>Order History</h2>
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
