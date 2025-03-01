package com.youyi.runner.ugc.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.youyi.common.base.Result;
import com.youyi.domain.ugc.helper.UgcMetadataHelper;
import com.youyi.domain.ugc.model.UgcMetadataDO;
import com.youyi.runner.ugc.model.UgcMetadataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.runner.ugc.util.UgcMetadataResponseUtil.queryUgcArticleTagSuccess;
import static com.youyi.runner.ugc.util.UgcMetadataResponseUtil.queryUgcCategorySuccess;
import static com.youyi.runner.ugc.util.UgcMetadataResponseUtil.queryUgcInterestTagSuccess;
import static com.youyi.runner.ugc.util.UgcMetadataResponseUtil.queryUgcQuestionCategorySuccess;
import static com.youyi.runner.ugc.util.UgcMetadataResponseUtil.queryUgcTopicSuccess;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/25
 */
@RestController
@RequestMapping("/ugc")
@RequiredArgsConstructor
public class UgcMetadataController {

    private final UgcMetadataHelper ugcMetadataHelper;

    @SaCheckLogin
    @RequestMapping(value = "/metadata/category", method = RequestMethod.GET)
    public Result<UgcMetadataResponse> queryUgcCategory() {
        UgcMetadataDO ugcMetadataDO = ugcMetadataHelper.queryUgcCategory();
        return queryUgcCategorySuccess(ugcMetadataDO);
    }

    @SaCheckLogin
    @RequestMapping(value = "/metadata/topic", method = RequestMethod.GET)
    public Result<UgcMetadataResponse> queryUgcTopic() {
        UgcMetadataDO ugcMetadataDO = ugcMetadataHelper.queryUgcTopic();
        return queryUgcTopicSuccess(ugcMetadataDO);
    }

    @SaCheckLogin
    @RequestMapping(value = "/metadata/qa_category", method = RequestMethod.GET)
    public Result<UgcMetadataResponse> queryUgcQuestionCategory() {
        UgcMetadataDO ugcMetadataDO = ugcMetadataHelper.queryUgcQuestionCategory();
        return queryUgcQuestionCategorySuccess(ugcMetadataDO);
    }

    @SaCheckLogin
    @RequestMapping(value = "/metadata/interest_tag", method = RequestMethod.GET)
    public Result<UgcMetadataResponse> queryUgcInterestTag() {
        UgcMetadataDO ugcMetadataDO = ugcMetadataHelper.queryUgcInterestTag();
        return queryUgcInterestTagSuccess(ugcMetadataDO);
    }

    @SaCheckLogin
    @RequestMapping(value = "/metadata/article_tag", method = RequestMethod.GET)
    public Result<UgcMetadataResponse> queryUgcArticleTag() {
        UgcMetadataDO ugcMetadataDO = ugcMetadataHelper.queryUgcArticleTag();
        return queryUgcArticleTagSuccess(ugcMetadataDO);
    }

}
