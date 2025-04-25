document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.querySelector("#login-form");
    const signupForm = document.querySelector("#signup-form");
  
    if (loginForm) {
      loginForm.addEventListener("submit", function (e) {
        const email = loginForm.querySelector("input[name='email']").value.trim();
        const password = loginForm.querySelector("input[name='password']").value.trim();
  
        if (!email || !password) {
          alert("All fields are required.");
          e.preventDefault();
          return;
        }
  
        if (!validateEmail(email)) {
          alert("Please enter a valid email address.");
          e.preventDefault();
          return;
        }
  
        if (password.length < 8) {
          alert("Password must be at least 8 characters.");
          e.preventDefault();
          return;
        }
      });
    }
  
    if (signupForm) {
      signupForm.addEventListener("submit", function (e) {
        const username = signupForm.querySelector("input[name='username']").value.trim();
        const email = signupForm.querySelector("input[name='email']").value.trim();
        const password = signupForm.querySelector("input[name='password']").value.trim();
        const firstName = signupForm.querySelector("input[name='firstName']").value.trim();
        const lastName = signupForm.querySelector("input[name='lastName']").value.trim();
        const phone = signupForm.querySelector("input[name='phone']").value.trim();
  
        if (!username || !email || !password || !firstName || !lastName || !phone) {
          alert("All fields are required.");
          e.preventDefault();
          return;
        }
  
        if (!validateEmail(email)) {
          alert("Invalid email format.");
          e.preventDefault();
          return;
        }
  
        if (password.length < 8) {
          alert("Password must be at least 8 characters long.");
          e.preventDefault();
          return;
        }
  
        if (!validatePhone(phone)) {
          alert("Invalid phone number format. Use +1234567890");
          e.preventDefault();
          return;
        }
      });
    }
  
    function validateEmail(email) {
      const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return regex.test(email);
    }
  
    function validatePhone(phone) {
      const regex = /^\+?\d{7,20}$/;
      return regex.test(phone);
    }
  });
  