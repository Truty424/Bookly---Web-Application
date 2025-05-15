<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <title>Change Password</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <%@ include file="/html/cdn.html" %>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/root.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/globals.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/userDashboard.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/changePassword.css"
      type="text/css"
    />
  </head>
  <body>
    <%@ include file="/html/header.html" %>
    <main class="container">
      <div class="d-flex justify-content-center align-items-center min-vh-100">
        <div class="card">
          <h2>Change Your Password</h2>
          <form
            action="${pageContext.request.contextPath}/user/changePassword"
            method="post"
          >
            <div class="form-group">
              <label for="currentPassword">Your Current Password :</label>
              <input
                type="currentpassword"
                name="currentPassword"
                id="currentPassword"
                placeholder="Enter your current password"
                required
              />
            </div>
            <div class="form-group">
              <label for="newPassword">Your New Password :</label>
              <input
                type="newPassword"
                name="newPassword"
                id="newPassword"
                placeholder="Enter your new password"
                required
              />
            </div>
            <button type="submit" class="update-button">Update Password</button>
          </form>
        </main>
      </div>
    </div>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
  </body>
</html>