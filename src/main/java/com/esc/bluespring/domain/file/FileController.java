package com.esc.bluespring.domain.file;

import com.esc.bluespring.common.utils.file.S3Service;
import com.esc.bluespring.domain.file.entity.Image;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final S3Service s3Service;
    @PostMapping("image")
    public String uploadImage(@NotNull MultipartFile file) {
        Image image = s3Service.upload(file);
        return image.getUrl();
    }
}
