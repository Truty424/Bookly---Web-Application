package it.unipd.bookly.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*") // You can restrict this to /secure/* or /admin/* if needed
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        HttpSession session = req.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        boolean loginRequest = uri.endsWith("login.jsp") || uri.endsWith("LoginUser") || uri.contains("resources");

        if (loggedIn || loginRequest || uri.contains("static") || uri.contains("signup")) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(req.getContextPath() + "/user/login.jsp");
        }
    }
}
