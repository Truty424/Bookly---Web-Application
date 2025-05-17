package it.unipd.bookly.servlet.image;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;

@WebServlet("/user/uploadProfileImage")
@MultipartConfig
public class UploadProfileImageServlet extends HttpServlet {

    private static final String IMAGE_FOLDER = "/static/img/user";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userId = request.getParameter("userId");
        Part filePart = request.getPart("profileImage");

        if (userId == null || filePart == null || filePart.getSize() == 0) {
            request.setAttribute("upload_error", "Please select a file to upload.");
            request.getRequestDispatcher("/jsp/user/userProfile.jsp").forward(request, response);
            return;
        }

        // Ścieżka do folderu na serwerze (realna, fizyczna ścieżka)
        String realPath = getServletContext().getRealPath(IMAGE_FOLDER);
        File uploadDir = new File(realPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        // Zapisujemy plik jako np. "5.jpg"
        File file = new File(uploadDir, userId + ".jpg");

        try (InputStream input = filePart.getInputStream();
             FileOutputStream output = new FileOutputStream(file)) {
            input.transferTo(output);
        }

        request.setAttribute("upload_success", "Profile image uploaded successfully.");
        request.getRequestDispatcher("/jsp/user/userProfile.jsp").forward(request, response);
    }
}
