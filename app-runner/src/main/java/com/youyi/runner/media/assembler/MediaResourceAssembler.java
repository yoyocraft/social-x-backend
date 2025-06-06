package com.youyi.runner.media.assembler;

import com.youyi.domain.media.model.MediaResourceDO;
import com.youyi.domain.media.type.MediaSource;
import com.youyi.domain.media.type.ResourceType;
import com.youyi.runner.media.model.ImageUploadRequest;
import java.io.File;
import org.apache.commons.io.FilenameUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import static com.youyi.common.constant.SymbolConstant.DOT;
import static com.youyi.common.util.ext.MoreFeatures.runCatching;
import static com.youyi.common.util.seq.IdSeqUtil.genTmpFileName;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/17
 */
@Mapper(
    imports = {
        MediaSource.class,
        ResourceType.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MediaResourceAssembler {

    MediaResourceAssembler MEDIA_RESOURCE_ASSEMBLER = Mappers.getMapper(MediaResourceAssembler.class);

    @Mappings({
        @Mapping(target = "media", expression = "java(toFile(file))"),
        @Mapping(target = "source", expression = "java(MediaSource.of(request.getSource()))"),
        @Mapping(target = "resourceType", expression = "java(ResourceType.IMAGE)")
    })
    MediaResourceDO toDO(ImageUploadRequest request, MultipartFile file);

    default File toFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(filename);
        File tmpFile = runCatching(() -> File.createTempFile(genTmpFileName(), DOT + extension));
        runCatching(() -> file.transferTo(tmpFile));
        return tmpFile;
    }
}
