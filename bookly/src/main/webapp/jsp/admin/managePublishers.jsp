<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipd.bookly.Resource.Publisher" %>

<html>
<head>
    <title>Manage Publishers</title>
</head>
<body>

<h2>Manage Publishers</h2>

<a class="add-btn" href="<%= request.getContextPath() %>/jsp/admin/addPublisher.jsp">+ Add Publisher</a>

<%
    List<Publisher> publishers = (List<Publisher>) request.getAttribute("publishers");
    if (publishers != null && !publishers.isEmpty()) {
%>
    <table>
        <tr>
            <th>ID</th><th>Name</th><th>Phone</th><th>Address</th><th>Actions</th>
        </tr>
        <% for (Publisher publisher : publishers) { %>
        <tr>
            <td><%= publisher.getPublisherId() %></td>
            <td><%= publisher.getPublisherName() %></td>
            <td><%= publisher.getPhone() %></td>
            <td><%= publisher.getAddress() %></td>
            <td class="actions">

                <!-- Delete -->
                <form action="<%= request.getContextPath() %>/admin/deletePublisher" method="post" onsubmit="return confirm('Are you sure?');">
                    <input type="hidden" name="publisher_id" value="<%= publisher.getPublisherId() %>" />
                    <button class="delete-btn" type="submit">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
<% } else { %>
    <p>No publishers found.</p>
<% } %>

<p><a href="<%= request.getContextPath() %>/admin/dashboard">Back to Dashboard</a></p>

</body>
</html>
