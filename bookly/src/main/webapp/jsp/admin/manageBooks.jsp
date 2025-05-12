<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Manage Books</title>
    <!-- Base styles -->
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

    <!-- Components -->
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/components/table.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/components/forms.css"
      type="text/css"
    />

    <!-- Page-specific -->
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/manageAdmin.css"
      type="text/css"
    />
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <div class="container mb-4">
      <div>
        <div class="row my-3">
          <div class="col-md-6">
            <h2 class="fw-bold text-dark mb-0">Manage Books</h2>
          </div>
          <div class="col-md-6 text-md-end mt-3 mt-md-0 action-buttons">
            <a
              href="${pageContext.request.contextPath}/jsp/admin/addBook.jsp"
              class="btn-add btn-custom"
            >
              + Add Book
            </a>
            <a
              href="${pageContext.request.contextPath}/admin/dashboard"
              class="btn btn-light border btn-custom"
            >
              Back to Dashboard
            </a>
          </div>
        </div>
        <div class="row g-2 mb-2 search-filter">
          <div class="col-md-6">
            <input
              id="searchInput"
              type="text"
              class="form-control rounded-3"
              placeholder="Search for books..."
            />
          </div>
          <div class="col-md-5 text-md-end p-0">
            <select
              id="rowsPerPage"
              class="form-select rounded-3 w-auto d-inline-block"
            >
              <option value="5">5 per page</option>
              <option value="10" selected>10 per page</option>
              <option value="20">20 per page</option>
            </select>
          </div>
        </div>

        <c:if test="${not empty books}">
          <div class="container p-0 table-responsive">
            <table id="booksTable" class="table table-striped table-hover">
              <thead class="table-dark">
                <tr>
                  <th>ID</th>
                  <th>Title</th>
                  <th>ISBN</th>
                  <th>Price (€)</th>
                  <th>Stock</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody id="tableBody">
                <c:forEach var="book" items="${books}">
                  <tr>
                    <td>${book.bookId}</td>
                    <td>${book.title}</td>
                    <td>${book.isbn}</td>
                    <td>${book.price}</td>
                    <td>${book.stockQuantity}</td>
                    <td>
                      <form
                        action="${pageContext.request.contextPath}/admin/deleteBook"
                        method="post"
                        onsubmit="return confirm('Are you sure you want to delete?');"
                      >
                        <input
                          type="hidden"
                          name="book_id"
                          value="${book.bookId}"
                        />
                        <button class="btn-custom btn-delete">Delete</button>
                      </form>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
          <nav>
            <ul id="pagination" class="pagination justify-content-center"></ul>
          </nav>
        </c:if>

        <c:if test="${empty books}">
          <p class="text-muted">No books found.</p>
        </c:if>
      </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Custom JavaScript for Pagination, Search, Toast -->
    <script src="${pageContext.request.contextPath}/static/js/table-pagination.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/search-table.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
