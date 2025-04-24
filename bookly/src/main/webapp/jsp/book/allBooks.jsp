<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>All Books</title>
    <link rel="stylesheet" type="text/css" href="../static/css/base/root.css" />
    <link
      rel="stylesheet"
      type="text/css"
      href="../static/css/base/globals.css"
    />
    <link
      rel="stylesheet"
      type="text/css"
      href="../static/css/components/footer.css"
    /> 
</head>
<body>
    <h1>All Books</h1>

    <ul>

    <form action="<%= request.getContextPath() %>/" method="get">
        <button type="submit">Go to Home</button>
    </form>

        <%
            List<Book> books = (List<Book>) request.getAttribute("all_books");
            if (books != null) {
                for (Book book : books) {
        %>
                    <li>
                        <a href="<%= request.getContextPath() %>/book/<%= book.getBookId() %>">
                            <%= book.getTitle() %>
                        </a>
                    </li>
        <%
                }
            } else {
        %>
            <li>No books found.</li>
        <%
            }
        %>
    </ul>
</body>
</html>
