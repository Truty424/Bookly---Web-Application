const searchInput = document.getElementById("searchInput");
const tableRows = document.querySelectorAll("#booksTable tbody tr");

searchInput.addEventListener("keyup", function () {
  const searchTerm = this.value.toLowerCase();
  tableRows.forEach((row) => {
    const title = row.querySelector("td:nth-child(2)").innerText.toLowerCase();
    row.style.display = title.includes(searchTerm) ? "" : "none";
  });
});
