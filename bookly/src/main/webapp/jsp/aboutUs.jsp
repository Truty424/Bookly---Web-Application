<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>About Us</title>
    <%@ include file="/html/cdn.html" %>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/root.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/base/globals.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/components/header.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/components/footer.css" type="text/css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/pages/aboutUs.css" type="text/css" />
</head>
<body>
    <%@ include file="/html/header.html" %>
    <div class="container py-5">
        <h1 class="page-title">About Us</h1>
        <p>Welcome to Bookly! We are a team of four students from the University of Padua, brought together by a shared passion for literature and digital innovation. This website was created as part of our Web Application course project, where we aimed to design a functional and user-friendly platform for discovering and purchasing books online.</p>
        <p>At Bookly, we believe in the power of stories to inspire, educate, and entertain. Our mission is to connect readers with books they'll love, through a platform that is simple, modern, and made with care.</p>
        <p>Thank you for being a part of our journey!</p>

        <h2 class="team-title">Meet the Team</h2>
        <div class="team-grid">
            <div class="team-card">
                <div class="team-image-placeholder">Image</div>
                <h3 class="team-name">Ben Yamoune Ayoub</h3>
                <a href="https://www.linkedin.com/in/ayoub-ben-yamoune-1015b3250/" target="_blank" class="team-link"> <i class="fa-brands fa-linkedin"></i> </a>
            </div>
            <div class="team-card">
                <div class="team-image-placeholder">Image</div>
                <h3 class="team-name">Jolani Mameghani Amirreza</h3>
                <a href="https://www.linkedin.com/" target="_blank" class="team-link"> <i class="fa-brands fa-linkedin"></i> </a>
            </div>
            <div class="team-card">
                <div class="team-image-placeholder">Image</div>
                <h3 class="team-name">Saeedidana Parisa</h3>
                <a href="https://www.linkedin.com/" target="_blank" class="team-link"> <i class="fa-brands fa-linkedin"></i> </a>
            </div>
            <div class="team-card">
                <div class="team-image-placeholder">Image</div>
                <h3 class="team-name">Truty Adam</h3>
                <a href="https://www.linkedin.com/" target="_blank" class="team-link"> <i class="fa-brands fa-linkedin"></i> </a>
            </div>
        </div>
    </div>    
    <%@ include file="/html/footer.html" %>
</body>
</html>