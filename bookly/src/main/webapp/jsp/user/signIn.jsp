<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Sign In</title>
</head>
<body>
    <h1>Sign In</h1>
    <form action="${pageContext.request.contextPath}/user/login" method="post">
        <input type="text" id="usernameOrEmail" name="usernameOrEmail" placeholder="Enter your username or email" required><br>
        <input type="password" id="password" name="password" placeholder="Enter your password" required><br>
        <button type="submit">Login</button>
    </form>
    
    <p>
        <a href="${pageContext.request.contextPath}/user/forgotPassword" style="text-decoration: underline;">Forgot password?</a>
    </p>
    
    <button onclick="location.href='${pageContext.request.contextPath}/user/register'">Create a new account</button>
</body>
</html>