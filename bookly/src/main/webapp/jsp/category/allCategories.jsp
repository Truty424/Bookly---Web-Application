<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>All Categories</title>
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
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/allCategories.css"
      type="text/css"
    />
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <%@ include file="/html/header.html" %>
    <main class="container">
      <div class="categories-grid">
        <c:choose>
          <c:when test="${not empty all_categories}">
            <c:forEach var="category" items="${all_categories}">
              <div class="category-card">
                <img
                  src="${pageContext.request.contextPath}/static/img/categories/${category.category_id}.png"
                  alt="${category.category_name}"
                  class="category-image"
                  onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/static/img/categories/default.jpg';"
                />
                <h3 class="category-title">
                  <a
                    href="${pageContext.request.contextPath}/category/${category.category_id}"
                    class="category-link"
                  >
                    ${category.category_name}
                  </a>
                </h3>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <p class="no-categories">No categories found.</p>
          </c:otherwise>
        </c:choose>
      </div>
    </main>
    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
