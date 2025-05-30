function fillSavedAddress() {
  const checkbox = document.getElementById("useSavedAddress");
  const saved = document.getElementById("savedAddress").value;
  const textarea = document.getElementById("address");
  const orderButton = document.querySelector(".order-button");
  const methodSelect = document.getElementById("paymentMethod");
  const method = methodSelect.value;

  if (checkbox.checked) {
    console.log("test")
    console.log(orderButton)
    textarea.value = saved;
    if (method === "in_person") {
      orderButton.disabled = false;
    }
  } else {
    console.log("test2")
    textarea.value = '';
    orderButton.disabled = true;
  }
}

// Always show the address field (if it was hidden by default)
document.addEventListener("DOMContentLoaded", function () {
  const addressField = document.getElementById("address-field");
  if (addressField) {
    addressField.style.display = "block";
  }
});
