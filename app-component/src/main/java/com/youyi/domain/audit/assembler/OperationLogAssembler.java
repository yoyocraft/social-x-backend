package com.youyi.domain.audit.assembler;

import com.youyi.common.type.OperationType;
import com.youyi.common.util.GsonUtil;
import com.youyi.domain.audit.model.OperationLogDO;
import com.youyi.domain.audit.model.OperationLogExtraData;
import com.youyi.domain.audit.repository.po.OperationLogPO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/05
 */
@Mapper(
    imports = {
        OperationType.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OperationLogAssembler {

    OperationLogAssembler OPERATION_LOG_ASSEMBLER = Mappers.getMapper(OperationLogAssembler.class);

    @Mappings({
        @Mapping(target = "operationType", expression = "java(OperationType.of(po.getOperationType()))"),
        @Mapping(target = "extraData", expression = "java(toExtraData(po.getExtraData()))")
    })
    OperationLogDO toDO(OperationLogPO po);

    default OperationLogExtraData toExtraData(String extra) {
        if (StringUtils.isBlank(extra)) {
            return null;
        }
        return GsonUtil.fromJson(extra, OperationLogExtraData.class);
    }
}
