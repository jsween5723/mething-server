package com.esc.bluespring.domain.file.entity;

import com.esc.bluespring.common.utils.file.exception.FileException.FileContentTypeException;
import com.esc.bluespring.common.utils.file.exception.FileException.FileFormatException;
import com.esc.bluespring.common.utils.file.exception.FileException.NoAttachmentException;
import jakarta.persistence.Entity;
import java.util.Arrays;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends FileMetadata {

  private static final String CONTENT_TYPE = "image";
  private static final String[] SUPPORT_EXTENSION = {"jpg", "jpeg", "png"};
  public Image(String url) {
    super(url);
  }
  Image(MultipartFile file) {
    super(file);
  }


  protected void validate(MultipartFile multipartFile) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      throw new NoAttachmentException();
    }
    String contentType = multipartFile.getContentType();
    if (!Objects.requireNonNull(contentType).contains(CONTENT_TYPE)) {
      throw new FileContentTypeException(contentType);
    }
    if (!Arrays.asList(SUPPORT_EXTENSION).contains(extension)) {
      throw new FileFormatException(extension);
    }
  }
}