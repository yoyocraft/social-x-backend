package com.youyi.domain.media.core;

import com.youyi.common.type.media.MediaSource;
import com.youyi.domain.media.model.MediaResourceDO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.youyi.common.constant.MediaConstant.DATE_PATH_FORMATTER;
import static com.youyi.common.type.conf.ConfigKey.MEDIA_ACCESS_URL_PREFIX;
import static com.youyi.common.type.conf.ConfigKey.MEDIA_STORAGE_BASE_PATH;
import static com.youyi.common.type.media.ResourceType.IMAGE;
import static com.youyi.common.util.CommonOperationUtil.buildFullPath;
import static com.youyi.common.util.ext.MoreFeatures.runCatching;
import static com.youyi.infra.conf.core.Conf.checkConfig;
import static com.youyi.infra.conf.core.Conf.getStringConfig;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Component
public class LocalImageManager implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(@Nonnull ApplicationReadyEvent event) {
        checkConfig(MEDIA_STORAGE_BASE_PATH);
        checkConfig(MEDIA_ACCESS_URL_PREFIX);

        runCatching(() -> mkdirIfNeeded(getStringConfig(MEDIA_STORAGE_BASE_PATH)));
    }

    public void doUploadImage(MediaResourceDO mediaResourceDO) {
        // 1. 校验图片
        checkImage(mediaResourceDO.getMedia());
        // 2. 计算日期目录
        String datePath = getCurrentDatePath();
        // 3. 计算图片存储文件夹路径
        String storageImageDirPath = getStorageImageDirPath(mediaResourceDO.getSource(), datePath);
        // 4. 复制图片到存储文件夹
        String finalFilePath = runCatching(() -> copyImageToStorage(mediaResourceDO.getMedia(), storageImageDirPath));
        // 5. 初始化 media 信息
        mediaResourceDO.create(finalFilePath, buildAccessUrl(mediaResourceDO, datePath));
    }

    private void mkdirIfNeeded(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (Files.exists(path)) {
            return;
        }
        Files.createDirectories(path);
    }

    private String copyImageToStorage(File sourceFile, String dir) throws IOException {
        File destinationFile = new File(dir, sourceFile.getName());
        try (
            FileInputStream src = new FileInputStream(sourceFile);
            FileOutputStream dest = new FileOutputStream(destinationFile)
        ) {
            IOUtils.copy(src, dest);
        }
        return destinationFile.getAbsolutePath();
    }

    /**
     * @return e.g. 2024/12/07
     */
    private String getCurrentDatePath() {
        return DateFormatUtils.format(System.currentTimeMillis(), DATE_PATH_FORMATTER);
    }

    private void checkImage(File image) {
        checkNotNull(image, "image is null");
        checkArgument(image.exists(), "image not exist");
    }

    private String getStorageImageDirPath(MediaSource source, String datePath) {
        String storageDirPath = buildFullPath(
            getStringConfig(MEDIA_STORAGE_BASE_PATH),
            IMAGE.getType(),
            source.getSource(),
            datePath
        );
        runCatching(() -> mkdirIfNeeded(storageDirPath));
        return storageDirPath;
    }

    private String buildAccessUrl(MediaResourceDO mediaResourceDO, String datePath) {
        return buildFullPath(
            getStringConfig(MEDIA_ACCESS_URL_PREFIX),
            mediaResourceDO.getSource().getSource(),
            datePath,
            mediaResourceDO.getMedia().getName()
        );
    }
}
