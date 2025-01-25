package com.youyi.runner.ugc.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.youyi.common.base.PageCursorResult;
import com.youyi.common.base.Result;
import com.youyi.domain.ugc.helper.UgcMetadataHelper;
import com.youyi.domain.ugc.model.UgcMetadataDO;
import com.youyi.domain.ugc.request.UgcTagQueryRequest;
import com.youyi.runner.ugc.model.UgcMetadataResponse;
import com.youyi.runner.ugc.model.UgcTagInfoResponse;
import com.youyi.runner.ugc.util.UgcMetadataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.youyi.domain.ugc.assembler.UgcMetadataAssembler.UGC_METADATA_ASSEMBLER;
import static com.youyi.runner.ugc.util.UgcMetadataResponseUtil.queryUgcArticleTagWithCursorSuccess;
import static com.youyi.runner.ugc.util.UgcMetadataResponseUtil.queryUgcCategorySuccess;
import static com.youyi.runner.ugc.util.UgcMetadataResponseUtil.queryUgcInterestTagSuccess;

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
    @RequestMapping(value = "/metadata/interest-tag", method = RequestMethod.GET)
    public Result<UgcMetadataResponse> queryUgcInterestTag() {
        UgcMetadataDO ugcMetadataDO = ugcMetadataHelper.queryUgcInterestTag();
        return queryUgcInterestTagSuccess(ugcMetadataDO);
    }

    @SaCheckLogin
    @RequestMapping(value = "/metadata/article-tag", method = RequestMethod.GET)
    public Result<PageCursorResult<Long, UgcTagInfoResponse>> queryUgcArticleTagWithCursor(UgcTagQueryRequest request) {
        UgcMetadataValidator.checkUgcTagQueryRequest(request);
        UgcMetadataDO ugcMetadataDO = UGC_METADATA_ASSEMBLER.toDO(request);
        ugcMetadataHelper.queryUgcArticleTagWithCursor(ugcMetadataDO);
        return queryUgcArticleTagWithCursorSuccess(ugcMetadataDO, request);
    }

}
