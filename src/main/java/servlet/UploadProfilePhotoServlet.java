package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import dao.UserDao;
import entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/uploadProfilePhoto")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1 MB
        maxFileSize = 1024 * 1024 * 5,    // 5 MB
        maxRequestSize = 1024 * 1024 * 10  // 10 MB
)
public class UploadProfilePhotoServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "uploads/profile_photos";
    private static final String PERMANENT_STORAGE_PATH = "/Users/danilbezin/IdeaProjects/Semestrovaya/permanent_storage";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        // Создаем директорию при инициализации сервлета
        File uploadDir = new File(PERMANENT_STORAGE_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try{
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

            String fileName = "user_" + user.getId() + System.currentTimeMillis() +
                    getFileExtension(filePart.getSubmittedFileName());
            String filePath = PERMANENT_STORAGE_PATH + File.separator + fileName;
            
            System.out.println("Saving file to: " + filePath);
            
            // Сохраняем файл в постоянную директорию
            filePart.write(filePath);
            
            // URL для доступа к файлу через сервлет
            String fileUrl = "userPhotos/" + fileName;
            System.out.println("File URL: " + fileUrl);
            
            UserDao userDao = new UserDao();
            userDao.updateUserPhoto(user.getId(), fileUrl);
            user.setPhotoPath(fileUrl);
            req.getSession().setAttribute("user", user);
            
            objectMapper.writeValue(resp.getWriter(),
                    Map.of("success", true, "photoUrl", fileUrl));
        } catch (Exception e) {
            e.printStackTrace();
            objectMapper.writeValue(resp.getWriter(),
                    Map.of("success", false, "error", "Произошла ошибка при загрузке файла: " + e.getMessage()));
        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
