document.addEventListener("DOMContentLoaded", function () {
  const form = document.querySelector("form");
  if (!form) return;

  const fields = {
    title: document.getElementById("title"),
    language: document.getElementById("language"),
    isbn: document.getElementById("isbn"),
    price: document.getElementById("price"),
    edition: document.getElementById("edition"),
    publication_year: document.getElementById("publication_year"),
    number_of_pages: document.getElementById("number_of_pages"),
    stock_quantity: document.getElementById("stock_quantity"),
    summary: document.getElementById("summary")
  };

  Object.values(fields).forEach(input => {
    if (!input) return;
    const error = document.createElement("div");
    error.className = "invalid-feedback";
    error.style.display = "none";
    error.style.color = "#dc3545";
    error.style.fontSize = "0.9rem";
    input.insertAdjacentElement("afterend", error);
  });

  form.addEventListener("submit", function (e) {
    let valid = true;

    Object.values(fields).forEach(input => {
      input.classList.remove("is-invalid");
      input.nextElementSibling.style.display = "none";
    });

    ["title", "language", "isbn"].forEach(key => {
      const input = fields[key];
      if (!input.value.trim()) {
        input.classList.add("is-invalid");
        input.nextElementSibling.textContent = "This field is required.";
        input.nextElementSibling.style.display = "block";
        valid = false;
      }
    });

    const languageRegex = /^[A-Za-zÀ-ÿ\s]+$/;
    if (!languageRegex.test(fields.language.value.trim())) {
      fields.language.classList.add("is-invalid");
      fields.language.nextElementSibling.textContent = "Language must contain only letters.";
      fields.language.nextElementSibling.style.display = "block";
      valid = false;
    }

    const isbnRegex = /^\d{3}-\d{1,5}-\d{1,7}-\d{1,7}-\d{1}$/;
    const cleanedISBN = fields.isbn.value.replace(/-/g, "");
    if (!isbnRegex.test(fields.isbn.value.trim()) || cleanedISBN.length !== 13) {
      fields.isbn.classList.add("is-invalid");
      fields.isbn.nextElementSibling.textContent = "ISBN must follow format like xxx-x-xxx-xxxxx-x (13 digits total).";
      fields.isbn.nextElementSibling.style.display = "block";
      valid = false;
    }

    const price = parseFloat(fields.price.value.replace(",", "."));
    if (isNaN(price) || price <= 0) {
      fields.price.classList.add("is-invalid");
      fields.price.nextElementSibling.textContent = "Enter a valid positive price.";
      fields.price.nextElementSibling.style.display = "block";
      valid = false;
    }

    const year = fields.publication_year.value.trim();
    const currentYear = new Date().getFullYear();
    if (!/^\d{4}$/.test(year) || parseInt(year) > currentYear) {
      fields.publication_year.classList.add("is-invalid");
      fields.publication_year.nextElementSibling.textContent = `Enter a valid 4-digit year (max ${currentYear}).`;
      fields.publication_year.nextElementSibling.style.display = "block";
      valid = false;
    }

    const pages = parseInt(fields.number_of_pages.value);
    if (isNaN(pages) || pages <= 0) {
      fields.number_of_pages.classList.add("is-invalid");
      fields.number_of_pages.nextElementSibling.textContent = "Enter a valid positive number.";
      fields.number_of_pages.nextElementSibling.style.display = "block";
      valid = false;
    }

    const stock = parseInt(fields.stock_quantity.value);
    if (isNaN(stock) || stock < 0) {
      fields.stock_quantity.classList.add("is-invalid");
      fields.stock_quantity.nextElementSibling.textContent = "Enter a valid positive number.";
      fields.stock_quantity.nextElementSibling.style.display = "block";
      valid = false;
    }

    if (!valid) e.preventDefault();
  });
});
