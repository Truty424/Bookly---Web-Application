<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <title>About Us</title>
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
      href="${pageContext.request.contextPath}/static/css/pages/aboutUs.css"
      type="text/css"
    />
  </head>
  <body>
    <%@ include file="/html/header.html" %>
    <div class="container py-5">
      <div>
        <h1 class="page-title">About Us</h1>
        <p>
          Welcome to Bookly! We are a team of four students from the University
          of Padua, brought together by a shared passion for literature and
          digital innovation. This website was created as part of our Web
          Application course project, where we aimed to design a functional and
          user-friendly platform for discovering and purchasing books online.
        </p>
        <p>
          At Bookly, we believe in the power of stories to inspire, educate, and
          entertain. Our mission is to connect readers with books they'll love,
          through a platform that is simple, modern, and made with care.
        </p>
        <p>Thank you for being a part of our journey!</p>

        <h2 class="team-title">Meet the Team</h2>
        <div class="team-grid">
          <div class="team-card">
            <img
              src="../static/img/team/aby.jpg"
              alt="ayoub ben yamoune"
              class="team-image"
            />
            <h3 class="team-name">Ben Yamoune Ayoub</h3>
            <a
              href="https://www.linkedin.com/in/ayoub-ben-yamoune-1015b3250/"
              target="_blank"
              class="team-link"
            >
              <i class="fa-brands fa-linkedin"></i>
            </a>
          </div>
          <div class="team-card">
            <img
              src="../static/img/team/amirjm.jpg"
              alt="amir jm"
              class="team-image"
            />
            <h3 class="team-name">Jolani Mameghani Amirreza</h3>
            <a
              href="https://www.linkedin.com/in/amirjm"
              target="_blank"
              class="team-link"
            >
              <i class="fa-brands fa-linkedin"></i>
            </a>
          </div>
          <div class="team-card">
            <img
              src="../static/img/team/pari.jpg"
              alt="Parisa Saeedidana"
              class="team-image"
            />
            <h3 class="team-name">Saeedidana Parisa</h3>
            <a
              href="https://www.linkedin.com/in/parisa-saeedidana-067833201/"
              target="_blank"
              class="team-link"
            >
              <i class="fa-brands fa-linkedin"></i>
            </a>
          </div>
          <div class="team-card">
            <img
              src="../static/img/team/adatru.jpg"
              alt="Adam Truty"
              class="team-image"
            />
            <h3 class="team-name">Truty Adam</h3>
            <a
              href="https://www.linkedin.com/in/adam-truty-46b5bb269/"
              target="_blank"
              class="team-link"
            >
              <i class="fa-brands fa-linkedin"></i>
            </a>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/html/footer.html" %>
    <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/header.js"></script>
  </body>
</html>
