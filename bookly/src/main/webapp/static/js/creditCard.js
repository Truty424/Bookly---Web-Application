// Format Card Number Input
function formatCardNumber(input) {
  input.addEventListener("input", () => {
    let value = input.value.replace(/\D/g, "").substring(0, 16);
    let formatted = value.match(/.{1,4}/g);
    input.value = formatted ? formatted.join(" ") : "";
    detectCardBrand(value);
  });
}

// Auto Move to Next Field
function autoTab(input, nextSelector, length) {
  input.addEventListener("input", () => {
    if (input.value.replace(/\s/g, "").length === length) {
      document.querySelector(nextSelector).focus();
    }
  });
}

// Card Brand Detection
function detectCardBrand(value) {
  const brandElem = document.getElementById("card-brand");
  if (!brandElem) return;

  const brand = /^4/.test(value)
    ? "Visa"
    : /^5[1-5]/.test(value)
    ? "MasterCard"
    : /^3[47]/.test(value)
    ? "American Express"
    : /^6/.test(value)
    ? "Discover"
    : "Unknown";

  brandElem.textContent = brand !== "Unknown" ? brand : "";
}

// Validate Card Number (Luhn)
function validateCreditCardNumber(number) {
  const cleaned = number.replace(/\D/g, "");
  if (!/^\d{13,19}$/.test(cleaned)) return false;

  let sum = 0;
  let shouldDouble = false;

  for (let i = cleaned.length - 1; i >= 0; i--) {
    let digit = parseInt(cleaned.charAt(i));
    if (shouldDouble) {
      digit *= 2;
      if (digit > 9) digit -= 9;
    }
    sum += digit;
    shouldDouble = !shouldDouble;
  }

  return sum % 10 === 0;
}

function validateCVV(cvv) {
  return /^\d{3,4}$/.test(cvv);
}

function validateExpiryDate(expiry) {
  if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(expiry)) return false;

  const [month, year] = expiry.split("/");
  const currentDate = new Date();
  const currentYear = parseInt(currentDate.getFullYear().toString().slice(-2));
  const currentMonth = currentDate.getMonth() + 1;

  const inputYear = parseInt(year);
  const inputMonth = parseInt(month);

  return (
    inputYear > currentYear ||
    (inputYear === currentYear && inputMonth >= currentMonth)
  );
}

function validateCardholderName(name) {
  return /^[a-zA-Z\s]{3,50}$/.test(name.trim());
}

function validateCart() {
  const cart = JSON.parse(localStorage.getItem("cart")) || [];
  return cart.length > 0;
}

// Main Submission Handler
function setupFormValidation() {
  const form = document.querySelector("#checkout-form");
  if (!form) return;

  form.addEventListener("submit", function (e) {
    const cardNumber = document.querySelector("input[name='cardNumber']").value;
    const cvv = document.querySelector("input[name='cvv']").value;
    const expiry = document.querySelector("input[name='expiry']").value;
    const cardholder = document.querySelector("input[name='cardholder']").value;

    if (!validateCart()) {
      alert("Cart is empty.");
      e.preventDefault();
      return;
    }

    if (!validateCreditCardNumber(cardNumber)) {
      alert("Invalid card number.");
      e.preventDefault();
      return;
    }

    if (!validateCVV(cvv)) {
      alert("Invalid CVV.");
      e.preventDefault();
      return;
    }

    if (!validateExpiryDate(expiry)) {
      alert("Invalid expiry date.");
      e.preventDefault();
      return;
    }

    if (!validateCardholderName(cardholder)) {
      alert("Invalid cardholder name.");
      e.preventDefault();
      return;
    }
  });
}

// Initialization
document.addEventListener("DOMContentLoaded", () => {
  formatCardNumber(document.querySelector("input[name='cardNumber']"));
  autoTab(
    document.querySelector("input[name='cardNumber']"),
    "input[name='expiry']",
    19
  );
  autoTab(
    document.querySelector("input[name='expiry']"),
    "input[name='cvv']",
    5
  );
  setupFormValidation();
  const methodSelect = document.getElementById("paymentMethod");
  const creditCardFields = document.getElementById("credit-card-fields");
  const addressField = document.getElementById("address-field");

  methodSelect.addEventListener("change", () => {
    const method = methodSelect.value;

    creditCardFields.style.display =
      method === "credit_card" ? "block" : "none";
    addressField.style.display =
      method === "pay_on_delivery" ? "block" : "none";
  });
});
