<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>forget Password</title>
</head>
<body>
    <h1>change Password</h1>
    <form method="post" action="${pageContext.request.contextPath}/user/changePassword">
        <label for="currentPassword">Current Password:</label>
        <input type="password" id="currentPassword" name="currentPassword" required><br>
        <label for="newPassword">New Password:</label>
        <input type="password" id="newPassword" name="newPassword" required><br>
        <button type="submit">Change Password</button>
    </form>
</body>
</html>