<%@ page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <%@ include file="/html/cdn.html"%>
    <title>Register</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta charset="ISO-8859-1" />
    <link
      rel="stylesheet"
      href="../../static/css/base/root.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="../../static/css/base/globals.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="../../static/css/components/forms.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="../../static/css/components/signup.css"
      type="text/css"
    />
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <%@ include file="/html/header.html" %>
    <div class="signup-container">
      <div class="signup-card">
        <h2 class="text-center">Create Your Account</h2>
        <form
          action="${pageContext.request.contextPath}/user/register"
          method="post"
          novalidate
        >
          <div class="row">
            <div class="col-md-6 mb-3 form-group">
              <label for="firstName">First Name</label>
              <input
                class="form-control"
                type="text"
                id="firstName"
                name="firstName"
                placeholder="First Name"
                required
              />
            </div>

            <div class="col-md-6 mb-3 form-group">
              <label for="lastName">Last Name</label>
              <input
                class="form-control"
                type="text"
                id="lastName"
                name="lastName"
                placeholder="Last Name"
                required
              />
            </div>
          </div>
          <div class="row">
            <div class="col-md-6 mb-3 form-group">
              <label for="username">Username</label>
              <input
                type="text"
                id="username"
                placeholder="username"
                name="username"
                required
              />
            </div>

            <div class="col-md-6 mb-3 form-group">
              <label for="email">Email</label>
              <input
                type="email"
                id="email"
                placeholder="enter your Email"
                name="email"
                required
              />
            </div>
          </div>

          <div class="row">
            <div class="col-md-6 mb-3 form-group" style="position: relative">
              <label for="password">Password</label>
              <input
                class="form-control"
                type="password"
                id="password"
                name="password"
                placeholder="enter your Password"
                pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$"
                minlength="4"
                required
              />
              <span class="toggle-password" onclick="togglePassword()"></span>
            </div>
            <div class="col-md-6 mb-3 form-group">
              <label for="phone">Phone</label>
              <input
                type="text"
                id="phone"
                name="phone"
                placeholder="+1234567890"
                required
              />
            </div>
          </div>

          <div class="row">
            <div class="col-md-12 mb-3 form-group">
              <label for="address">Address</label>
              <input
                type="text"
                placeholder="enter your address"
                id="address"
                name="address"
                required
              />
            </div>
          </div>

          <div class="row">
            <div class="col-12">
              <button type="submit" class="w-100 signup-btn mt-3">
                Create Account
              </button>
            </div>
          </div>

          <c:if test="${not empty error_message}">
            <p class="text-danger text-center mt-2">
              <strong>${error_message}</strong>
            </p>
          </c:if>
        </form>

        <p class="text-center signin-link mt-4">
          Already have an account?
          <a href="${pageContext.request.contextPath}/user/login">Sign In</a>
        </p>
      </div>
    </div>
    <script src="../../static/js/toggle-password.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
