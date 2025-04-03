package it.unipd.bookly.servlet.home;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.book.GetAllBooksDAO;
import it.unipd.bookly.dao.book.GetTopRatedBooksDAO;
import it.unipd.bookly.dao.category.GetAllCategoriesDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeServlet", value = "/home") public class HomeServlet extends AbstractDatabaseServlet {

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    LogContext.setIPAddress(req.getRemoteAddr());
    LogContext.setResource(req.getRequestURI());
    LogContext.setAction("HomeServlet");
    
    try {
        // Fetch all categories
        List<Category> categories = new GetAllCategoriesDAO(getConnection()).access().getOutputParam();
        
        // Fetch top-rated books
        List<Book> topRatedBooks = new GetTopRatedBooksDAO(getConnection()).access().getOutputParam();
        
        // Fetch all books (optional, if needed)
        List<Book> allBooks = new GetAllBooksDAO(getConnection()).access().getOutputParam();
        
        // Set attributes for rendering in JSP
        req.setAttribute("categories", categories);
        req.setAttribute("topRatedBooks", topRatedBooks);
        req.setAttribute("allBooks", allBooks);
        
        // Forward to home.jsp
        req.getRequestDispatcher("/jsp/home.jsp").forward(req, resp);
    } catch (Exception e) {
        LOGGER.error("Error in HomeServlet: {}", e.getMessage());
        req.getRequestDispatcher("/html/error.html").forward(req, resp);
    } finally {
        LogContext.removeAction();
        LogContext.removeResource();
    }
}

}

//package it.unipd.bookly.serverlet.home;
//
//import it.unipd.bookly.LogContext;
//import it.unipd.bookly.Resource.Category;
//import it.unipd.bookly.Resource.Collection;
//import it.unipd.bookly.Resource.Recipe;
//import it.unipd.bookly.dao.category.GetAllCategoriesDAO;
//import it.unipd.bookly.dao.category.GetRecipesOfCategoryDAO;
//import it.unipd.bookly.dao.collection.GetAllCollectionsDAO;
//import it.unipd.bookly.dao.collection.GetRecipesInCollectionDAO;
//import it.unipd.bookly.servlet.AbstractDatabaseServlet;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.util.List;
//
//import static it.unipd.bookly.services.user.UserValidation.isUserLoggedIn;
//
//public class HomeServlet extends AbstractDatabaseServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        LogContext.setIPAddress(req.getRemoteAddr());
//        LogContext.setResource(req.getRequestURI());
//        LogContext.setAction("Home");
//        LOGGER.info("HomeServlet");
//        prepareAddRecipe(req, resp);
//    }
//
//    private void prepareAddRecipe(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try {
//            List<Category> categories = new GetAllCategoriesDAO(getConnection()).access().getOutputParam();
//            // show the recipes of the first category which is Breakfast
//            List<Recipe> recipes = new GetRecipesOfCategoryDAO(getConnection(), categories.getFirst().getCategory_name()).access().getOutputParam();
//            List<Collection> collections = new GetAllCollectionsDAO(getConnection()).access().getOutputParam();
//            List<Recipe> collectionRecipes = new GetRecipesInCollectionDAO(getConnection(), collections.getFirst().getCollection_name()).access().getOutputParam();
//
//            req.setAttribute("home_recipes", recipes);
//            req.setAttribute("categories", categories);
//            req.setAttribute("collections", collections);
//            req.setAttribute("collection_recipes", collectionRecipes);
//            req.setAttribute("userIsLoggedIn", isUserLoggedIn(req));
//
//            req.getRequestDispatcher("/jsp/home.jsp").forward(req, resp);
//        } catch (Exception e) {
//            LOGGER.error(String.format("Error accessing the database: %s", e.getMessage()));
//            req.getRequestDispatcher("/html/error.html").forward(req, resp);
//        }
//    }
//}
