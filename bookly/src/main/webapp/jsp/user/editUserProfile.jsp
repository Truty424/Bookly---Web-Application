<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <title>Edit User Profile</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta charset="ISO-8859-1" />
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
      href="${pageContext.request.contextPath}/static/css/pages/editUserProfile.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/userDashboard.css"
      type="text/css"
    />

    <%@ include file="/html/cdn.html" %>
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
          <h2>Edit Your Profile</h2>
          <form
          action="${pageContext.request.contextPath}/user/editUserProfile"
          method="post"
        >
          <div class="form-group">
            <label for="firstname">First Name:</label>
            <input
              type="text"
              id="firstname"
              name="firstName"
              value="${user.firstName}"
              required
            />
          </div>
        
          <div class="form-group">
            <label for="lastname">Last Name:</label>
            <input
              type="text"
              id="lastname"
              name="lastName"
              value="${user.lastName}"
              required
            />
          </div>
        
          <div class="form-group">
            <label for="username">Username:</label>
            <input
              type="text"
              id="username"
              name="username"
              value="${user.username}"
              required
            />
          </div>
        
          <div class="form-group">
            <label for="email">Email:</label>
            <input
              type="email"
              id="email"
              name="email"
              value="${user.email}"
              required
            />
          </div>
        
          <div class="form-group">
            <label for="phone">Phone:</label>
            <input
              type="tel"
              id="phone"
              name="phone"
              value="${user.phone}"
            />
          </div>
        
          <div class="form-group">
            <label for="address">Address:</label>
            <input
              type="text"
              id="address"
              name="address"
              value="${user.address}"
            />
          </div>
          <button type="submit" class="save-button">Save The Changes</button>
          </form>
        </main>
      </div>
    </div>
  </body>
</html>
