package com.esc.bluespring.domain.file.entity;

import com.esc.bluespring.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class FileMetadata extends BaseEntity {

  private static final String SEPARATOR = "/";
  protected String extension;
  @Column(length = 500)
  protected String url;
  private String originalName;
  private String fileName;
  private Long bytes;

  FileMetadata(MultipartFile file) {
    originalName = file.getOriginalFilename();
    extension = getExtension(file);
    fileName = UUID.randomUUID() + "."+extension;
    validate(file);
    bytes = file.getSize();
  }

  public FileMetadata(String url) {
      this.url = URLEncoder.encode(url, StandardCharsets.UTF_8);
  }

  public void changeUrl(String url) {
    this.url = url;
  }

  private String getExtension(MultipartFile file) {
    String contentType = file.getContentType();
    return contentType.substring(contentType.lastIndexOf(SEPARATOR) + 1).toLowerCase();
  }

  protected void validate(MultipartFile multipartFile) {
  }
}
