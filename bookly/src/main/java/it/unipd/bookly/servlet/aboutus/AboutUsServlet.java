package it.unipd.bookly.servlet.aboutus;

import java.io.IOException;

import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "AboutUsServlet", value = "/about-us")
public class AboutUsServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LOGGER.info("Accessing About Us page");

        req.getRequestDispatcher("/jsp/aboutUs.jsp").forward(req, res);
    }
}