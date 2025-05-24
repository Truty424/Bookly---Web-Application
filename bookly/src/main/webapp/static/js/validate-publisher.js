document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector("form");
  if (!form) return;

  const name = document.getElementById("publisher_name");
  const phone = document.getElementById("phone");

  const inputs = [name, phone];

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

    const nameRegex = /^[A-Za-zÀ-ÿ\s'-]{1,}$/;
    if (!nameRegex.test(name.value.trim())) {
      name.classList.add("is-invalid");
      name.nextElementSibling.textContent = "Publisher name must contain only letters.";
      name.nextElementSibling.style.display = "block";
      valid = false;
    }

    if (phone.value.trim() !== "") {
      const phoneRegex = /^\+\d{10,15}$/;
      if (!phoneRegex.test(phone.value.trim())) {
        phone.classList.add("is-invalid");
        phone.nextElementSibling.textContent =
          "Phone must be in format +1234567890 (10–15 digits).";
        phone.nextElementSibling.style.display = "block";
        valid = false;
      }
    }

    if (!valid) {
      e.preventDefault();
    }
  });
});
