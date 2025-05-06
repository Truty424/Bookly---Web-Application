<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Author" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>All Authors</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/pages/allAuthors.css" type="text/css" />
    <%@ include file="/html/cdn.html" %>

</head>
<body>
    <%@ include file="/html/header.html" %>
    <div class="container">
        <h1 class="page-title">Book Authors</h1>
        <div class="authors-grid">
            <%
                List<Author> authors = (List<Author>) request.getAttribute("all_authors");
                if (authors != null) {
                    for (Author author : authors) {
            %>
                <div class="author-card">
                    <h3 class="author-name">
                        <a href="<%= request.getContextPath() %>/author/<%= author.getAuthorId() %>">
                            <%= author.getName() %>
                        </a>
                    </h3>
                </div>
            <%
                    }
                } else {
            %>
                <p class="no-authors">No authors found.</p>
            <%
                }
            %>
        </div>
    </div>
    <%@ include file="/html/footer.html" %>
</body>
</html>
