function toggleWishlist(bookId, action) {
    fetch(`/wishlist/${action}/${bookId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.status === "success") {
          alert(data.message);
          // Optionally update the heart icon or button
        } else {
          console.error("Error:", data.message);
        }
      })
      .catch((error) => {
        console.error("Request failed:", error);
      });
}