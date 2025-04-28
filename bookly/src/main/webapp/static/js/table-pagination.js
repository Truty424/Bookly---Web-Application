document.addEventListener("DOMContentLoaded", () => {
    const tableBody = document.getElementById("tableBody");
    const rowsPerPage = document.getElementById("rowsPerPage");
    const pagination = document.getElementById("pagination");
    const searchInput = document.getElementById("searchInput");

    let rows = Array.from(tableBody.querySelectorAll("tr"));
    let currentPage = 1;

    function displayRows() {
      let perPage = parseInt(rowsPerPage.value);
      let start = (currentPage - 1) * perPage;
      let end = start + perPage;

      rows.forEach((row, index) => {
        row.style.display = index >= start && index < end ? "" : "none";
      });

      setupPagination();
    }

    function setupPagination() {
      let perPage = parseInt(rowsPerPage.value);
      let pageCount = Math.ceil(rows.length / perPage);

      pagination.innerHTML = "";

      for (let i = 1; i <= pageCount; i++) {
        let li = document.createElement("li");
        li.className = `page-item ${i === currentPage ? "active" : ""}`;
        li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        li.addEventListener("click", function (e) {
          e.preventDefault();
          currentPage = i;
          displayRows();
        });
        pagination.appendChild(li);
      }
    }

    searchInput.addEventListener("input", () => {
      let keyword = searchInput.value.toLowerCase();
      rows.forEach((row) => {
        let text = row.innerText.toLowerCase();
        row.style.display = text.includes(keyword) ? "" : "none";
      });
      setupPagination();
    });

    rowsPerPage.addEventListener("change", () => {
      currentPage = 1;
      displayRows();
    });

    displayRows();
  });
