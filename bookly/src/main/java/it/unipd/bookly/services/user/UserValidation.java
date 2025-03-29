package it.unipd.bookly.services.user;

import it.unipd.bookly.auth.JwtManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UserValidation {

    public static boolean isUserLoggedIn(HttpServletRequest req) {
        return req.getSession().getAttribute("Authorization") != null;
    }
    public static boolean isUserOwner(HttpServletRequest req, String owner) {
        String currentUser = getUserNameFromSession(req);
        return currentUser.equals(owner);
    }
    public static String getUserNameFromSession(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String token = (String) session.getAttribute("Authorization");
        return JwtManager.parseToken(token).getPayload().get("username").toString();
    }
}
