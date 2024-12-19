package service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import config.CloudinaryConfig;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class PhotoUploadService {
    private final Cloudinary cloudinary;

    public PhotoUploadService() {
        this.cloudinary = CloudinaryConfig.getCloudinary();
    }

    public String uploadPhoto(Part filePart) throws IOException {
        InputStream inputStream = filePart.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        
        Map uploadResult = cloudinary.uploader().upload(
                bytes,
                ObjectUtils.asMap(
                        "folder", "user_photos",
                        "resource_type", "auto"
                )
        );

        return (String) uploadResult.get("secure_url");
    }
}
