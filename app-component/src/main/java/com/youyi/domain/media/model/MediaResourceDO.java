package com.youyi.domain.media.model;

import com.youyi.common.type.media.MediaSource;
import com.youyi.common.type.media.ResourceType;
import com.youyi.domain.media.repository.po.MediaResourcePO;
import com.youyi.domain.user.model.UserDO;
import java.io.File;
import lombok.Getter;
import lombok.Setter;

import static com.youyi.common.util.RandomGenUtil.genResourceKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Getter
@Setter
public class MediaResourceDO {

    private String resourceKey;
    private ResourceType resourceType;
    private String resourceUrl;
    private MediaSource source;

    private File media;
    private String creatorId;

    private String accessUrl;

    public MediaResourcePO buildToSaveMediaResourcePO() {
        MediaResourcePO mediaResourcePO = new MediaResourcePO();
        mediaResourcePO.setResourceKey(resourceKey);
        mediaResourcePO.setResourceType(resourceType.name());
        mediaResourcePO.setResourceUrl(resourceUrl);
        mediaResourcePO.setSource(source.name());
        mediaResourcePO.setCreatorId(creatorId);
        return mediaResourcePO;
    }

    public void create(String resourceUrl, String accessUrl) {
        this.resourceKey = genResourceKey();
        this.resourceUrl = resourceUrl;
        this.accessUrl = accessUrl;
    }

    public void fillCreatorInfo(UserDO currentUser) {
        this.creatorId = currentUser.getUserId();
    }
}
