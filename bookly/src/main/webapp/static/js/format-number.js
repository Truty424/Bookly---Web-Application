document.addEventListener("DOMContentLoaded", () => {
    // Format rating elements
    document.querySelectorAll("[data-format='rating']").forEach(el => {
      const value = parseFloat(el.textContent);
      if (!isNaN(value)) {
        el.textContent = value.toFixed(1);
      }
    });
  
    // Format price elements
    document.querySelectorAll("[data-format='price']").forEach(el => {
      const value = parseFloat(el.textContent);
      if (!isNaN(value)) {
        el.textContent = `€${value.toFixed(2)}`;
      }
    });
  });
  