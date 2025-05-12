<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Add Publisher</title>
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
        <h2 class="fw-bold text-dark">Add New Publisher</h2>
        <a
          href="${pageContext.request.contextPath}/admin/dashboard"
          class="btn btn-light border btn-custom"
        >
          Back to Dashboard
        </a>
      </div>

      <form
        action="${pageContext.request.contextPath}/admin/addPublisher"
        method="post"
      >
        <div class="row g-3">
          <div class="col-md-6">
            <label for="publisher_name" class="form-label"
              >Publisher Name</label
            >
            <input
              type="text"
              class="form-control rounded-3"
              id="publisher_name"
              name="publisher_name"
              required
              placeholder="Enter publisher name"
            />
          </div>

          <div class="col-md-6">
            <label for="phone" class="form-label"
              >Phone <small class="text-muted">(optional)</small></label
            >
            <input
              type="text"
              class="form-control rounded-3"
              id="phone"
              name="phone"
              placeholder="Enter phone number"
            />
          </div>

          <div class="col-md-12">
            <label for="address" class="form-label"
              >Address <small class="text-muted">(optional)</small></label
            >
            <textarea
              class="form-control rounded-3"
              id="address"
              name="address"
              rows="3"
              placeholder="Enter address"
            ></textarea>
          </div>
        </div>

        <div class="mt-4">
          <button type="submit" class="btn-add btn-custom">
            Add Publisher
          </button>
        </div>
      </form>
    </div>
    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
