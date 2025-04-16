<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Sign In</title>
</head>
<body>
    <h1>Sign In</h1>
    <form method="post" action="${pageContext.request.contextPath}/user/login">
        <input type="text" name="email" placeholder="Email" required />
        <input type="password" name="password" placeholder="Password" required />
        <button type="submit">Login</button>
    </form>
    <p>
        <a href="${pageContext.request.contextPath}/user/changePassword" style="text-decoration: underline;">Forgot password?</a>
    </p>
    
    <button onclick="location.href='${pageContext.request.contextPath}/user/register'">Create a new account</button>
</body>
</html>