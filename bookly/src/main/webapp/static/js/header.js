document.addEventListener("DOMContentLoaded", () => {
  const toggleBtn = document.querySelector(".nav-toggle");
  const navMain = document.querySelector(".nav-main");
  const currentPath = window.location.pathname;
  const navLinks = document.querySelectorAll(".nav-links a");

  toggleBtn.addEventListener("click", () => {
    navMain.classList.toggle("open");
  });
  navLinks.forEach((link) => {
    const linkPath = link.getAttribute("href");

    if (currentPath === linkPath || currentPath.startsWith(linkPath + "/")) {
      link.classList.add("active");
    } else {
      link.classList.remove("active");
    }
  });
});
