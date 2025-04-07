<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Edit Profile</title>
</head>
<body>
    <h1>Edit Profile</h1>
    <form method="post" action="${pageContext.request.contextPath}/user/update">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="${user.email}" required><br>
        <label for="phone">Phone:</label>
        <input type="text" id="phone" name="phone" value="${user.phone}"><br>
        <label for="address">Address:</label>
        <input type="text" id="address" name="address" value="${user.address}"><br>
        <button type="submit">Update</button>
    </form>
</body>
</html>