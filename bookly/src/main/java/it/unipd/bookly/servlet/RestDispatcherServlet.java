package it.unipd.bookly.servlet;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.rest.DeleteRecipeRest;
import it.unipd.bookly.rest.GetAvgRateOfRecipeRest;
import it.unipd.bookly.rest.GetRecipeRest;
import it.unipd.bookly.rest.InsertRecipeRest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

public class RestDispatcherServlet extends AbstractDatabaseServlet {
    /**
     * The JSON UTF-8 MIME media type
     */
    private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        final OutputStream out = resp.getOutputStream();

        try {
            if (processRecipe(req, resp))
                return;

            handleUnknownRequest(req, resp, out);
        } catch (Throwable t) {
            LOGGER.error("Unexpected error while processing the REST resource.", t);

            final Message message = new Message("Unexpected error.", "E5A1", t.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message.toJSON(out);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
            LogContext.removeIPAddress();
        }
    }

    private boolean processRecipe(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        final String method = req.getMethod();

        String path = req.getRequestURI();
        Message message = null;

        if (path.lastIndexOf("rest/recipe/") <= 0) {
            return false;
        }

        switch (method) {
            case "POST":
                new InsertRecipeRest(req, resp, getConnection()).serve();
                break;
            case "GET":
                if (path.contains("avgRate"))
                    new GetAvgRateOfRecipeRest(req, resp, getConnection()).serve();

                else
                    new GetRecipeRest(req, resp, getConnection()).serve();
                break;
            case "DELETE":
                new DeleteRecipeRest(req, resp, getConnection()).serve();
                break;
            default:
                message = new Message("Method not allowed.", "E4A5", String.format("Method %s is not allowed.", method));
                resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                message.toJSON(resp.getOutputStream());
                break;
        }

        return true;
    }

    private void handleUnknownRequest(HttpServletRequest req, HttpServletResponse resp, OutputStream out) throws IOException {
        LOGGER.warn("Unknown resource requested: %s.", req.getRequestURI());

        final Message message = new Message("Unknown resource requested.", "E4A6",
                String.format("Requested resource is %s.", req.getRequestURI()));
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.setContentType(JSON_UTF_8_MEDIA_TYPE);
        message.toJSON(out);
    }
}
