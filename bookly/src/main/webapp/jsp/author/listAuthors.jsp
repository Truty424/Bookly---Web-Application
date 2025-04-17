<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipd.bookly.resource.Author" %>
<%
    List<Author> authors = (List<Author>) request.getAttribute("authors");
%>
<html>
<head>
    <title>Author List</title>
</head>
<body>
    <h2>All Authors</h2>
    <table border="1">
        <tr><th>ID</th><th>Name</th></tr>
        <%
            if (authors != null) {
                for (Author a : authors) {
        %>
        <tr>
            <td><%= a.getId() %></td>
            <td><%= a.getName() %></td>
        </tr>
        <%
                }
            } else {
        %>
        <tr><td colspan="2">No authors found.</td></tr>
        <% } %>
    </table>
</body>
</html>
