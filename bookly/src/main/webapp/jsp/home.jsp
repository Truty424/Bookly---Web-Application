<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unipd.bookly.Resource.Book" %>
<%@ page import="it.unipd.bookly.Resource.Category" %>
<%@ page import="java.util.List" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <title>Home - Bookly</title>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <!-- Base styles -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/root.css" type="text/css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base/globals.css" type="text/css" />

  <!-- Components -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/components/forms.css" type="text/css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/components/button.css" type="text/css" />

  <!-- Page-specific -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/pages/home.css" type="text/css" />
</head>

<body>
  <%@ include file="/html/cdn.html" %>
  <%@ include file="/html/header.html" %>

  <div class="home-container">
    <h1>Welcome to Bookly</h1>

    <c:choose>
      <c:when test="${not empty sessionScope.user}">
        <p>Welcome, <strong>${sessionScope.user.firstName}</strong>!</p>
        <form action="${pageContext.request.contextPath}/user/profile" method="get" style="display: inline;">
          <button type="submit" class="btn btn-primary">My Profile</button>
        </form>
      </c:when>
    </c:choose>

    <!-- Navigation Buttons -->
    <div class="home-buttons">
      <form action="${pageContext.request.contextPath}/category" method="get">
        <button type="submit" class="btn btn-primary">View All Categories</button>
      </form>

      <form action="${pageContext.request.contextPath}/book" method="get">
        <button type="submit" class="btn btn-primary">View All Books</button>
      </form>

      <form action="${pageContext.request.contextPath}/cart" method="get">
        <button type="submit" class="btn btn-primary">View Cart</button>
      </form>
    </div>

    <!-- Top Rated Books Section -->
    <div class="home-card">
      <h2>Top Rated Books (4+ stars)</h2>

      <div class="book-grid">
        <c:forEach var="book" items="${topRatedBooks}">
          <div class="book-card">
            <img src="${pageContext.request.contextPath}/load-book-img?bookId=${book.bookId}" alt="Cover of ${book.title}" class="book-cover" />
            <h3 class="book-title"><a href="${pageContext.request.contextPath}/book/${book.bookId}">${book.title}</a></h3>
            <p>Rating: ${book.average_rate}</p>
            <p>Price: €${book.price}</p>
            <form action="${pageContext.request.contextPath}/book/${book.bookId}" method="get">
              <button type="submit" class="btn btn-primary">View Details</button>
            </form>
          </div>
        </c:forEach>
      </div>
    </div>

    <!-- Categories and their Books -->
    <c:forEach var="entry" items="${booksByCategory}">
        <div class="home-category">
            <h2 class="category-title">${categoryMap[entry.key].category_name}</h2>

            <div class="book-grid">
                <c:forEach var="book" items="${entry.value}">
                    <div class="book-card">
                        <img src="${pageContext.request.contextPath}/load-book-img?bookId=${book.bookId}" alt="Cover of ${book.title}" class="book-cover" />

                        <h3 class="book-title">
                            <a href="${pageContext.request.contextPath}/book/${book.bookId}">
                                ${book.title}
                            </a>
                        </h3>

                        <p>Rating: ${book.average_rate}</p>
                        <p>Price: €${book.price}</p>

                        <form action="${pageContext.request.contextPath}/book/${book.bookId}" method="get">
                            <button type="submit" class="btn btn-primary">View Details</button>
                        </form>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:forEach>


  </div>

  <%@ include file="/html/footer.html" %>
</body>
</html>
