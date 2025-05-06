<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Category" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>All Categories</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/root.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/base/globals.css"
      type="text/css"
    />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/components/header.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/components/footer.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/pages/allCategories.css" type="text/css" />
    <%@ include file="/html/cdn.html" %>
</head>
<body>
    <%@ include file="/html/header.html" %>
    <div class="container">
        <h1 class="page-title">Book Categories</h1>
        <div class="categories-grid">
            <%
                List<Category> categories = (List<Category>) request.getAttribute("all_categories");
                if (categories != null) {
                    for (Category category : categories) {
            %>
                        <div class="category-card">
                            <img src="<%= request.getContextPath() %>/static/img/categories/<%= category.getCategory_id() %>.jpg" alt="<%= category.getCategory_name() %>" class="category-image" />
                            <h3 class="category-title">
                                <a href="<%= request.getContextPath() %>/category/<%= category.getCategory_id() %>" class="category-link">
                                    <%= category.getCategory_name() %>
                                </a>
                            </h3>
                        </div>
            <%
                    }
                } else {
            %>
                <p class="no-categories">No categories found.</p>
            <%
                }
            %>
        </div>
    </div>
    <%@ include file="/html/footer.html" %>
</body>
</html>