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
            <form action="${pageContext.request.contextPath}/user/edit" method="get">
                <button type="submit">Edit Profile</button>
            </form>
            <form action="${pageContext.request.contextPath}/user/changePassword" method="get">
                <button type="submit">Change Password</button>
            </form>
            <form action="${pageContext.request.contextPath}/logout" method="post">
                <button type="submit">Log Out</button>
            </form>
        </div>

        <h2 style="margin-top: 30px;">Orders</h2>
        <c:if test="${not empty user.orders}">
            <ul>
                <c:forEach var="orderItem" items="${user.orders}">
                    <li>${orderItem}</li>
                </c:forEach>
            </ul>
        </c:if>
        <c:if test="${empty user.orders}">
            <p>No order available.</p>
        </c:if>
    </c:if>

    <c:if test="${empty user}">
        <p style="color: red;">Error: User session not available.</p>
    </c:if>
</body>
</html>
