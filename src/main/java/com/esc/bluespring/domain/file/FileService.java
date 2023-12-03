package com.esc.bluespring.domain.file;

import com.esc.bluespring.domain.file.entity.Image;
import com.esc.bluespring.domain.file.entity.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
    private final ImageRepository imageRepository;

    public Image findByUrl(String url) {
        return imageRepository.findByUrl(url);
    }
}
