package com.youyi.domain.media.helper;

import com.youyi.domain.media.core.LocalImageManager;
import com.youyi.domain.media.model.MediaResourceDO;
import com.youyi.domain.media.repository.MediaResourceRepository;
import com.youyi.domain.user.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Service
@RequiredArgsConstructor
public class MediaResourceHelper {

    private final MediaResourceRepository mediaResourceRepository;
    private final UserService userService;
    private final LocalImageManager localImageManager;

    public void uploadImage(MediaResourceDO mediaResourceDO) {
        // 1. 填充创建者信息
        mediaResourceDO.fillCreatorInfo(userService.getCurrentUserInfo());
        // 2. 上传图片
        localImageManager.doUploadImage(mediaResourceDO);
        // 3. 落库
        mediaResourceRepository.insertMedia(mediaResourceDO.buildToSaveMediaResourcePO());
    }

}
