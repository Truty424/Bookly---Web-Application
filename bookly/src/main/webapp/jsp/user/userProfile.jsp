<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Profile</title>
</head>
<body>
    <h1>Profile</h1>

    <c:if test="${not empty user}">
        <p><strong>First Name:</strong> ${user.firstName}</p>
        <p><strong>Last Name:</strong> ${user.lastName}</p>
        <p><strong>Username:</strong> ${user.username}</p>
        <p><strong>Email:</strong> ${user.email}</p>
        <p><strong>Phone:</strong> ${user.phone}</p>
        <p><strong>Address:</strong> ${user.address}</p>
        <img src="${pageContext.request.contextPath}/user/image/${user.userId}" alt="Profile Image" width="150" height="150"/>

        <h2>History</h2>
        <c:if test="${not empty user.history}">
            <ul>
                <c:forEach var="historyItem" items="${user.history}">
                    <li>${historyItem}</li>
                </c:forEach>
            </ul>
        </c:if>
        <c:if test="${empty user.history}">
            <p>No history available.</p>
        </c:if>
    </c:if>

    <c:if test="${empty user}">
        <p>Error: User data not available.</p>
    </c:if>
</body>
</html>
