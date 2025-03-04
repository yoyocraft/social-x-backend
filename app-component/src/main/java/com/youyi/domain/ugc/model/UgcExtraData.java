package com.youyi.domain.ugc.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@Getter
@Setter
public class UgcExtraData {

    private String auditRet;

    /**
     * 是否已解决
     */
    private Boolean hasSolved = Boolean.FALSE;

    /**
     * AI 生成的总结
     */
    private String ugcSummary;
}
