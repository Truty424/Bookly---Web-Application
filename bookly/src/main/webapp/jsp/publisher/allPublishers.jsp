<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipd.bookly.Resource.Publisher" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Publishers</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/components/header.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/components/footer.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/pages/allPublishers.css" type="text/css" />
    <%@ include file="/html/cdn.html" %>
</head>
<body>
    <%@ include file="/html/header.html" %>
    <div class="container">
        <h1 class="page-title">All Publishers</h1>
        <div class="publishers-grid">
            <%
                List<Publisher> publishers = (List<Publisher>) request.getAttribute("all_publishers");
                if (publishers != null && !publishers.isEmpty()) {
                    for (Publisher publisher : publishers) {
            %>
                        <div class="publisher-card">
                            <h3 class="publisher-name">
                                <a href="<%= request.getContextPath() %>/publisher/<%= publisher.getPublisherId() %>">
                                    <%= publisher.getPublisherName() %>
                                </a>
                            </h3>
                            <p class="publisher-phone"><strong>Phone:</strong> <%= publisher.getPhone() %></p>
                            <p class="publisher-address"><strong>Address:</strong> <%= publisher.getAddress() %></p>
                        </div>
            <%
                    }
                } else {
            %>
                <p class="no-publishers-message">No publishers found.</p>
            <%
                }
            %>
        </div>
    </div>
    <%@ include file="/html/footer.html" %>
</body>
</html>