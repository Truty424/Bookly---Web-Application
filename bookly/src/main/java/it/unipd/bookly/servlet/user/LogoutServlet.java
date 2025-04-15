package it.unipd.bookly.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/user/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Invalidate user session
        if (req.getSession(false) != null) {
            req.getSession().invalidate();
        }

        // Optional: clear Authorization header (if set manually)
        resp.setHeader("Authorization", "");

        // Redirect to home page or login page
        resp.sendRedirect(req.getContextPath() + "/");
    }

    // Also handle POST if form submits logout
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
