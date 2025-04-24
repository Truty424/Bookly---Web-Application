<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="java.util.List" %>
<html>
<head>
    
    <title>Search Results</title>
    <style>
        /* Basic styles for the search results */
        body {
            font-family: Arial, sans-serif;
        }

        h1 {
            color: #4CAF50;
        }

        .search-query {
            font-size: 1.2em;
            color: #333;
            margin-bottom: 20px;
        }

        .book-list {
            list-style-type: none;
            padding: 0;
        }

        .book-item {
            margin-bottom: 15px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .book-item a {
            text-decoration: none;
            color: #007bff;
            font-size: 1.1em;
        }

        .book-item a:hover {
            text-decoration: underline;
        }

        .book-item p {
            margin: 5px 0;
        }

        .no-results {
            color: red;
        }
    </style>
</head>
<body>
    <%@ include file="/html/cdn.html" %> 
    <%@ include file="/html/header.html" %>
<h1>Search Results</h1>

<%-- Display the search query entered by the user --%>
<%
    String query = (String) request.getAttribute("search_query");
    List<Book> books = (List<Book>) request.getAttribute("search_results");
%>

<%-- Display the search query --%>
<div class="search-query">
    Showing results for: "<%= query %>"
</div>

<%-- Check if any books are returned --%>
<% if (books != null && !books.isEmpty()) { %>
    <ul class="book-list">
        <%-- Loop through the list of books and display them --%>
        <% for (Book book : books) { %>
            <li class="book-item">
                <a href="<%= request.getContextPath() %>/book/<%= book.getBookId() %>"><%= book.getTitle() %></a>
                <p><strong>Author:</strong> <%= book.getAuthor() %></p>
                <p><strong>ISBN:</strong> <%= book.getIsbn() %></p>
                <p><strong>Price:</strong> $<%= book.getPrice() %></p>
            </li>
        <% } %>
    </ul>
<% } else { %>
    <p class="no-results">No results found for your search.</p>
<% } %>

<%-- Add a link back to the main search page --%>
<p><a href="<%= request.getContextPath() %>/search">Back to Search</a></p>
<%@ include file="/html/footer.html" %>
</body>
</html>
