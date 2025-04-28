<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> <%@ page
contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="it.unipd.bookly.Resource.Book" %> <%@ page
import="it.unipd.bookly.Resource.Category" %> <%@ page import="java.util.List"
%> <%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Home - Bookly</title>
    <link rel="stylesheet" type="text/css" href="../static/css/base/root.css" />
    <link
      rel="stylesheet"
      type="text/css"
      href="../static/css/base/globals.css"
    />
  </head>
  <body>
    <%@ include file="/html/cdn.html" %> 
    <%@ include file="/html/header.html" %>
    <h1>Welcome to Bookly</h1>

    <c:choose>
      <c:when test="${not empty sessionScope.user}">
        <p>Welcome, <strong>${sessionScope.user.firstName}</strong>!</p>
        <form
          action="${pageContext.request.contextPath}/user/profile"
          method="get"
          style="display: inline"
        >
          <button type="submit">My Profile</button>
        </form>
      </c:when>
    </c:choose>

    <!-- Go to All Categories -->
    <form action="${pageContext.request.contextPath}/category" method="get">
      <button type="submit">View All Categories</button>
    </form>

    <!-- View All Books -->
    <form action="${pageContext.request.contextPath}/book" method="get">
      <button type="submit">View All Books</button>
    </form>

    <!-- View Cart -->
    <form action="${pageContext.request.contextPath}/cart" method="get">
      <button type="submit">View Cart</button>
    </form>

    <!-- Top Rated Books -->
    <h2>Top Rated Books (4+ stars)</h2>
    <c:forEach var="book" items="${allBooks}">
      <div>
        <h3>
          <a href="${pageContext.request.contextPath}/book/${book.bookId}">
            ${book.title}
          </a>
        </h3>

        <p>
          <strong>Author(s):</strong>
          <c:choose>
            <c:when test="${not empty bookAuthors[book.bookId]}">
              <c:forEach
                var="author"
                items="${bookAuthors[book.bookId]}"
                varStatus="loop"
              >
                ${author.firstName} ${author.lastName}<c:if test="${!loop.last}"
                  >,
                </c:if>
              </c:forEach>
            </c:when>
            <c:otherwise>Unknown</c:otherwise>
          </c:choose>
        </p>

        <p>Rating: ${book.average_rate}</p>
        <p>Price: €${book.price}</p>

        <form
          action="${pageContext.request.contextPath}/book/${book.bookId}"
          method="get"
        >
          <button type="submit">View Details</button>
        </form>
      </div>
      <hr />
    </c:forEach>

    <!-- All Books -->
    <h2>All Books</h2>
    <c:forEach var="book" items="${allBooks}">
      <div>
        <h3>${book.title}</h3>
        <p>
          <strong>Author(s):</strong>
          <c:choose>
            <c:when test="${not empty bookAuthors[book.bookId]}">
              <c:forEach
                var="author"
                items="${bookAuthors[book.bookId]}"
                varStatus="loop"
              >
                ${author.firstName} ${author.lastName}<c:if test="${!loop.last}"
                  >,
                </c:if>
              </c:forEach>
            </c:when>
            <c:otherwise> Unknown </c:otherwise>
          </c:choose>
        </p>
        <p>Rating: ${book.average_rate}</p>
        <p>Price: €${book.price}</p>
      </div>
      <hr />
    </c:forEach>

    <%@ include file="/html/footer.html" %>
  </body>
</html>
