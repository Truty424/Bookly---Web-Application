<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Books in Category</title>
    <%@ include file="/html/cdn.html" %> 
</head>
<body>

    <%@ include file="/html/header.html" %>
    <h1>Books in Selected Category</h1>

    <ul>
        <%
            List<Book> books = (List<Book>) request.getAttribute("category_books");
            if (books != null && !books.isEmpty()) {
                for (Book book : books) {
        %>
                    <li>
                        <strong><%= book.getTitle() %></strong><br/>
                        ISBN: <%= book.getIsbn() %><br/>
                        Price: $<%= book.getPrice() %><br/>
                        Language: <%= book.getLanguage() %>
                    </li>
                    <hr/>
        <%
                }
            } else {
        %>
            <li>No books found in this category.</li>
        <%
            }
        %>
    </ul>

    <a href="<%= request.getContextPath() %>/category">Back to all categories</a>
</body>
</html>
