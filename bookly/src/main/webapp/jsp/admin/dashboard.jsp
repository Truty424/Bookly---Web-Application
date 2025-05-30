<%@ page contentType="text/html;charset=UTF-8" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta charset="ISO-8859-1" />
    <title>Admin Dashboard</title>

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
      href="${pageContext.request.contextPath}/static/css/components/forms.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/components/signup.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/adminDashboard.css"
      type="text/css"
    />
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <!-- Get user from session -->
    <c:set var="user" value="${sessionScope.user}" />
    <c:set var="name" value="${not empty user ? user.firstName : 'Admin'}" />

    <div class="admin-container">
      <div class="dashboard-header text-center my-4">
        <h1 class="text-bold">Welcome 👋</h1>
        <p class="text-muted">Manage the system from your dashboard</p>
      </div>

      <div class="dashboard-cards">
        <a class="card-link" href="${pageContext.request.contextPath}/admin/books">
          <div class="card-title">📚 Manage Books</div>
          <p class="text-muted small mt-2">
            Add, edit or delete books in the system
          </p>
        </a>

        <a class="card-link" href="${pageContext.request.contextPath}/admin/authors">
          <div class="card-title">🖋 Manage Authors</div>
          <p class="text-muted small mt-2">
            Manage author information and profiles
          </p>
        </a>

        <a class="card-link" href="${pageContext.request.contextPath}/admin/publishers">
          <div class="card-title">🏢 Manage Publishers</div>
          <p class="text-muted small mt-2">Manage publisher companies</p>
        </a>

        <a class="card-link" href="${pageContext.request.contextPath}/admin/discounts">
          <div class="card-title">💸 Manage Discounts</div>
          <p class="text-muted small mt-2">Set up discount campaigns</p>
        </a>
      </div>

      <form action="${pageContext.request.contextPath}/user/logout" method="post" class="text-center">
        <button class="logout-btn" type="submit">Log Out</button>
      </form>
    </div>
  </body>
</html>
