<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Add Discount</title>
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
        <h2 class="fw-bold text-dark">Create Discount Code</h2>
        <a
          href="${pageContext.request.contextPath}/admin/dashboard"
          class="btn btn-light border btn-custom"
        >
          Back to Dashboard
        </a>
      </div>

      <form
        action="${pageContext.request.contextPath}/admin/addDiscount"
        method="post"
      >
        <div class="row g-3">
          <div class="col-md-6">
            <label for="code" class="form-label">Discount Code</label>
            <input
              type="text"
              class="form-control rounded-3"
              id="code"
              name="code"
              required
              placeholder="Enter discount code"
            />
          </div>

          <div class="col-md-6">
            <label for="percentage" class="form-label">Percentage (%)</label>
            <input
              type="number"
              class="form-control rounded-3"
              id="percentage"
              name="percentage"
              step="0.01"
              min="0"
              max="100"
              required
              placeholder="Enter discount percentage"
            />
          </div>

          <div class="col-md-6">
            <label for="expiredDate" class="form-label">Expiration Date</label>
            <input
              type="date"
              class="form-control rounded-3"
              id="expiredDate"
              name="expiredDate"
              required
            />
          </div>
        </div>

        <div class="mt-4">
          <button type="submit" class="btn-add btn-custom">
            Add Discount
          </button>
        </div>
      </form>
    </div>
    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
