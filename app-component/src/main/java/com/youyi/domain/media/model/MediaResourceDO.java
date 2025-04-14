package com.youyi.domain.media.model;

import com.youyi.domain.media.repository.po.MediaResourcePO;
import com.youyi.domain.media.type.MediaSource;
import com.youyi.domain.media.type.ResourceType;
import com.youyi.domain.user.model.UserDO;
import java.io.File;
import lombok.Getter;
import lombok.Setter;

import static com.youyi.common.util.seq.IdSeqUtil.genMediaResourceKey;

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
    private Long gmtCreate;
    private Long gmtModified;

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
        mediaResourcePO.setGmtCreate(gmtCreate);
        mediaResourcePO.setGmtModified(gmtModified);
        return mediaResourcePO;
    }

    public void fillUrl(String resourceUrl, String accessUrl) {
        this.resourceKey = genMediaResourceKey();
        this.resourceUrl = resourceUrl;
        this.accessUrl = accessUrl;
    }

    public void create() {
        this.gmtCreate = System.currentTimeMillis();
        this.gmtModified = System.currentTimeMillis();
    }

    public void fillCreatorInfo(UserDO creator) {
        this.creatorId = creator.getUserId();
    }
}
