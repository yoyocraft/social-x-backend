package com.youyi.runner.ugc.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.common.util.CommonOperationUtil;
import com.youyi.domain.ugc.helper.UgcHelper;
import com.youyi.domain.ugc.model.UgcDO;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.infra.sse.SseEmitter;
import com.youyi.runner.ugc.model.UgcDeleteRequest;
import com.youyi.runner.ugc.model.UgcInteractionRequest;
import com.youyi.runner.ugc.model.UgcPublishRequest;
import com.youyi.runner.ugc.model.UgcQueryRequest;
import com.youyi.runner.ugc.model.UgcResponse;
import com.youyi.runner.ugc.model.UgcStatisticResponse;
import com.youyi.runner.ugc.model.UgcSummaryGenerateRequest;
import com.youyi.runner.ugc.util.UgcValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.runner.ugc.assembler.UgcAssembler.UGC_ASSEMBLER;
import static com.youyi.runner.ugc.util.UgcResponseUtil.deleteSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.generateSummarySuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.interactSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.listFollowUgcFeedSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.listQuestionsSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.listRecommendUgcFeedSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.listSelfCollectedUgcSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.listTimelineUgcFeedSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.publishSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.queryHotUgcSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.querySelfUgcSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.queryUgcDetailSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.queryUgcStatisticSuccess;
import static com.youyi.runner.ugc.util.UgcResponseUtil.queryUserPageUgcSuccess;

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
    public Result<Boolean> publishUgc(@RequestBody UgcPublishRequest request) {
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
    public Result<Boolean> deleteUgc(@RequestBody UgcDeleteRequest request) {
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
    @RequestMapping(value = "/me", method = RequestMethod.POST)
    public Result<PageCursorResult<String, UgcResponse>> listSelfUgc(@RequestBody UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForQuerySelfUgc(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcPageInfo = ugcHelper.listSelfUgc(ugcDO);
        return querySelfUgcSuccess(ugcPageInfo, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/me/collected", method = RequestMethod.POST)
    public Result<PageCursorResult<Long, UgcResponse>> listSelfCollectedUgc(@RequestBody UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForQuerySelfCollectionUgc(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcPageInfo = ugcHelper.listSelfCollectedUgc(ugcDO);
        return listSelfCollectedUgcSuccess(ugcPageInfo, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Result<UgcResponse> queryUgcDetail(@RequestBody UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForQueryByUgcId(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        ugcHelper.queryByUgcId(ugcDO);
        return queryUgcDetailSuccess(ugcDO, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Result<PageCursorResult<String, UgcResponse>> queryUserPageUgc(@RequestBody UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForUserPage(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcDOList = ugcHelper.queryUserUgc(ugcDO);
        return queryUserPageUgcSuccess(ugcDOList, request);
    }

    /**
     * core query api
     */
    @SaCheckLogin
    @RequestMapping(value = "/time_feed", method = RequestMethod.POST)
    public Result<PageCursorResult<String, UgcResponse>> listTimelineUgcFeed(@RequestBody UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForTimelineFeed(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcDOList = ugcHelper.listTimelineUgcFeed(ugcDO);
        return listTimelineUgcFeedSuccess(ugcDOList, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/follow_feed", method = RequestMethod.POST)
    public Result<PageCursorResult<String, UgcResponse>> listFollowUgcFeed(@RequestBody UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForFollowFeed(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcDOList = ugcHelper.listFollowUgcFeed(ugcDO);
        return listFollowUgcFeedSuccess(ugcDOList, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/recommend_feed", method = RequestMethod.POST)
    public Result<PageCursorResult<String, UgcResponse>> listRecommendUgcFeed(@RequestBody UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForRecommendFeed(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcDOList = ugcHelper.listRecommendUgcFeed(ugcDO);
        return listRecommendUgcFeedSuccess(ugcDOList, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/qa", method = RequestMethod.POST)
    public Result<PageCursorResult<String, UgcResponse>> listQuestions(@RequestBody UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForListQuestion(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcDOList = ugcHelper.listQuestions(ugcDO);
        return listQuestionsSuccess(ugcDOList, request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/hot", method = RequestMethod.POST)
    public Result<List<UgcResponse>> listHotUgc(@RequestBody UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForHotUgcList(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        List<UgcDO> ugcDOList = ugcHelper.listHotUgc(ugcDO);
        return queryHotUgcSuccess(ugcDOList);
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.UGC_INTERACT)
    @RequestMapping(value = "/interact", method = RequestMethod.POST)
    public Result<Boolean> interactUgc(@RequestBody UgcInteractionRequest request) {
        UgcValidator.checkUgcInteractionRequest(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> ugcHelper.interact(ugcDO),
            CommonOperationUtil::tooManyRequestError,
            request.getTargetId(), request.getInteractionType(), request.getReqId()
        );
        return interactSuccess(request);
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public SseEmitter generateSummary(UgcSummaryGenerateRequest request) {
        UgcValidator.checkUgcSummaryGenerateRequest(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        SseEmitter sseEmitter = ugcHelper.generateSummary(ugcDO);
        return generateSummarySuccess(sseEmitter, request);
    }

    @RequestMapping(value = "/statistic", method = RequestMethod.POST)
    public Result<UgcStatisticResponse> queryUgcStatistic(@RequestBody UgcQueryRequest request) {
        UgcValidator.checkUgcQueryRequestForUgcStatistic(request);
        UgcDO ugcDO = UGC_ASSEMBLER.toDO(request);
        ugcHelper.queryUgcStatistic(ugcDO);
        return queryUgcStatisticSuccess(request, ugcDO);
    }
}
