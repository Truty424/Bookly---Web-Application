<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/login.css">
    <title>Sign In</title>
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
      href="../../static/css/components/login.css"
      type="text/css"
    />
  </head>
  <body>
    <%@ include file="/html/cdn.html" %> <%@ include file="/html/header.html" %>
    <div class="signin-container">
      <div class="login-card">
        <h2>Sign In</h2>
        <form
          class="form-group"
          method="post"
          action="${pageContext.request.contextPath}/user/login"
        >
          <div>
            <label for="username">Username</label>
            <input
              type="text"
              name="username"
              placeholder="username"
              required
            />
          </div>
          <div>
            <label for="password">Password</label>
            <input
              type="password"
              name="password"
              placeholder="Password"
              required
            />
          </div>
          <button class="login-btn" type="submit">Login</button>
        </form>
        <div class="forgot-password">
          <a href="${pageContext.request.contextPath}/user/changePassword"
            >Forgot password?</a
          >
        </div>
        <div>
          <p class="create-account">
            Don't have an account?<a
              href="${pageContext.request.contextPath}/user/register"
            >
              Create a new account</a
            >
          </p>
        </div>
      </div>
    </div>
  </body>
</html>
