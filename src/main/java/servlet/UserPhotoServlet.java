package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/userPhotos/*")
public class UserPhotoServlet extends HttpServlet {
    private static final String PERMANENT_STORAGE_PATH = "/Users/danilbezin/IdeaProjects/Semestrovaya/permanent_storage";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String fileName = pathInfo.substring(1);
        File file = new File(PERMANENT_STORAGE_PATH, fileName);

        System.out.println("Requested file: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = getServletContext().getMimeType(fileName);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        response.setContentType(contentType);
        response.setContentLength((int) file.length());

        Files.copy(file.toPath(), response.getOutputStream());
    }
}
