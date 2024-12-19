package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDao;
import entity.User;
import service.PhotoUploadService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Map;

@WebServlet("/uploadProfilePhoto")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1 MB
        maxFileSize = 1024 * 1024 * 5,    // 5 MB
        maxRequestSize = 1024 * 1024 * 10  // 10 MB
)
public class UploadProfilePhotoServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PhotoUploadService photoUploadService = new PhotoUploadService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            User user = (User) req.getSession().getAttribute("user");
            if (user == null) {
                objectMapper.writeValue(resp.getWriter(),
                        Map.of("success", false, "message", "User not found"));
                return;
            }

            Part filePart = req.getPart("file");
            if (filePart == null) {
                objectMapper.writeValue(resp.getWriter(),
                        Map.of("success", false, "error", "Файл не выбран"));
                return;
            }

            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                objectMapper.writeValue(resp.getWriter(),
                        Map.of("success", false, "error", "Выбранный файл не является изображением"));
                return;
            }

            String cloudinaryUrl = photoUploadService.uploadPhoto(filePart);

            UserDao userDao = new UserDao();
            userDao.updateUserPhoto(user.getId(), cloudinaryUrl);
            user.setPhotoPath(cloudinaryUrl);
            req.getSession().setAttribute("user", user);

            objectMapper.writeValue(resp.getWriter(),
                    Map.of("success", true, "photoUrl", cloudinaryUrl));

        } catch (Exception e) {
            e.printStackTrace();
            objectMapper.writeValue(resp.getWriter(),
                    Map.of("success", false, "error", "Произошла ошибка при загрузке файла: " + e.getMessage()));
        }
    }
    }
