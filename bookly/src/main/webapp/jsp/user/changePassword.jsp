<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Change Password</title>
</head>
<body>
    <h2>Change Password</h2>
    <form action="${pageContext.request.contextPath}/user/changePassword" method="post">
        <input type="password" name="password" placeholder="New Password" required />
        <button type="submit">Update</button>
    </form>

</body>
</html>