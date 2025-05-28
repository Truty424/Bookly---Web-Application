<%@ page contentType="text/html;charset=UTF-8" %> 
<%@ page import="it.unipd.bookly.Resource.User" %>

<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta charset="ISO-8859-1" />
    <title>Admin Dashboard</title>

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
    <link
      rel="stylesheet"
      href="../../static/css/pages/adminDashboard.css"
      type="text/css"
    />
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <% User user = (User) session.getAttribute("user"); String name = (user !=
    null) ? user.getFirstName() : "Admin"; %>
    <div
      class="container vh-100 d-flex flex-column justify-content-center align-items-center"
    >
      <div class="dashboard-header text-center mb-4">
        <h1 class="text-bold">Welcome, <%= name %> 👋</h1>
        <p class="text-muted">Manage the system from your dashboard</p>
      </div>

      <div class="dashboard-cards">
        <a class="card-link" href="<%= request.getContextPath() %>/admin/books">
          <div class="card-title">📚 Manage Books</div>
          <p class="text-muted small mt-2">
            Add, edit or delete books in the system
          </p>
        </a>

        <a
          class="card-link"
          href="<%= request.getContextPath() %>/admin/authors"
        >
          <div class="card-title">🖋 Manage Authors</div>
          <p class="text-muted small mt-2">
            Manage author information and profiles
          </p>
        </a>

        <a
          class="card-link"
          href="<%= request.getContextPath() %>/admin/publishers"
        >
          <div class="card-title">🏢 Manage Publishers</div>
          <p class="text-muted small mt-2">Manage publisher companies</p>
        </a>

        <a
          class="card-link"
          href="<%= request.getContextPath() %>/admin/discounts"
        >
          <div class="card-title">💸 Manage Discounts</div>
          <p class="text-muted small mt-2">Set up discount campaigns</p>
        </a>
      </div>

      <form
        action="<%= request.getContextPath() %>/user/logout"
        method="post"
        class="text-center"
      >
        <button class="logout-btn" type="submit">Log Out</button>
      </form>
    </div>
  </body>
</html>
