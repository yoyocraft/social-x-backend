package com.youyi.domain.audit.model;

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

    private String operationType;
    private Long operatorId;
    private String operatorName;
    private String extraData;

    private OperationLogPO toSaveOperationLog;

    public void buildToSaveOperationLog() {
        toSaveOperationLog = new OperationLogPO();
        toSaveOperationLog.setOperationType(operationType);
        toSaveOperationLog.setOperatorId(operatorId);
        toSaveOperationLog.setOperatorName(operatorName);
        toSaveOperationLog.setGmtCreate(System.currentTimeMillis());
        toSaveOperationLog.setGmtModified(System.currentTimeMillis());
        toSaveOperationLog.setExtraData(extraData);
    }


}
