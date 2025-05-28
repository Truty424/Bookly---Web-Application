<%@ page contentType="text/html;charset=UTF-8" %>
  <html>

  <head>
    <title>Change Password</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta charset="ISO-8859-1" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/components/forms.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/userDashboard.css"
      type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/changePassword.css"
      type="text/css" />
    <%@ include file="/html/cdn.html" %>
  </head>

  <body>

    <%@ include file="/html/header.html" %>
      <div class="profile-container">
        <div>
          <h2>Change Password</h2>
          <form action="${pageContext.request.contextPath}/user/changePassword" method="post" class="form-group">
            <div class="form-group" style="position: relative;">
              <label for="NewPassword">New Password</label>
              <input type="password" name="newPassword" placeholder="New Password" required id="NewPassword" />
              <span class="toggle-password" onclick="togglePassword('newPassword', this)">👁️</span>
              <button type="submit" class="btn my-4">Update</button>
            </div>
        </div>
      </div>
      <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
      <script>
        function togglePassword(inputId, iconSpan) {
          const input = document.getElementById(inputId);
          if (input.type === "password") {
            input.type = "text";
            iconSpan.textContent = "🙈";
          } else {
            input.type = "password";
            iconSpan.textContent = "👁️";
          }
        }
      </script>
  </body>

  </html>