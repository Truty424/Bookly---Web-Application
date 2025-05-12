<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="java.util.List" %> <%@ page import="it.unipd.bookly.resource.Author" %>
<% List<Author>
  authors = (List<Author
    >) request.getAttribute("authors"); %>
    <html>
      <head>
        <link
          rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/pages/list-authors.css"
        />
        <title>Author List</title>
      </head>
      <body>
        <main class="authors-container">
          <h2>All Authors</h2>
          <table border="1">
            <tr>
              <th>ID</th>
              <th>Name</th>
            </tr>
            <% if (authors != null) { for (Author a : authors) { %>
            <tr>
              <td><%= a.getId() %></td>
              <td><%= a.getName() %></td>
            </tr>
            <% } } else { %>
            <tr>
              <td colspan="2">No authors found.</td>
            </tr>
            <% } %>
          </table>
        </main>
        <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
      </body>
    </html> </Author
></Author>
