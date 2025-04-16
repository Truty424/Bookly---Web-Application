<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Edit User Profile</title>
</head>
<body>
    <h1>Edit User Profile</h1>
    <form action="${pageContext.request.contextPath}/user/editUserProfile" method="post">
        <label for="name">First Name:</label>
        <input type="text" id="firstname" name="firstname" value="${user.firstName}" required><br>
        <label for="lastname">Last Name:</label>
        <input type="text" id="lastname" name="lastname" value="${user.lastName}" required><br>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="${user.username}" required><br>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="${user.email}" required><br>
        <label for="phone">Phone:</label>
        <input type="text" id="phone" name="phone" value="${user.phone}"><br>
        <label for="address">Address:</label>
        <input type="text" id="address" name="address" value="${user.address}"><br>
        <button type="submit">Save Changes</button>
    </form>
</body>
</html>