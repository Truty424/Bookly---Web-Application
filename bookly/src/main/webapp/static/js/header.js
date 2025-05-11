document.addEventListener("DOMContentLoaded", () => {
  const toggleBtn = document.querySelector(".nav-toggle");
  const navMain = document.querySelector(".nav-main");

  toggleBtn.addEventListener("click", () => {
    navMain.classList.toggle("open");
  });
});
