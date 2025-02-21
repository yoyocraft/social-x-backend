package com.youyi.runner.ugc.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.common.util.CommonOperationUtil;
import com.youyi.domain.ugc.helper.UgcHelper;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.domain.ugc.request.UgcDeleteRequest;
import com.youyi.domain.ugc.request.UgcInteractionRequest;
import com.youyi.domain.ugc.request.UgcPublishRequest;
import com.youyi.domain.ugc.request.UgcQueryRequest;
import com.youyi.domain.ugc.request.UgcSetStatusRequest;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.runner.ugc.model.UgcResponse;
import com.youyi.runner.ugc.util.UgcValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.domain.ugc.assembler.UgcAssembler.UGC_ASSEMBLER;
import static com.youyi.runner.ugc.util.UgcResponseUtil.deleteSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.interactSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.publishSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.queryFollowPageUgcSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.queryMainPageUgcSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.queryUgcDetailSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.queryRecommendPageUgcSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.querySelfUgcSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.queryUserPageUgcSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.setStatusSuccess;

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
        LocalLockUtil.runWithLockFailSafe(
            () -> ugcHelper.publishUgc(ugcDO),
            CommonOperationUtil::tooManyRequestError,
            request.getUgcType(), request.getReqId()
        );
        return publishSuccess(request);
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.UGC_DELETE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result<Boolean> delete(@RequestBody UgcDeleteRequest request) {
        UgcValidator.checkUgcDeleteRequest(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> ugcHelper.deleteUgc(ugcDO),
            CommonOperationUtil::tooManyRequestError,
            request.getUgcId()
        );
        return deleteSuccess(request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public Result<PageCursorResult<String, UgcResponse>> querySelfUgc(UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForQuerySelfUgc(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcPageInfo = ugcHelper.querySelfUgc(ugcDO);
        return querySelfUgcSuccess(ugcPageInfo, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result<UgcResponse> queryUgcDetail(UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForQueryByUgcId(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        ugcHelper.queryByUgcId(ugcDO);
        return queryUgcDetailSuccess(ugcDO, request);
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.UGC_SET_STATUS)
    @RequestMapping(value = "/set_status", method = RequestMethod.POST)
    public Result<Boolean> setUgcStatus(@RequestBody UgcSetStatusRequest request) {
        UgcValidator.checkUgcSetStatusRequest(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> ugcHelper.updateUgcStatus(ugcDO),
            CommonOperationUtil::tooManyRequestError,
            request.getUgcId(), request.getStatus()
        );
        return setStatusSuccess(request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public Result<PageCursorResult<String, UgcResponse>> queryMainPageUgc(UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForMainPage(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcDOList = ugcHelper.queryByCursorForMainPage(ugcDO);
        return queryMainPageUgcSuccess(ugcDOList, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Result<PageCursorResult<String, UgcResponse>> queryUserPageUgc(UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForUserPage(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcDOList = ugcHelper.queryByCursorForUserPage(ugcDO);
        return queryUserPageUgcSuccess(ugcDOList, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/follow_feed", method = RequestMethod.GET)
    public Result<PageCursorResult<String, UgcResponse>> queryFollowPageUgc(UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForFollowPage(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcDOList = ugcHelper.queryFollowPageUgc(ugcDO);
        return queryFollowPageUgcSuccess(ugcDOList, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/recommend_feed", method = RequestMethod.GET)
    public Result<PageCursorResult<String, UgcResponse>> queryRecommendPageUgc(UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForRecommendPage(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcDOList = ugcHelper.queryRecommendPageUgc(ugcDO);
        return queryRecommendPageUgcSuccess(ugcDOList, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/hot", method = RequestMethod.GET)
    public Result<List<UgcResponse>> queryHotUgc() {
        // core logic:
        // score calculation: view_cnt(40%), like_cnt(30%), collect_cnt(20%), commentary_cnt(10%)
        // top 10
        // T+1, get from cache
        return null;
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.UGC_INTERACT)
    @RequestMapping(value = "/interact", method = RequestMethod.POST)
    public Result<Boolean> interact(@RequestBody UgcInteractionRequest request) {
        UgcValidator.checkUgcInteractionRequest(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> ugcHelper.interact(ugcDO),
            CommonOperationUtil::tooManyRequestError,
            request.getTargetId(), request.getInteractionType(), request.getReqId()
        );
        return interactSuccess(request);
    }
}
