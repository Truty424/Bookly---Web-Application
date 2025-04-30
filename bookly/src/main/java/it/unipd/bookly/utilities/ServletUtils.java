package it.unipd.bookly.utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletUtils {
    public static void redirectToErrorPage(HttpServletRequest req, HttpServletResponse resp, String errorMessage)
            throws IOException {
        req.getSession().setAttribute("errorMessage", errorMessage);
        resp.sendRedirect(req.getContextPath() + "/jsp/error.jsp");
    }
}
