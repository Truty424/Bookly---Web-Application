<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Category" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>All Categories</title>
</head>
<body>
    <form action="<%= request.getContextPath() %>/" method="get">
        <button type="submit">Go to Home</button>
    </form>
    <h1>Book Categories</h1>

    <ul>
        <%
            List<Category> categories = (List<Category>) request.getAttribute("all_categories");
            if (categories != null) {
                for (Category category : categories) {
        %>
                    <li>
                        <a href="<%= request.getContextPath() %>/category/<%= category.getCategory_id() %>">
                            <%= category.getCategory_name() %>
                        </a>
                    </li>
        <%
                }
            } else {
        %>
            <li>No categories found.</li>
        <%
            }
        %>
    </ul>
</body>
</html>
