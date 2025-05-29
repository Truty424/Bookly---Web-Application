<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <!DOCTYPE html>
  <html lang="en">
  <meta charset="UTF-8" />
  <title>Add Author</title>
  <!-- Base styles -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" type="text/css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" type="text/css" />

  <!-- Components -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/components/table.css" type="text/css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/components/forms.css" type="text/css" />

  <!-- Page-specific -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/manageAdmin.css" type="text/css" />
  <%@ include file="/html/cdn.html" %>
    </head>

    <body>
      <div class="container py-5">
        <div class="mb-4 d-flex justify-content-between align-items-center">
          <h2 class="fw-bold text-dark">Add New Author</h2>
          <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-light border btn-custom">Back to
            Dashboard</a>
        </div>

        <form action="${pageContext.request.contextPath}/admin/addAuthor" method="post">
          <div class="row g-3">
            <div class="col-md-6">
              <label for="firstName" class="form-label">First Name</label>
              <input type="text" class="form-control rounded-3" id="firstName" name="firstName" required
                placeholder="Enter first name" />
            </div>
            <div class="col-md-6">
              <label for="lastName" class="form-label">Last Name</label>
              <input type="text" class="form-control rounded-3" id="lastName" name="lastName" required
                placeholder="Enter last name" />
            </div>
            <div class="col-md-6">
              <label for="nationality" class="form-label">Nationality</label>
              <input type="text" class="form-control rounded-3" id="nationality" name="nationality" required
                placeholder="Enter nationality" />
            </div>
            <div class="col-md-12">
              <label for="biography" class="form-label">Biography <small class="text-muted">(optional)</small></label>
              <textarea class="form-control rounded-3" id="biography" name="biography" rows="4"
                placeholder="Write a short biography..."></textarea>
            </div>
          </div>
          <div class="mt-4">
            <button type="submit" class="btn-add btn-custom">
              Add Author
            </button>
          </div>
        </form>
      </div>

      <!-- Bootstrap 5 JS Bundle -->
      <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
      <script src="${pageContext.request.contextPath}/static/js/validate-author.js"></script>
    </body>

  </html>