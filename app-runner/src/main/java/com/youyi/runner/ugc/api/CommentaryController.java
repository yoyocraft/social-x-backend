package com.youyi.runner.ugc.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.youyi.common.annotation.RecordOpLog;
import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.type.OperationType;
import com.youyi.common.util.CommonOperationUtil;
import com.youyi.domain.ugc.helper.CommentaryHelper;
import com.youyi.domain.ugc.model.CommentaryDO;
import com.youyi.infra.lock.LocalLockUtil;
import com.youyi.runner.ugc.model.CommentaryDeleteRequest;
import com.youyi.runner.ugc.model.CommentaryPublishRequest;
import com.youyi.runner.ugc.model.CommentaryQueryRequest;
import com.youyi.runner.ugc.model.CommentaryResponse;
import com.youyi.runner.ugc.model.UgcInteractionRequest;
import com.youyi.runner.ugc.util.CommentaryValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.runner.ugc.assembler.CommentaryAssembler.COMMENTARY_ASSEMBLER;
import static com.youyi.runner.ugc.util.CommentaryResponseUtil.adoptSuccess;
import static com.youyi.runner.ugc.util.CommentaryResponseUtil.deleteSuccess;
import static com.youyi.runner.ugc.util.CommentaryResponseUtil.featuredSuccess;
import static com.youyi.runner.ugc.util.CommentaryResponseUtil.likeSuccess;
import static com.youyi.runner.ugc.util.CommentaryResponseUtil.publishSuccess;
import static com.youyi.runner.ugc.util.CommentaryResponseUtil.queryUgcCommentarySuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/commentary")
public class CommentaryController {

    private final CommentaryHelper commentaryHelper;

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.COMMENTARY_PUBLISH)
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Result<Boolean> publishCommentary(@RequestBody CommentaryPublishRequest request) {
        CommentaryValidator.checkCommentaryPublishRequest(request);
        CommentaryDO commentaryDO = COMMENTARY_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> commentaryHelper.publish(commentaryDO),
            CommonOperationUtil::tooManyRequestError,
            commentaryDO.getUgcId(), request.getReqId()
        );
        return publishSuccess(request);
    }

    @SaCheckLogin
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Result<PageCursorResult<String, CommentaryResponse>> queryUgcCommentary(CommentaryQueryRequest request) {
        CommentaryValidator.checkCommentaryQueryRequest(request);
        CommentaryDO commentaryDO = COMMENTARY_ASSEMBLER.toDO(request);
        List<CommentaryDO> commentaryDOList = commentaryHelper.queryUgcCommentary(commentaryDO);
        return queryUgcCommentarySuccess(commentaryDOList, request);
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.COMMENTARY_DELETE)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result<Boolean> deleteCommentary(@RequestBody CommentaryDeleteRequest request) {
        CommentaryValidator.checkCommentaryDeleteRequest(request);
        CommentaryDO commentaryDO = COMMENTARY_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> commentaryHelper.deleteCommentary(commentaryDO),
            CommonOperationUtil::tooManyRequestError,
            commentaryDO.getUgcId()
        );
        return deleteSuccess(request);
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.UGC_INTERACT)
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public Result<Boolean> likeCommentary(@RequestBody UgcInteractionRequest request) {
        CommentaryValidator.checkUgcInteractionRequest(request);
        CommentaryDO commentaryDO = COMMENTARY_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> commentaryHelper.like(commentaryDO),
            CommonOperationUtil::tooManyRequestError,
            commentaryDO.getCommentaryId(), request.getReqId()
        );
        return likeSuccess(request);
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.UGC_ADOPT)
    @RequestMapping(value = "/adopt", method = RequestMethod.POST)
    public Result<Boolean> adoptCommentary(@RequestBody UgcInteractionRequest request) {
        CommentaryValidator.checkUgcInteractionRequest(request);
        CommentaryDO commentaryDO = COMMENTARY_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> commentaryHelper.adopt(commentaryDO),
            CommonOperationUtil::tooManyRequestError,
            commentaryDO.getCommentaryId(), request.getReqId()
        );
        return adoptSuccess(request);
    }

    @SaCheckLogin
    @RecordOpLog(opType = OperationType.UGC_ADOPT)
    @RequestMapping(value = "/featured", method = RequestMethod.POST)
    public Result<Boolean> featuredCommentary(@RequestBody UgcInteractionRequest request) {
        CommentaryValidator.checkUgcInteractionRequest(request);
        CommentaryDO commentaryDO = COMMENTARY_ASSEMBLER.toDO(request);
        LocalLockUtil.runWithLockFailSafe(
            () -> commentaryHelper.featured(commentaryDO),
            CommonOperationUtil::tooManyRequestError,
            commentaryDO.getCommentaryId(), request.getReqId()
        );
        return featuredSuccess(request);
    }
}
