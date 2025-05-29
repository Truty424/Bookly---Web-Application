<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unipd.bookly.Resource.Publisher" %>

<html>
<head>
    <meta charset="UTF-8" />
    <title>Manage Publishers</title>
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
          <!-- Title and Action Buttons -->
          <div class="row my-3">
            <div class="col-md-6">
              <h2 class="fw-bold text-dark mb-0">Manage Publishers</h2>
            </div>
            <div class="col-md-6 text-md-end mt-3 mt-md-0 action-buttons">
              <a
                href="<%= request.getContextPath() %>/admin/addPublisher"
                class="btn-add btn-custom"
              >
                + Add Publisher
              </a>
              <a
                href="<%= request.getContextPath() %>/admin/dashboard"
                class="btn btn-light border btn-custom"
              >
                Back to Dashboard
              </a>
            </div>
          </div>
      
          <!-- Search and Filter -->
          <div class="row g-2 mb-2 search-filter">
            <div class="col-md-6">
              <input
                id="searchInput"
                type="text"
                class="form-control rounded-3"
                placeholder="Search for publishers..."
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
      
          <!-- Publishers Table -->
          <%
            List<Publisher> publishers = (List<Publisher>) request.getAttribute("publishers");
            if (publishers != null && !publishers.isEmpty()) {
          %>
          <div class="container p-0 table-responsive">
            <table id="publishersTable" class="table table-striped table-hover">
              <thead class="table-dark">
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Phone</th>
                  <th>Address</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody id="tableBody">
                <% for (Publisher publisher : publishers) { %>
                <tr>
                  <td><%= publisher.getPublisherId() %></td>
                  <td><%= publisher.getPublisherName() %></td>
                  <td><%= publisher.getPhone() %></td>
                  <td><%= publisher.getAddress() %></td>
                  <td>
                    <form
                      action="<%= request.getContextPath() %>/admin/deletePublisher"
                      method="post"
                      onsubmit="return confirm('Are you sure you want to delete?');"
                    >
                      <input
                        type="hidden"
                        name="publisher_id"
                        value="<%= publisher.getPublisherId() %>"
                      />
                      <button type="submit" class="btn-custom btn-delete">
                        Delete
                      </button>
                    </form>
                  </td>
                </tr>
                <% } %>
              </tbody>
            </table>
          </div>
      
          <!-- Pagination -->
          <nav>
            <ul id="pagination" class="pagination justify-content-center"></ul>
          </nav>
          <% } else { %>
            <p class="text-muted">No publishers found.</p>
          <% } %>
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
