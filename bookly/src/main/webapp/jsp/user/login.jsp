<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/login.css">
    <title>Sign In</title>
  </head>
  <body>
    <%@ include file="/html/cdn.html" %> <%@ include file="/html/header.html" %>
    <h1>Sign In</h1>
    <div class="login-container"></div>
    <form method="post" action="${pageContext.request.contextPath}/user/login">
      <input type="text" name="username" placeholder="username" required />
      <input type="password" name="password" placeholder="Password" required />
      <button type="submit">Login</button>
    </form>
  </div>
    <p>
      <a
        href="${pageContext.request.contextPath}/user/changePassword"
        style="text-decoration: underline"
        >Forgot password?</a
      >
    </p>

    <button
      onclick="location.href='${pageContext.request.contextPath}/user/register'"
    >
      Create a new account
    </button>
    <%@ include file="/html/footer.html" %>
  </body>
</html>
