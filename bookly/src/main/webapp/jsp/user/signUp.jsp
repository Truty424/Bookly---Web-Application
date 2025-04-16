<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Register</title>
</head>
<body>

<h2>Sign Up</h2>

<% String error = (String) request.getAttribute("error_message"); %>
<% if (error != null) { %>
    <p class="error"><%= error %></p>
<% } %>

<form action="<%= request.getContextPath() %>/user/register" method="post">
    <input type="text" name="username" placeholder="Username" required />
    <input type="email" name="email" placeholder="Email" required />
    <input type="password" name="password" placeholder="Password (min 8 chars)" required minlength="8" />
    <input type="text" name="firstName" placeholder="First Name" required />
    <input type="text" name="lastName" placeholder="Last Name" required />
    <input type="text" name="phone" placeholder="Phone (e.g. +1234567890)" required />
    <input type="text" name="address" placeholder="Address" required />
    <button type="submit">Create Account</button>
</form>

<p>Already have an account? <a href="login.jsp">Sign In</a></p>

</body>
</html>
