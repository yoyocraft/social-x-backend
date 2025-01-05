package com.youyi.core.audit.assembler;

import com.youyi.core.audit.domain.OperationLogDO;
import com.youyi.core.audit.repository.po.OperationLogPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OperationLogAssembler {

    OperationLogAssembler OPERATION_LOG_ASSEMBLER = Mappers.getMapper(OperationLogAssembler.class);

    OperationLogDO toDO(OperationLogPO po);
}
