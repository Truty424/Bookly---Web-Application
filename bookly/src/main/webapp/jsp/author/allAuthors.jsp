<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Author" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>All Authors</title>
    <%@ include file="/html/cdn.html" %> 
    <link
    rel="stylesheet"
    href="<%= request.getContextPath() %>/static/css/pages/allAuthors.css"
    type="text/css"
    />
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
                    <img
                        src="<%= request.getContextPath() %>/static/img/authors/<%= author.getAuthorId() %>.jpg"
                        alt="<%= author.getName() %>"
                        class="author-image"
                    />
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
</body>
</html>
