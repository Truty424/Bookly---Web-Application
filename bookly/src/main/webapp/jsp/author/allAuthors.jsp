<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>All Authors</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <%@ include file="/html/cdn.html" %>
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
      href="${pageContext.request.contextPath}/static/css/pages/allAuthors.css"
      type="text/css"
    />
  </head>
  <body>
    <%@ include file="/html/header.html" %>
    <main class="container">
      <h1 class="page-title">Book Authors</h1>
      <div class="authors-grid">
        <c:choose>
          <c:when test="${not empty all_authors}">
            <c:forEach var="author" items="${all_authors}">
              <div class="author-card">
                <h3 class="author-name">
                  <a
                    href="${pageContext.request.contextPath}/author/${author.authorId}"
                  >
                    ${author.name}
                  </a>
                </h3>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <p class="no-authors">No authors found.</p>
          </c:otherwise>
        </c:choose>
      </div>
    </main>
    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
