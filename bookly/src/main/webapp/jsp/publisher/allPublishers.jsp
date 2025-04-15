<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipd.bookly.Resource.Publisher" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Publishers</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }
        h1 {
            color: #333;
        }
        ul {
            list-style-type: none;
            padding-left: 0;
        }
        li {
            margin: 10px 0;
        }
        a {
            text-decoration: none;
            color: #007bff;
            font-size: 18px;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
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
</body>
</html>
