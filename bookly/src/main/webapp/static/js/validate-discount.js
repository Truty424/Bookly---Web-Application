document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector("form");
  if (!form) return;

  const code = document.getElementById("code");
  const percentage = document.getElementById("percentage");
  const expires = document.getElementById("expiredDate");

  const inputs = [code, percentage, expires];

  inputs.forEach(input => {
    const error = document.createElement("div");
    error.className = "invalid-feedback";
    error.style.display = "none";
    error.style.fontSize = "0.9rem";
    error.style.color = "#dc3545";
    input.insertAdjacentElement("afterend", error);
  });

  form.addEventListener("submit", function (e) {
    let valid = true;

    inputs.forEach(input => {
      input.classList.remove("is-invalid");
      input.nextElementSibling.style.display = "none";
    });

    if (!/^[a-zA-Z0-9]+$/.test(code.value)) {
      code.classList.add("is-invalid");
      code.nextElementSibling.textContent = "Code must be alphanumeric.";
      code.nextElementSibling.style.display = "block";
      valid = false;
    }

    const pct = parseFloat(percentage.value);
    if (isNaN(pct) || pct < 1 || pct > 100) {
      percentage.classList.add("is-invalid");
      percentage.nextElementSibling.textContent = "Percentage must be between 1 and 100.";
      percentage.nextElementSibling.style.display = "block";
      valid = false;
    }

    if (!expires.value) {
      expires.classList.add("is-invalid");
      expires.nextElementSibling.textContent = "Expiration date is required.";
      expires.nextElementSibling.style.display = "block";
      valid = false;
    } else {
      const year = expires.value.split("-")[0];
      if (!/^\d{4}$/.test(year)) {
        expires.classList.add("is-invalid");
        expires.nextElementSibling.textContent = "Year must be 4 digits.";
        expires.nextElementSibling.style.display = "block";
        valid = false;
      } else {
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        const inputDate = new Date(expires.value);
        if (inputDate <= today) {
          expires.classList.add("is-invalid");
          expires.nextElementSibling.textContent = "Date must be in the future.";
          expires.nextElementSibling.style.display = "block";
          valid = false;
        }
      }
    }

    if (!valid) {
      e.preventDefault();
    }
  });
});
