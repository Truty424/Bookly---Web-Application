document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector("form");

  const fields = [
    {
      input: document.getElementById("firstName"),
      message: "First name must only contain letters."
    },
    {
      input: document.getElementById("lastName"),
      message: "Last name must only contain letters."
    },
    {
      input: document.getElementById("nationality"),
      message: "Nationality must only contain letters."
    }
  ];

  const nameRegex = /^[A-Za-zÀ-ÿ\s'-]+$/;
  const minLength = 1;

  function showError(input, message) {
    clearError(input);
    input.classList.add("is-invalid");
    input.style.borderColor = "#dc2626";

    const error = document.createElement("div");
    error.className = "error-message";
    error.style.color = "#dc2626";
    error.style.fontSize = "0.85rem";
    error.style.marginTop = "0.25rem";
    error.textContent = message;
    input.parentElement.appendChild(error);
  }

  function clearError(input) {
    input.classList.remove("is-invalid");
    input.style.borderColor = "";
    const error = input.parentElement.querySelector(".error-message");
    if (error) error.remove();
  }

  form.addEventListener("submit", function (e) {
    let valid = true;

    fields.forEach(({ input, message }) => {
      const value = input.value.trim();
      if (!nameRegex.test(value) || value.length < minLength) {
        showError(input, message);
        valid = false;
      } else {
        clearError(input);
      }
    });

    if (!valid) {
      e.preventDefault();
    }
  });

  fields.forEach(({ input }) => {
    input.addEventListener("input", () => clearError(input));
  });
});
