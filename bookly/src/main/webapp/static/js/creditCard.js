// Format Card Number Input
function formatCardNumber(input) {
  input.addEventListener("input", () => {
    let value = input.value.replace(/\D/g, "").substring(0, 16);
    let formatted = value.match(/.{1,4}/g);
    input.value = formatted ? formatted.join(" ") : "";
    detectCardBrand(value);
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
  console.log("CLEANED:", cleaned);
  if (!/^\d{13,19}$/.test(cleaned)) {
    console.log("FAILED REGEX");
    return false;
  }
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

function showError(input, message) {
  let errorEl = input.parentElement.querySelector(".error-message");
  if (!errorEl) {
    errorEl = document.createElement("div");
    errorEl.className = "error-message";
    input.parentElement.appendChild(errorEl);
  }
  errorEl.textContent = message;
}

function clearError(input) {
  const errorEl = input.parentElement.querySelector(".error-message");
  if (errorEl) {
    errorEl.remove();
  }
}

function validateCVV(cvv) {
  return /^\d{3,4}$/.test(cvv);
}

function validateExpiryDate(expiry) {
  if (!/^\d{4}-\d{2}$/.test(expiry)) return false;

  const [year, month] = expiry.split("-").map(Number);

  const currentDate = new Date();
  const currentYear = currentDate.getFullYear();
  const currentMonth = currentDate.getMonth() + 1;

  return year > currentYear || (year === currentYear && month >= currentMonth);
}

function validateCart() {
  const cart = JSON.parse(localStorage.getItem("cart")) || [];
  return cart.length > 0;
}

function setupFormValidation() {
  const form = document.querySelector("#checkout-form");
  if (!form) return;

  form.addEventListener("submit", function (e) {
    const paymentMethod = document.getElementById("paymentMethod").value;

    if (paymentMethod === "credit_card") {
      const cardNumber = document.querySelector(
        "input[name='cardNumber']"
      ).value;
      const cvv = document.querySelector("input[name='cvv']").value;
      const expiry = document.querySelector("input[name='expiry']").value;

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
    } else if (paymentMethod === "in_person") {
      const address = document.getElementById("address").value;
      if (!address.trim()) {
        alert("Address is required.");
        e.preventDefault();
        return;
      }
    }
  });
}

document.addEventListener("DOMContentLoaded", () => {
  const cardNumberInput = document.querySelector("input[name='cardNumber']");
  const expiryInput = document.querySelector("input[name='expiry']");
  const cvvInput = document.querySelector("input[name='cvv']");
  const methodSelect = document.getElementById("paymentMethod");
  const creditCardFields = document.getElementById("credit-card-fields");
  const addressField = document.getElementById("address-field");
  const addressInput = document.getElementById("address");
  const orderButton = document.querySelector(".order-button");

  formatCardNumber(cardNumberInput);
  setupFormValidation();

  function updateButtonState() {
    clearError(cardNumberInput);
    clearError(cvvInput);
    clearError(expiryInput);
    clearError(addressInput);
    const method = methodSelect.value;

    if (method === "credit_card") {
      const cardValid = validateCreditCardNumber(cardNumberInput.value);
      const cvvValid = validateCVV(cvvInput.value);
      const expiryValid = validateExpiryDate(expiryInput.value);

      // Show errors only if there's input
      if (cardNumberInput.value.trim() && !cardValid) {
        showError(cardNumberInput, "Invalid card number.");
      } else {
        clearError(cardNumberInput);
      }

      if (cvvInput.value.trim() && !cvvValid) {
        showError(cvvInput, "Invalid CVV.");
      } else {
        clearError(cvvInput);
      }

      if (expiryInput.value.trim() && !validateExpiryDate(expiryInput.value)) {
        showError(expiryInput, "Expiry date must be in the future.");
      } else {
        clearError(expiryInput);
      }

      orderButton.disabled = !(cardValid && cvvValid && expiryValid);
    } else if (method === "in_person") {
      const addressValid = addressInput.value.trim().length > 0;

      if (!addressValid) {
        showError(addressInput, "Address is required.");
      } else {
        clearError(addressInput);
      }

      orderButton.disabled = !addressValid;
    } else {
      orderButton.disabled = true;
    }
  }

  // Toggle field visibility on payment method change
  methodSelect.addEventListener("change", () => {
    const method = methodSelect.value;

    creditCardFields.style.display =
      method === "credit_card" ? "block" : "none";
    addressField.style.display = method === "in_person" ? "block" : "none";

    updateButtonState();
  });

  // Watch all input fields for changes
  [cardNumberInput, cvvInput, addressInput, methodSelect].forEach((input) => {
    input.addEventListener("input", updateButtonState);
  });

  updateButtonState(); // check initial state
});
