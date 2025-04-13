package com.youyi.common.util;

import com.youyi.common.constant.SymbolConstant;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/09
 */
public class MediaUtil {

    private static final Logger logger = LoggerFactory.getLogger(MediaUtil.class);

    public static String encodeImageToBase64(BufferedImage image, String format) throws IOException {
        // 将图片写入字节数组输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, format, byteArrayOutputStream);

        // 获取字节数组
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // 使用 Base64 编码
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public static String encodeImageToBase64(String imagePath) {
        return encodeImageToBase64(new File(imagePath));
    }

    public static String encodeImageToBase64(File file) {
        try {
            String format = getFileExtension(file);
            BufferedImage image = ImageIO.read(file);
            return encodeImageToBase64(image, format);
        } catch (IOException e) {
            logger.error("encodeImageToBase64FromPath error", e);
            return SymbolConstant.EMPTY;
        }
    }

    public static String getFileExtension(File file) {
        String extension = FilenameUtils.getExtension(file.getName());
        if (StringUtils.isNotBlank(extension)) {
            return extension;
        }
        return "jpg";
    }
}
