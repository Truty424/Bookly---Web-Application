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
    <div class="container-fluid p-0">
      <div class="d-flex">
        <aside class="sidebar">
          <div class="sidebar-content">
            <ul class="nav-top">
              <li>
                <a href="${pageContext.request.contextPath}/user/profile"
                  ><i class="fas fa-user"></i> My profile</a
                >
              </li>
              <li>
                <a
                  href="${pageContext.request.contextPath}/user/editUserProfile"
                  ><i class="fas fa-edit"></i> Edit Profile</a
                >
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/user/changePassword"
                  class="active"
                  ><i class="fas fa-lock"></i> Edit Password</a
                >
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/wishlist"
                  ><i class="fas fa-heart"></i> My Wishlist</a
                >
              </li>
            </ul>

            <ul class="nav-bottom">
              <li>
                <a href="${pageContext.request.contextPath}/"
                  ><i class="fas fa-home"></i> Home Page</a
                >
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/logout"
                  ><i class="fas fa-sign-out-alt"></i> Logout</a
                >
              </li>
            </ul>
          </div>
        </aside>

        <main class="profile-content">
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