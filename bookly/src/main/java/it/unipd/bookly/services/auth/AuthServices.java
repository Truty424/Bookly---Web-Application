package it.unipd.bookly.services.auth;

import java.io.IOException;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.auth.JwtManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthServices {

    private static final String AUTH_HEADER_KEY = "Authorization";

    /**
     * Authenticates the user.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     *
     * @return {@code true} if the user has been successfully authenticated;
     *         {@code false otherwise}.
     */
    public static boolean authenticateUser(HttpServletRequest req, HttpServletResponse res) {

        final String auth = res.getHeader(AUTH_HEADER_KEY);
        try {
            // get the authorization information
            if (auth == null || auth.isBlank()) {
                sendAuthenticationChallenge(res);
                return false;
            }

            // perform Base64 decoding
            String username = JwtManager.parseToken(auth).getPayload().get("username").toString();
            if (username != null || username != "") {
                // create a new session
                HttpSession session = req.getSession(true);
                session.setAttribute("username", username);
                return true;
            }

        } catch (Exception e) {
            // LOGGER.error("Unable to authenticate the user.", e);
        } finally {
            LogContext.removeAction();
        }

        return false;
    }

    /**
     * Sends the authentication challenge.
     *
     * @param res the HTTP servlet response.
     *
     * @throws IOException if anything goes wrong while sending the authentication
     *                     challenge.
     */
    private static void sendAuthenticationChallenge(HttpServletResponse res) throws IOException {
        try {
            res.setHeader("Authorization", "");
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            throw e;
        }
    }

}
