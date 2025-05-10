<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
  <head>
    <title>Search Results</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta charset="ISO-8859-1" />
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
      href="${pageContext.request.contextPath}/static/css/pages/results.css"
    />
    <%@ include file="/html/cdn.html" %>
  </head>
  <body>
    <%@ include file="/html/header.html" %>

    <div class="search-container">
      <div class="d-flex justify-content-between align-items-center">
        <h2>Search Results</h2>
        <div>
          <form action="${pageContext.request.contextPath}/" method="get">
            <button type="submit" class="back-btn">
              <i class="fas fa-home"></i> Home Page
            </button>
          </form>
        </div>
      </div>
      <div class="search-query">
        Showing results for: "<c:out value="${search_query}" />"
      </div>

      <c:choose>
        <c:when test="${not empty search_results}">
          <ul class="book-list">
            <c:forEach var="book" items="${search_results}">
              <li class="book-item">
                <div class="book-image">
                  <img
                    src="${pageContext.request.contextPath}/static/img/book/${book.bookId}.jpg"
                    alt="Cover of ${book.title}"
                    onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/static/img/book/default.jpg';"
                  />
                </div>
                <div class="book-details">
                  <h3>
                    <a
                      href="${pageContext.request.contextPath}/book/${book.bookId}"
                    >
                      ${book.title}
                    </a>
                  </h3>
                  <p>
                    <strong>Author(s):</strong>
                    <c:choose>
                      <c:when test="${not empty authors_map[book.bookId]}">
                        <c:forEach
                          var="author"
                          items="${authors_map[book.bookId]}"
                          varStatus="loop"
                        >
                          ${author.firstName} ${author.lastName}<c:if
                            test="${!loop.last}"
                            >,
                          </c:if>
                        </c:forEach>
                      </c:when>
                      <c:otherwise>Unknown</c:otherwise>
                    </c:choose>
                  </p>
                  <p><strong>Rate:</strong> ${book.average_rate}</p>
                  <p>
                    <strong>Price:</strong> €<fmt:formatNumber
                      value="${book.price}"
                      type="currency"
                      currencySymbol="€"
                    />
                  </p>
                  <form
                    action="${pageContext.request.contextPath}/book/${book.bookId}"
                    method="get"
                  >
                    <button type="submit" class="btn btn-detail">
                      View Details
                    </button>
                  </form>
                </div>
              </li>
            </c:forEach>
          </ul>
        </c:when>
        <c:otherwise>
          <p class="no-results">No results found for your search.</p>
        </c:otherwise>
      </c:choose>
    </div>

    <%@ include file="/html/footer.html" %>
  </body>
</html>
