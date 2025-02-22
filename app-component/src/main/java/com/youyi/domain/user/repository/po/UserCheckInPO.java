package com.youyi.domain.user.repository.po;

import com.youyi.common.base.BasePO;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/02/21
 */
@Getter
@Setter
public class UserCheckInPO extends BasePO {

    private String userId;

    private LocalDate checkInDate;

    private Long checkInTime;

}
