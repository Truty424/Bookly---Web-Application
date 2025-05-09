<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="it.unipd.bookly.Resource.Author" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<html>
<head>
    <title>Search Results</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/results.css" />
    <%@ include file="/html/cdn.html" %>
</head>
<body>

<%@ include file="/html/header.html" %>

<%
    String query = (String) request.getAttribute("search_query");
    List<Book> books = (List<Book>) request.getAttribute("search_results");
    Map<Integer, List<Author>> authorsMap = (Map<Integer, List<Author>>) request.getAttribute("authors_map");
%>

<div class="search-container">
    <h1>Search Results</h1>
    <div class="search-query">Showing results for: "<%= query %>"</div>

    <% if (books != null && !books.isEmpty()) { %>
        <ul class="book-list">
            <% for (Book book : books) { %>
                <li class="book-item">
                    <div class="book-image">
                        <img
                          src="<%= request.getContextPath() %>/static/img/book/<%= book.getBookId() %>.jpg"
                          alt="Cover of <%= book.getTitle() %>"
                          onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/static/img/book/default.jpg';"
                        />
                    </div>
                    <div class="book-details">
                        <h3>
                            <a href="<%= request.getContextPath() %>/book/<%= book.getBookId() %>"><%= book.getTitle() %></a>
                        </h3>
                        <p><strong>Author(s):</strong>
                            <%
                                List<Author> authors = authorsMap.get(book.getBookId());
                                if (authors != null && !authors.isEmpty()) {
                                    for (int i = 0; i < authors.size(); i++) {
                                        Author a = authors.get(i);
                                        out.print(a.getFirstName() + " " + a.getLastName());
                                        if (i < authors.size() - 1) out.print(", ");
                                    }
                                } else {
                                    out.print("Unknown");
                                }
                            %>
                        </p>
                        <p><strong>Rate:</strong> <%= book.getAverage_rate() %></p>
                        <p><strong>Price:</strong> €<%= String.format("%.2f", book.getPrice()) %></p>
                        <form action="<%= request.getContextPath() %>/book/<%= book.getBookId() %>" method="get">
                            <button type="submit" class="btn btn-primary">View Details</button>
                        </form>
                    </div>
                </li>
            <% } %>
        </ul>
    <% } else { %>
        <p class="no-results">No results found for your search.</p>
    <% } %>

    <div style="text-align: center; margin-top: 40px;">
      <form action="${pageContext.request.contextPath}/" method="get">
        <button type="submit" class="btn btn-primary">
          <i class="fas fa-home"></i> Home Page
        </button>
      </form>
    </div>
</div>

<%@ include file="/html/footer.html" %>
</body>
</html>