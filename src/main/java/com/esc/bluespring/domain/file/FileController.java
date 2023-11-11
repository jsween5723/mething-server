package com.esc.bluespring.domain.file;

import static com.esc.bluespring.domain.member.entity.Member.ANONYMOUS;

import com.esc.bluespring.common.BaseResponse;
import com.esc.bluespring.common.utils.file.S3Service;
import com.esc.bluespring.domain.file.entity.Image;
import com.esc.bluespring.domain.file.model.FileDto.FileUrlResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final S3Service s3Service;

    @PostMapping(value = "image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RolesAllowed({ANONYMOUS})
    public BaseResponse<FileUrlResponse> uploadImage(@NotNull MultipartFile file) {
        Image image = s3Service.upload(file);
        return new BaseResponse<>(new FileUrlResponse(image.getUrl()));
    }
}
