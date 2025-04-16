<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipd.bookly.Resource.Author" %>

<html>
<head>
    <title>Manage Authors</title>
</head>
<body>

<h2>Manage Authors</h2>

<a class="add-btn" href="<%= request.getContextPath() %>/jsp/admin/addAuthor.jsp">+ Add Author</a>

<%
    List<Author> authors = (List<Author>) request.getAttribute("authors");
    if (authors != null && !authors.isEmpty()) {
%>
    <table>
        <tr>
            <th>ID</th><th>Name</th><th>Nationality</th><th>Actions</th>
        </tr>
        <% for (Author author : authors) { %>
        <tr>
            <td><%= author.getAuthorId() %></td>
            <td><%= author.getFirstName() %> <%= author.getLastName() %></td>
            <td><%= author.getNationality() %></td>
            <td class="actions">
                <!-- Edit -->
                <form action="<%= request.getContextPath() %>/admin/updateAuthor" method="post">
                    <input type="hidden" name="author_id" value="<%= author.getAuthorId() %>" />
                    <input type="hidden" name="first_name" value="<%= author.getFirstName() %>" />
                    <input type="hidden" name="last_name" value="<%= author.getLastName() %>" />
                    <input type="hidden" name="biography" value="<%= author.getBiography() %>" />
                    <input type="hidden" name="nationality" value="<%= author.getNationality() %>" />
                    <button class="edit-btn" type="submit">Edit</button>
                </form>

                <!-- Delete -->
                <form action="<%= request.getContextPath() %>/admin/deleteAuthor" method="post" onsubmit="return confirm('Are you sure?');">
                    <input type="hidden" name="author_id" value="<%= author.getAuthorId() %>" />
                    <button class="delete-btn" type="submit">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
<% } else { %>
    <p>No authors found.</p>
<% } %>

<p><a href="<%= request.getContextPath() %>/admin/dashboard">Back to Dashboard</a></p>

</body>
</html>
