package com.youyi.domain.user.repository.relation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/05
 */
@Getter
@Setter
public class SuggestedUserInfo {

    private String suggestedUserId;

    private Long score;
}
