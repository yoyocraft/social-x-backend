package com.youyi.runner.ugc.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.base.PageResult;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.domain.ugc.helper.UgcHelper;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.request.UgcPublishRequest;
import com.youyi.domain.ugc.request.UgcQueryRequest;
import com.youyi.runner.ugc.model.UgcResponse;
import com.youyi.runner.ugc.util.UgcValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.domain.ugc.assembler.UgcAssembler.UGC_ASSEMBLER;
import static com.youyi.runner.ugc.util.UgcResponseUtil.publishSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.querySelfUgcSuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/23
 */
@RestController
@RequestMapping("/ugc")
@RequiredArgsConstructor
public class UgcController {

    private final UgcHelper ugcHelper;

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.UGC_PUBLISH)
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Result<Boolean> publish(@RequestBody UgcPublishRequest request) {
        UgcValidator.checkUgcPublishRequest(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        ugcHelper.publishUgc(ugcDO);
        return publishSuccess(request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public Result<PageResult<UgcResponse>> querySelfUgc(UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequest(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        Page<UgcDO> ugcPageInfo = ugcHelper.querySelfUgc(ugcDO);
        return querySelfUgcSuccess(ugcPageInfo, request);
    }

}
