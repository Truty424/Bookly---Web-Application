document.addEventListener("DOMContentLoaded", async () => {
  const userActions = document.getElementById("user-actions");
  const loginBtn = userActions.querySelector(".login-btn");

  try {
    const res = await fetch("/user/status", {
      headers: { "Accept": "application/json" }
    });

    if (!res.ok) throw new Error("Failed to fetch user status");

    const data = await res.json();

    if (data.loggedIn) {
      // Remove login/signup button
      if (loginBtn) {
        loginBtn.remove();
      }

      // Create avatar dropdown
      const avatar = document.createElement("div");
      avatar.classList.add("avatar-dropdown");
      avatar.innerHTML = `
        <button class="avatar-button">
          <i class="fas fa-user-circle"></i>
          <span>${data.username}</span>
        </button>
        <ul class="dropdown-menu">
          <li><a href="/user/profile">Profile</a></li>
          <li><a href="/user/logout">Logout</a></li>
        </ul>
      `;

      userActions.appendChild(avatar);

      // Dropdown toggle
      avatar.querySelector(".avatar-button").addEventListener("click", (e) => {
        e.preventDefault();
        avatar.classList.toggle("open");
      });

      // Close dropdown if clicked outside
      document.addEventListener("click", (e) => {
        if (!avatar.contains(e.target)) {
          avatar.classList.remove("open");
        }
      });
    }
  } catch (err) {
    console.error("Error checking login status:", err);
  }
});
