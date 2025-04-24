<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Author" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>All Authors</title>
</head>
<body>
    <%@ include file="/html/cdn.html" %> 
    <%@ include file="/html/header.html" %>
    <h1>Book Authors</h1>

    <ul>
        <%
            List<Author> authors = (List<Author>) request.getAttribute("all_authors");
            if (authors != null) {
                for (Author author : authors) {
        %>
                    <li>
                        <a href="<%= request.getContextPath() %>/author/<%= author.getAuthorId() %>">
                            <%= author.getName() %>
                        </a>
                    </li>
        <%
                }
            } else {
        %>
            <li>No authors found.</li>
        <%
            }
        %>
    </ul>
</body>
</html>
