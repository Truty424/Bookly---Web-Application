<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>All Publishers</title>
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
      href="${pageContext.request.contextPath}/static/css/pages/allPublishers.css"
      type="text/css"
    />

  </head>
  <body>
    <%@ include file="/html/header.html" %>
    <main class="container">
      <div class="publishers-grid">
        <c:choose>
          <c:when test="${not empty all_publishers}">
            <c:forEach var="publisher" items="${all_publishers}">
              <div class="publisher-card">
                <h3 class="publisher-name">
                  <a
                    href="${pageContext.request.contextPath}/publisher/${publisher.publisherId}"
                  >
                    ${publisher.publisherName}
                  </a>
                </h3>
                <p class="publisher-phone">
                  <strong>Phone:</strong> ${publisher.phone}
                </p>
                <p class="publisher-address">
                  <strong>Address:</strong> ${publisher.address}
                </p>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <p class="no-publishers-message">No publishers found.</p>
          </c:otherwise>
        </c:choose>
      </div>
    </main>
    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
