package com.youyi.domain.audit.model;

import com.youyi.common.type.OperationType;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.audit.repository.po.OperationLogPO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Getter
@Setter
public class OperationLogDO {

    private OperationType operationType;
    private String operatorId;
    private String operatorName;
    private OperationLogExtraData extraData;
    private Long gmtCreate;
    private Long gmtModified;

    public void create() {
        this.gmtCreate = System.currentTimeMillis();
        this.gmtModified = System.currentTimeMillis();
    }

    public OperationLogPO buildToSaveOperationLog() {
        OperationLogPO toSaveOperationLog = new OperationLogPO();
        toSaveOperationLog.setOperationType(operationType.name());
        toSaveOperationLog.setOperatorId(operatorId);
        toSaveOperationLog.setOperatorName(operatorName);
        toSaveOperationLog.setExtraData(GsonUtil.toJson(extraData));
        toSaveOperationLog.setGmtCreate(gmtCreate);
        toSaveOperationLog.setGmtModified(gmtModified);
        return toSaveOperationLog;
    }
}
