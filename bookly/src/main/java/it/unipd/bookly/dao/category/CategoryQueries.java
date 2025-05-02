package it.unipd.bookly.dao.category;

public final class CategoryQueries {

    private CategoryQueries() {
    }

    // --- CREATE ---
    public static final String ADD_CATEGORY_TO_BOOK = """
        INSERT INTO booklySchema.category_belongs (book_id, category_id)
        VALUES (?, ?)
    """;

    // --- READ ---
    public static final String GET_ALL_CATEGORIES = """
        SELECT *
        FROM booklySchema.categories
        ORDER BY category_name
    """;

    public static final String GET_CATEGORY_BY_ID = """
        SELECT *
        FROM booklySchema.categories
        WHERE category_id = ?
    """;

    public static final String GET_CATEGORY_BY_NAME = """
        SELECT *
        FROM booklySchema.categories
        WHERE category_name ILIKE ?
    """;

    public static final String GET_CATEGORIES_BY_BOOK = """
        SELECT c.*
        FROM booklySchema.categories c
        JOIN booklySchema.category_belongs cb ON c.category_id = cb.category_id
        WHERE cb.book_id = ?
    """;

    public static final String GET_BOOKS_BY_CATEGORY = """
        SELECT b.*, i.image, i.image_type
        FROM booklySchema.books b
        JOIN booklySchema.category_belongs cb ON b.book_id = cb.book_id
        LEFT JOIN booklySchema.book_image i ON b.book_id = i.book_id
        WHERE cb.category_id = ?
    """;

    // --- DELETE ---
    public static final String REMOVE_CATEGORY_FROM_BOOK = """
        DELETE FROM booklySchema.category_belongs
        WHERE book_id = ? AND category_id = ?
    """;
}
