<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Books by Author</title>
</head>
<body>
    <%@ include file="/html/cdn.html" %> 
    <%@ include file="/html/header.html" %>
    <h1>Books by Author ID: ${author_id}</h1>

    <ul>
        <%
            List<Book> books = (List<Book>) request.getAttribute("author_books");
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
            <li>No books found by this author.</li>
        <%
            }
        %>
    </ul>

    <a href="<%= request.getContextPath() %>/author">Back to all authors</a>
</body>
</html>
