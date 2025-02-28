package com.youyi.common.type.cache;

import java.time.Duration;
import lombok.Getter;

import static com.youyi.common.util.CacheUtil.ofKey;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/19
 */
@Getter
public enum CacheKey {

    UGC_VIEW_COUNT(ofKey("ugc", "${ugcId}", "sv"), Duration.ofHours(24)),
    UGC_LIKE_COUNT(ofKey("ugc", "${ugcId}", "sl"), Duration.ofHours(24)),
    UGC_COLLECT_COUNT(ofKey("ugc", "${ugcId}", "sc"), Duration.ofHours(24)),
    UGC_COMMENTARY_COUNT(ofKey("ugc", "${ugcId}", "scom"), Duration.ofHours(24)),
    UGC_USER_RECOMMEND_TAG(ofKey("ugc", "u", "${userId}", "rec_tag"), Duration.ofHours(24)),
    /**
     * 多设置一个小时，避免计算延迟
     */
    HOT_UGC_LIST(ofKey("ugc", "hot"), Duration.ofHours(25), false),

    COMMENTARY_LIKE_COUNT(ofKey("cmt", "${commentaryId}", "cl"), Duration.ofHours(24)),

    USER_VERIFY_TOKEN(ofKey("u", "vrf", "cap", "${email}", "${bizType}"), Duration.ofMinutes(10)),
    USER_FOLLOW_IDS(ofKey("u", "fl", "${userId}"), Duration.ofHours(24)),

    EMAIL_CAPTCHA(ofKey("em", "cap", "${email}", "${bizType}"), Duration.ofMillis(10)),
    ;

    private final boolean pattern;
    private final String key;
    private final Duration ttl;

    CacheKey(String key) {
        this.key = key;
        // 表示缓存常驻
        this.ttl = Duration.ZERO;
        this.pattern = true;
    }

    CacheKey(String key, Duration ttl) {
        this.key = key;
        this.ttl = ttl;
        this.pattern = true;
    }

    CacheKey(String key, Duration ttl, boolean pattern) {
        this.key = key;
        this.ttl = ttl;
        this.pattern = pattern;
    }
}
