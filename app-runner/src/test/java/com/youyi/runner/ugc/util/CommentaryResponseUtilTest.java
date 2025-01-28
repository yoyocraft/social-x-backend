package com.youyi.runner.ugc.util;

import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.common.constant.RepositoryConstant;
import com.youyi.common.type.ReturnCode;
import com.youyi.common.type.ugc.CommentaryStatus;
import com.youyi.common.util.IdSeqUtil;
import com.youyi.domain.ugc.model.CommentaryDO;
import com.youyi.domain.ugc.request.CommentaryQueryRequest;
import com.youyi.domain.user.model.UserDO;
import com.youyi.runner.ugc.model.CommentaryResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/28
 */
class CommentaryResponseUtilTest {

    @Test
    void testQueryUgcCommentarySuccess() {
        CommentaryQueryRequest request = buildQueryRequest();
        List<CommentaryDO> commentaryDOList = buildCommentaryDOList();
        Result<PageCursorResult<String, CommentaryResponse>> result = CommentaryResponseUtil.queryUgcCommentarySuccess(commentaryDOList, request);
        Assertions.assertEquals(ReturnCode.SUCCESS.getCode(), result.getCode());
    }

    CommentaryQueryRequest buildQueryRequest() {
        CommentaryQueryRequest request = new CommentaryQueryRequest();
        request.setUgcId("ugcId");
        request.setCursor("0");
        return request;
    }

    List<CommentaryDO> buildCommentaryDOList() {
        UserDO commentator1 = new UserDO();
        commentator1.setUserId(IdSeqUtil.genUserId());
        commentator1.setNickName("commentator1");
        commentator1.setAvatar("avatar1");

        UserDO commentator2 = new UserDO();
        commentator2.setUserId(IdSeqUtil.genUserId());
        commentator2.setNickName("commentator2");
        commentator2.setAvatar("avatar2");

        CommentaryDO parentCommentary1 = buildCommentaryDO(RepositoryConstant.TOP_COMMENTARY_ID, IdSeqUtil.genCommentaryId(), commentator1);
        CommentaryDO childCommentary11 = buildCommentaryDO(parentCommentary1.getCommentaryId(), IdSeqUtil.genCommentaryId(), commentator2);
        CommentaryDO childCommentary21 = buildCommentaryDO(parentCommentary1.getCommentaryId(), IdSeqUtil.genCommentaryId(), commentator2);

        CommentaryDO parentCommentary2 = buildCommentaryDO(RepositoryConstant.TOP_COMMENTARY_ID, IdSeqUtil.genCommentaryId(), commentator2);
        CommentaryDO childCommentary22 = buildCommentaryDO(parentCommentary2.getCommentaryId(), IdSeqUtil.genCommentaryId(), commentator1);
        CommentaryDO childCommentary23 = buildCommentaryDO(parentCommentary2.getCommentaryId(), IdSeqUtil.genCommentaryId(), commentator1);
        return List.of(parentCommentary1, childCommentary11, childCommentary21, parentCommentary2, childCommentary22, childCommentary23);
    }

    CommentaryDO buildCommentaryDO(String parentId, String commentaryId, UserDO commentator) {
        CommentaryDO commentaryDO = new CommentaryDO();
        commentaryDO.setCommentaryId(commentaryId);
        commentaryDO.setParentId(parentId);
        commentaryDO.setUgcId(IdSeqUtil.genUgcId());
        commentaryDO.setCommentator(commentator);
        commentaryDO.setCommentary("test_" + parentId);
        commentaryDO.setLikeCount(100L);
        commentaryDO.setStatus(CommentaryStatus.NORMAL);
        commentaryDO.setGmtCreate(System.currentTimeMillis());
        commentaryDO.setGmtModified(System.currentTimeMillis());
        commentaryDO.setCursor("0");
        return commentaryDO;
    }
}

// Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme