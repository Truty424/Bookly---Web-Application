<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Register</title>
    <meta charset="UTF-8">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/components/form.css">>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/components/buttons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/utilities/display.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/utilities/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/utilities/spacing.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/utilities/toast.css">

    <script defer src="${pageContext.request.contextPath}/static/js/password-show.js"></script>
</head>
<body>

<div class="container" style="margin-top: 50px; max-width: 600px;">
    <h2 class="text-center">Create Your Account</h2>

    <c:if test="${not empty error_message}">
        <p class="text-danger"><strong>${error_message}</strong></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/user/register" method="post" novalidate>

        <div class="form-control">
            <label for="username">Username</label>
            <input class="form-input" type="text" id="username" name="username" required />
        </div>

        <div class="form-control">
            <label for="email">Email</label>
            <input class="form-input" type="email" id="email" name="email" required />
        </div>

        <div class="form-control">
            <label for="password">Password</label>
            <input class="form-input" type="password" id="password" name="password" minlength="8" required />
            <div style="margin-top: 0.5rem;">
                <input type="checkbox" onclick="togglePasswordVisibility()" id="showPassword" />
                <label for="showPassword">Show Password</label>
            </div>
        </div>

        <div class="form-control">
            <label for="firstName">First Name</label>
            <input class="form-input" type="text" id="firstName" name="firstName" required />
        </div>

        <div class="form-control">
            <label for="lastName">Last Name</label>
            <input class="form-input" type="text" id="lastName" name="lastName" required />
        </div>

        <div class="form-control">
            <label for="phone">Phone</label>
            <input class="form-input" type="text" id="phone" name="phone" placeholder="+1234567890" required />
        </div>

        <div class="form-control">
            <label for="address">Address</label>
            <input class="form-input" type="text" id="address" name="address" required />
        </div>

        <div class="form-control" style="margin-top: 20px;">
            <button type="submit" class="btn btn-primary w-100">Create Account</button>
        </div>
    </form>

    <p class="text-center" style="margin-top: 20px;">
        Already have an account?
        <a href="${pageContext.request.contextPath}/user/login">Sign In</a>
    </p>
</div>

</body>
</html>
