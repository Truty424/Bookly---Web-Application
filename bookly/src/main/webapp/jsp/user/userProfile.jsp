<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Profile</title>
</head>
<body>
    <h1>User Profile</h1>

    <c:if test="${not empty user}">
        <p><strong>First Name:</strong> ${user.firstName}</p>
        <p><strong>Last Name:</strong> ${user.lastName}</p>
        <p><strong>Username:</strong> ${user.username}</p>
        <p><strong>Email:</strong> ${user.email}</p>
        <p><strong>Phone:</strong> ${user.phone}</p>
        <p><strong>Address:</strong> ${user.address}</p>

        <img src="${pageContext.request.contextPath}/user/image/${user.userId}" alt="Profile Image" width="150" height="150"/>

        <div class="button-group" style="margin-top: 20px;">
            <form action="${pageContext.request.contextPath}/user/editUserProfile" method="get">
                <button type="submit">Edit Profile</button>
            </form>
            <form action="${pageContext.request.contextPath}/user/changePassword" method="get">
                <button type="submit">Change Password</button>
            </form>
            <form action="${pageContext.request.contextPath}/user/logout" method="post">
                <button type="submit">Log Out</button>
            </form>
            <form action="${pageContext.request.contextPath}/" method="get">
                <button type="submit">Home Page</button>
            </form>
            <form action="${pageContext.request.contextPath}/wishlist" method="get" style="display: inline;">
                <button type="submit">Wishlist</button>
            </form>
        </div>

        <h2 style="margin-top: 30px;">Order History</h2>
        <c:if test="${not empty user_orders}">
            <table border="1" cellpadding="5" cellspacing="0">
                <tr>
                    <th>Order ID</th>
                    <th>Status</th>
                    <th>Total Price (€)</th>
                    <th>Order Date</th>
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
        <p style="color: red;"><strong>${order_error}</strong></p>
    </c:if>
    <c:if test="${empty user}">
        <p style="color: red;">Error: User session not available.</p>
    </c:if>
</body>
</html>
