<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.User" %>

<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>

<%
    User user = (User) session.getAttribute("user");
    String name = (user != null) ? user.getFirstName() : "Admin";
%>

<h1>Welcome, <%= name %> 👋</h1>
<p>This is your admin dashboard. Use the links below to manage the system.</p>

<div class="admin-links">
    <a class="admin-link" href="<%= request.getContextPath() %>/admin/books"> Manage Books</a>
    <a class="admin-link" href="<%= request.getContextPath() %>/admin/authors">Manage Authors</a>
    <a class="admin-link" href="<%= request.getContextPath() %>/admin/publishers"> Manage Publishers</a>
    <a class="admin-link" href="<%= request.getContextPath() %>/admin/discounts"> Manage Discounts</a>
</div>

<form action="<%= request.getContextPath() %>/user/logout" method="post" class="logout-form">
    <button class="logout-btn" type="submit">Log Out</button>
</form>

</body>
</html>
