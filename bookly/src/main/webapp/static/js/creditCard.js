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

// function validateExpiryDate(expiry) {
//   if (!/^\d{4}-\d{2}$/.test(expiry)) return false;

//   const [year, month] = expiry.split("-").map(Number);
//   const currentDate = new Date();
//   const currentYear = currentDate.getFullYear();
//   const currentMonth = currentDate.getMonth() + 1;

//   return year > currentYear || (year === currentYear && month >= currentMonth);
// }


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

    // if (!validateExpiryDate(expiry)) {
    //   alert("Invalid expiry date.");
    //   e.preventDefault();
    //   return;
    // }
  });
}

document.addEventListener("DOMContentLoaded", () => {
  const cardNumberInput = document.querySelector("input[name='cardNumber']");
  // const expiryInput = document.querySelector("input[name='expiry']");
  const cvvInput = document.querySelector("input[name='cvv']");
  const methodSelect = document.getElementById("paymentMethod");
  const creditCardFields = document.getElementById("credit-card-fields");
  const addressField = document.getElementById("address-field");
  const addressInput = document.getElementById("address");
  const orderButton = document.querySelector(".order-button");

  formatCardNumber(cardNumberInput);
  autoTab(cardNumberInput, "input[name='expiry']", 19);
  autoTab(expiryInput, "input[name='cvv']", 5);
  setupFormValidation();

  console.log(methodSelect);

  function updateButtonState() {
    const method = methodSelect.value;

    if (method === "credit_card") {
      const cardValid = validateCreditCardNumber(cardNumberInput.value);
      const cvvValid = validateCVV(cvvInput.value);
      // const expiryValid = validateExpiryDate(expiryInput.value);
      orderButton.disabled = !(cardValid && cvvValid);
    } else if (method === "in_person") {
      const addressValid = addressInput.value.trim().length > 0;
      orderButton.disabled = !addressValid;
    } else {
      orderButton.disabled = true;
    }
  }

  // Toggle field visibility on payment method change
  methodSelect.addEventListener("change", () => {
    const method = methodSelect.value;

    creditCardFields.style.display =  method === "credit_card" ? "block" : "none";
    addressField.style.display = method === "in_person" ? "block" : "none";

    updateButtonState();
  });

  // Watch all input fields for changes
  [cardNumberInput, expiryInput, cvvInput, addressInput, methodSelect].forEach(
    (input) => {
      input.addEventListener("input", updateButtonState);
    }
  );

  updateButtonState(); // check initial state
});
