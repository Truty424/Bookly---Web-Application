<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>User Profile</title>
</head>
<body>
    <h1>Profile</h1>
    <p><strong>First Name:</strong> ${user.firstName}</p>
    <p><strong>Last Name:</strong> ${user.lastName}</p>
    <p><strong>Username:</strong> ${user.username}</p>
    <p><strong>Email:</strong> ${user.email}</p>
    <p><strong>Phone:</strong> ${user.phone}</p>
    <p><strong>Address:</strong> ${user.address}</p>
    <img src="${pageContext.request.contextPath}/user/image/${user.userId}" alt="Profile Image"/>

    <h2>History</h2>
    <ul>
        <c:forEach var="historyItem" items="${user.history}">
            <li>${historyItem}</li>
        </c:forEach>
    </ul>
</body>
</html>