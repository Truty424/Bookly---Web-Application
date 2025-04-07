<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Sign Up</title>
</head>
<body>
    <h1>Sign Up</h1>
    <form action="${pageContext.request.contextPath}/user/register" method="post">
        <input type="text" id="firstName" name="firstName" placeholder="Enter your first name" required><br>
        <input type="text" id="lastName" name="lastName" placeholder="Enter your last name" required><br>
        <input type="text" id="username" name="username" placeholder="Enter your username" required><br>
        <input type="password" id="password" name="password" placeholder="Enter your password" required><br>
        <input type="password" id="repeatPassword" name="repeatPassword" placeholder="Repeat your password" required><br>
        <input type="text" id="phone" name="phone" placeholder="Enter your phone number"><br>
        <input type="text" id="address" name="address" placeholder="Enter your address"><br>
        <input type="text" id="city" name="city" placeholder="Enter your city"><br>
        <input type="text" id="country" name="country" placeholder="Enter your country"><br>
        <button type="submit">Sign Up</button>
    </form>
</body>
</html>