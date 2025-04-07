<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Edit User Profile</title>
</head>
<body>
    <h1>Edit User Profile</h1>
    <form action="${pageContext.request.contextPath}/user/updateUserProfile" method="post">
        <label for="name">First Name:</label>
        <input type="text" id="name" name="name" value="${user.name}" required><br>
        <label for="lastname">Last Name:</label>
        <input type="text" id="lastname" name="lastname" value="${user.lastname}" required><br>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="${user.username}" required><br>
        <button type="submit">Save Changes</button>
    </form>
</body>
</html>