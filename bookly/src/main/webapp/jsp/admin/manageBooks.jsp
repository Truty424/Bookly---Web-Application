<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>

<html>
<head>
    <title>Manage Books</title>
</head>
<body>

<h2>Manage Books</h2>

<a class="add-btn" href="<%= request.getContextPath() %>/jsp/admin/addBook.jsp">+ Add Book</a>

<%
    List<Book> books = (List<Book>) request.getAttribute("books");
    if (books != null && !books.isEmpty()) {
%>
    <table>
        <tr>
            <th>ID</th><th>Title</th><th>ISBN</th><th>Price</th><th>Stock</th><th>Actions</th>
        </tr>
        <% for (Book book : books) { %>
        <tr>
            <td><%= book.getBookId() %></td>
            <td><%= book.getTitle() %></td>
            <td><%= book.getIsbn() %></td>
            <td>€<%= book.getPrice() %></td>
            <td><%= book.getStockQuantity() %></td>
            <td class="actions">
                <!-- Edit Form -->
                <form action="<%= request.getContextPath() %>/admin/updateBook" method="post">
                    <input type="hidden" name="book_id" value="<%= book.getBookId() %>" />
                    <button class="edit-btn" type="submit">Edit</button>
                </form>

                <!-- Delete Form -->
                <form action="<%= request.getContextPath() %>/admin/deleteBook" method="post" onsubmit="return confirm('Are you sure?');">
                    <input type="hidden" name="book_id" value="<%= book.getBookId() %>" />
                    <button class="delete-btn" type="submit">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
<% } else { %>
    <p>No books found.</p>
<% } %>

<p><a href="<%= request.getContextPath() %>/admin/dashboard">Back to Dashboard</a></p>

</body>
</html>
