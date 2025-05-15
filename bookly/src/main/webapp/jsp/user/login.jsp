<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/login.css"
    />
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
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <%@ include file="/html/header.html" %>
    <div class="container py-5 px-3">
      <div class="row justify-content-center">
        <div class="col-lg-5 col-md-7 col-12">
          <div class="login-card shadow-sm p-4">
            <h2 class="text-center mb-4">Sign In</h2>

            <!-- <c:if test="${not empty error_message}">
              <div class="alert alert-danger">${error_message}</div>
            </c:if> -->

            <form
              class="form-group"
              action="${pageContext.request.contextPath}/user/login"
              method="post"
              novalidate
            >
              <div class="mb-3">
                <input
                  class="form-control"
                  type="text"
                  name="username"
                  placeholder="Username"
                  required
                />
              </div>
              <div class="mb-3" style="position: relative">
                <input
                  class="form-control"
                  type="password"
                  name="password"
                  id="password"
                  placeholder="Password"
                  required
                />
                <span class="toggle-password" onclick="togglePassword()"></span>
              </div>
              <button class="signin-btn" type="submit">Sign In</button>
            </form>

            <div class="create-account text-center mt-3">
              <p>
                Don't have an account?
                <a href="${pageContext.request.contextPath}/user/register"
                  >Sign Up</a
                >
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <script src="../../static/js/toggle-password.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
