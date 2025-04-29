<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipd.bookly.Resource.Publisher" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Publishers</title>
    <%@ include file="/html/cdn.html" %> 
</head>
<body>
    <%@ include file="/html/header.html" %>
    <h1>All Publishers</h1>
    <ul>
        <%
            List<Publisher> publishers = (List<Publisher>) request.getAttribute("all_publishers");
            if (publishers != null && !publishers.isEmpty()) {
                for (Publisher publisher : publishers) {
        %>
                    <li>
                        <a href="<%= request.getContextPath() %>/publisher/<%= publisher.getPublisherId() %>">
                            <%= publisher.getPublisherName() %>
                        </a>
                    </li>
        <%
                }
            } else {
        %>
                <li>No publishers found.</li>
        <%
            }
        %>
    </ul>

    <%@ include file="/html/footer.html" %>
</body>
</html>
