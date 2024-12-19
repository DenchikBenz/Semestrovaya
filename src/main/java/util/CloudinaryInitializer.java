package util;

import com.cloudinary.utils.ObjectUtils;
import config.CloudinaryConfig;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@WebListener
public class CloudinaryInitializer implements ServletContextListener {
    private static final String DEFAULT_AVATAR_SVG = 
        "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"200\" height=\"200\" viewBox=\"0 0 200 200\">" +
        "<defs><style>.cls-1{fill:#3498db;}.cls-2{fill:#fff;}</style></defs>" +
        "<circle class=\"cls-1\" cx=\"100\" cy=\"100\" r=\"100\"/>" +
        "<circle class=\"cls-2\" cx=\"100\" cy=\"80\" r=\"35\"/>" +
        "<path class=\"cls-2\" d=\"M100,125c-25,0-55,20-55,55h110C155,145,125,125,100,125Z\"/>" +
        "</svg>";

    private static String defaultAvatarUrl = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Map uploadResult = CloudinaryConfig.getCloudinary().uploader()
                .upload(DEFAULT_AVATAR_SVG.getBytes(StandardCharsets.UTF_8),
                    ObjectUtils.asMap(
                        "public_id", "default-avatar",
                        "folder", "defaults",
                        "resource_type", "raw",
                        "format", "svg"
                    ));
            
            defaultAvatarUrl = (String) uploadResult.get("secure_url");
            System.out.println("Default avatar uploaded successfully to: " + defaultAvatarUrl);
            
        } catch (Exception e) {
            System.err.println("Error uploading default avatar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getDefaultAvatarUrl() {
        if (defaultAvatarUrl == null) {
            return "data:image/svg+xml;base64," + 
                   Base64.getEncoder().encodeToString(
                       DEFAULT_AVATAR_SVG.getBytes(StandardCharsets.UTF_8));
        }
        return defaultAvatarUrl;
    }
}
