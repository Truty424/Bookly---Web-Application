<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> <%@ page
contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="it.unipd.bookly.Resource.Book" %> <%@ page
import="it.unipd.bookly.Resource.Category" %> <%@ page import="java.util.List"
%> <%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Home - Bookly</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

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
      href="${pageContext.request.contextPath}/static/css/components/forms.css"
      type="text/css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/components/button.css"
      type="text/css"
    />

    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/components/carousel.css"
      type="text/css"
    />

    <!-- Page-specific -->
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/static/css/pages/home.css"
      type="text/css"
    />
    <%@ include file="/html/cdn.html" %>
  </head>

  <body>
    <%@ include file="/html/header.html" %>

    <div class="home-container">
      <!-- Hero Section -->
      <section class="hero">
        <div class="container hero-grid">
          <div class="hero-text">
            <h1>A Library That's Always Open.</h1>
            <p>You may access the best collection with over +30 genres</p>
            <div class="search-container">
              <div class="search-bar-hero">
                <input
                  type="text"
                  placeholder="Search Books, Authors Or Topics"
                />
                <button class="search-btn-hero">Search</button>
              </div>
            </div>
            <div class="reviews-container">
              <div class="reviewer-avatars">
                <img
                  src="../static/img/user1.jpg"
                  alt="Reviewer 1"
                  class="reviewer-img"
                />
                <img
                  src="../static/img/user2.jpg"
                  alt="Reviewer 2"
                  class="reviewer-img"
                />
                <img
                  src="../static/img/user3.jpg"
                  alt="Reviewer 3"
                  class="reviewer-img"
                />
              </div>
              <div class="review-info">
                <p>+500 Positive Reviews</p>
                <div class="stars">★★★★★</div>
              </div>
            </div>
          </div>
          <div class="hero-image">
            <img
              src="../static/img/globe-book.png"
              alt="Open book on a globe"
            />
          </div>
        </div>
      </section>

      <!-- Features Section -->
      <section class="features">
        <div class="container">
          <div class="features-grid">
            <div class="feature-card">
              <div class="feature-image"></div>
              <h3 class="feature-title">
                The best Books to give as gift this year
              </h3>
              <p class="feature-desc">Great collections for all.</p>
            </div>
            <div class="feature-card">
              <div class="feature-image"></div>
              <h3 class="feature-title">Free Wordwide Delivery</h3>
              <p class="feature-desc">Yeah. Shop now.</p>
            </div>
            <div class="feature-card">
              <div class="feature-image"></div>
              <h3 class="feature-title">Christmas Crackers</h3>
              <p class="feature-desc">Great collections for all.</p>
            </div>
            <div class="feature-card">
              <div class="feature-image"></div>
              <h3 class="feature-title">Special Editions</h3>
              <p class="feature-desc">Great collections for all.</p>
            </div>
          </div>
        </div>
      </section>

      <!-- Book Section -->
      <section class="books-carousel-section">
        <div class="container">
          <h2 class="section-title">Top Rated Books</h2>
          <div class="carousel-wrapper">
            <button class="carousel-btn prev">  <i class="fas fa-chevron-left"></i></button>
            <div class="carousel-grid" id="carousel">
              <c:forEach var="book" items="${topRatedBooks}">
                <div class="book-item">
                  <div class="book-cover">
                    <img
                      src="${pageContext.request.contextPath}/load-book-img?bookId=${book.bookId}"
                      alt="Cover of ${book.title}"
                      class="book-cover"
                    />
                  </div>
                  <h3 class="book-title">${book.title}</h3>
                  <p class="book-author">
                    <c:forEach
                      var="author"
                      items="${bookAuthors[book.bookId]}"
                      varStatus="authorStatus"
                    >
                      ${author.name}<c:if test="${!authorStatus.last}">, </c:if>
                    </c:forEach>
                  </p>
                  <p class="book-rating">rate:${book.average_rate}</p>
                  <p class="book-price">€${book.price}</p>
                </div>
              </c:forEach>
            </div>
            <button class="carousel-btn next">
              <i class="fas fa-chevron-right"></i>
            </button>
          </div>
        </div>
      </section>

      <!-- Quote Section -->
      <section class="quote-section">
        <div class="container">
          <p class="quote-text">
            "Books are the quietest and most constant of friends; they are the
            most accessible and wisest of counselors, and the most patient of
            teachers."
          </p>
          <div class="quote-author">
            <div class="author-img"></div>
            <p class="author-name">Emma Johanssen</p>
          </div>
        </div>
      </section>

      <section class="categories-section">
        <div class="container">
          <div class="categories-header">
            <h2 class="categories-title">Categories</h2>
            <a href="#" class="view-all">View All →</a>
          </div>
          <div>
            <ul class="categories-grid">
              <c:forEach var="entry" items="${booksByCategory}">
                <li class="category-list">
                  ${categoryMap[entry.key].category_name}
                </li>
              </c:forEach>
            </ul>
          </div>
        </div>
      </section>

      <!-- Benefits Section -->
      <section class="benefits-section">
        <div class="container">
          <div class="benefits-grid">
            <div class="benefit-card">
              <div class="benefit-icon">1</div>
              <div class="benefit-content">
                <h4>Free Worldwide Delivery</h4>
                <p>On all orders over $50</p>
              </div>
            </div>
            <div class="benefit-card">
              <div class="benefit-icon">2</div>
              <div class="benefit-content">
                <h4>Easy Returns</h4>
                <p>30-day return policy</p>
              </div>
            </div>
            <div class="benefit-card">
              <div class="benefit-icon">3</div>
              <div class="benefit-content">
                <h4>10% Student Discount</h4>
                <p>With valid student ID</p>
              </div>
            </div>
            <div class="benefit-card">
              <div class="benefit-icon">4</div>
              <div class="benefit-content">
                <h4>Gift Wrapping</h4>
                <p>Available on request</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>

    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/carousel.js"></script>
  </body>
</html>
