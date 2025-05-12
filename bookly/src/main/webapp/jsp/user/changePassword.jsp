<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <title>Change Password</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta charset="ISO-8859-1" />
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/components/forms.css"
    type="text/css" />

  </head>
  <body>
 <%@ include file="/html/header.html" %>
        <div class="container">
            <div class="d-flex justify-content-center align-items-center vh-100">
                <div class="card">
                    <h2>Change Password</h2>
                    <form
                    action="${pageContext.request.contextPath}/user/changePassword"
                    method="post"
                    class="form-group"
                    >
                    <div>
                    <label for="currentPassword">Current Password</label>
                    <input
                        type="password"
                        name="currentPassword"
                        placeholder="New Password"
                        required
                        id="currentPassword"
                    />
                    <button class="my-3" type="submit">Update</button>
                    </div>
                </div>
            </div>
        </div>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
