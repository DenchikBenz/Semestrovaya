package config;

import com.cloudinary.Cloudinary;

public class CloudinaryConfig {
    private static final String CLOUDINARY_URL = "cloudinary://288591425236129:etEGu0bvkXtCZSwGyNQ4tVeEDmg@djajxmp6a";

    private static Cloudinary cloudinary;

    public static Cloudinary getCloudinary() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(CLOUDINARY_URL);
        }
        return cloudinary;
    }
}