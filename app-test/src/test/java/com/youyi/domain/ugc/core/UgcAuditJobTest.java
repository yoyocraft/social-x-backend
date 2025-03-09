package com.youyi.domain.ugc.core;

import com.youyi.BaseIntegrationTest;
import com.youyi.common.type.ugc.UgcStatus;
import com.youyi.domain.ugc.repository.UgcRepository;
import com.youyi.domain.ugc.repository.document.UgcDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/03/09
 */
class UgcAuditJobTest extends BaseIntegrationTest {

    @Autowired
    UgcRepository ugcRepository;

    @Autowired
    UgcAuditJob ugcAuditJob;

    @Test
    void testDoAuditUgc() {
        String ugcId = "1898614509680074752";
        UgcDocument ugcDocument = Assertions.assertDoesNotThrow(() -> ugcRepository.queryByUgcId(ugcId));
        ugcDocument.setStatus(UgcStatus.AUDITING.name());
        Assertions.assertDoesNotThrow(() -> ugcAuditJob.doAuditUgc(ugcDocument));
    }
}
