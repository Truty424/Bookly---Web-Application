function fillSavedAddress() {
  const checkbox = document.getElementById("useSavedAddress");
  const saved = document.getElementById("savedAddress").value;
  const textarea = document.getElementById("address");

  if (checkbox.checked) {
    textarea.value = saved;
  } else {
    textarea.value = '';
  }
}

// Always show the address field (if it was hidden by default)
document.addEventListener("DOMContentLoaded", function () {
  const addressField = document.getElementById("address-field");
  if (addressField) {
    addressField.style.display = "block";
  }
});
