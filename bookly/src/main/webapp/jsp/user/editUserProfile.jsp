<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/edit-user-profile.css">
    <title>Edit User Profile</title>
</head>
<body>
    <div class="profile-container">
    <h1>Edit User Profile</h1>
    <form action="${pageContext.request.contextPath}/user/editUserProfile" method="post">
        <label for="name">First Name:</label>
        <input type="text" id="firstname" name="firstName" value="${user.firstName}" required><br>
        <label for="lastname">Last Name:</label>
        <input type="text" id="lastname" name="lastName" value="${user.lastName}" required><br>
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
    </div>
</body>
</html>