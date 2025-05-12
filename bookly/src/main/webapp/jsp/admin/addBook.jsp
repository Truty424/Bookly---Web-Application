<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Add New Book</title>
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
    <div class="container py-5">
      <div class="mb-4 d-flex justify-content-between align-items-center">
        <h2 class="fw-bold text-dark">Add Book</h2>
        <a
          href="${pageContext.request.contextPath}/admin/dashboard"
          class="btn btn-light border btn-custom"
          >Back to Dashboard</a
        >
      </div>

      <form
        action="${pageContext.request.contextPath}/admin/addBook"
        method="post"
      >
        <div class="row g-3">
          <div class="col-md-6">
            <label for="title" class="form-label">Title</label>
            <input
              type="text"
              class="form-control rounded-3"
              id="title"
              name="title"
              required
              placeholder="Enter book title"
            />
          </div>

          <div class="col-md-6">
            <label for="language" class="form-label">Language</label>
            <input
              type="text"
              class="form-control rounded-3"
              id="language"
              name="language"
              required
              placeholder="Enter language"
            />
          </div>

          <div class="col-md-6">
            <label for="isbn" class="form-label">ISBN</label>
            <input
              type="text"
              class="form-control rounded-3"
              id="isbn"
              name="isbn"
              required
              placeholder="Enter ISBN number"
            />
          </div>

          <div class="col-md-6">
            <label for="price" class="form-label">Price (€)</label>
            <input
              type="number"
              class="form-control rounded-3"
              id="price"
              name="price"
              step="0.01"
              required
              placeholder="Enter price"
            />
          </div>

          <div class="col-md-6">
            <label for="edition" class="form-label"
              >Edition <small class="text-muted">(optional)</small></label
            >
            <input
              type="text"
              class="form-control rounded-3"
              id="edition"
              name="edition"
              placeholder="Enter edition"
            />
          </div>

          <div class="col-md-6">
            <label for="publication_year" class="form-label"
              >Publication Year</label
            >
            <input
              type="number"
              class="form-control rounded-3"
              id="publication_year"
              name="publication_year"
              required
              placeholder="Enter publication year"
            />
          </div>

          <div class="col-md-6">
            <label for="number_of_pages" class="form-label"
              >Number of Pages</label
            >
            <input
              type="number"
              class="form-control rounded-3"
              id="number_of_pages"
              name="number_of_pages"
              required
              placeholder="Enter number of pages"
            />
          </div>

          <div class="col-md-6">
            <label for="stock_quantity" class="form-label"
              >Stock Quantity</label
            >
            <input
              type="number"
              class="form-control rounded-3"
              id="stock_quantity"
              name="stock_quantity"
              required
              placeholder="Enter stock quantity"
            />
          </div>

          <div class="col-md-12">
            <label for="summary" class="form-label"
              >Summary <small class="text-muted">(optional)</small></label
            >
            <textarea
              class="form-control rounded-3"
              id="summary"
              name="summary"
              rows="4"
              placeholder="Write a short summary..."
            ></textarea>
          </div>
        </div>

        <div class="mt-4">
          <button type="submit" class="btn-add btn-custom">Add Book</button>
        </div>
      </form>
    </div>
    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
