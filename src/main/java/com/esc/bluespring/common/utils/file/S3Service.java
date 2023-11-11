package com.esc.bluespring.common.utils.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.esc.bluespring.common.utils.file.exception.FileException.S3DeleteFailException;
import com.esc.bluespring.common.utils.file.exception.FileException.S3FetchFailException;
import com.esc.bluespring.common.utils.file.exception.FileException.S3UploadFailException;
import com.esc.bluespring.domain.file.FileRepository;
import com.esc.bluespring.domain.file.entity.FileMetadata;
import com.esc.bluespring.domain.file.entity.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final FileRepository repository;
  private static final String AWS_DOMAIN = ".amazonaws.com/";
  private final Directory directory = S3Directory.MEMBER;
  @Transactional
  public Image upload(MultipartFile file) {
    if (file == null) {
      return null;
    }
    Image metadata = Image.builder().file(file).build();
    String fileName = directory.getDirName() + metadata.getFileName();
    saveFile(file, fileName);
    metadata.changeUrl(getUrl(fileName));
    repository.save(metadata);
    return metadata;
  }

  public void remove(String url) {
    deleteFile(url);
    repository.deleteByUrl(url);
  }

  public FileMetadata findFile(String url) {
    return repository.findByUrl(url);
  }
  protected void saveFile(MultipartFile file, String fileName) {
    try (InputStream inputStream = file.getInputStream()) {
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(file.getContentType());
      objectMetadata.setContentLength(file.getSize());
      amazonS3Client.putObject(bucket, fileName, inputStream, objectMetadata);
    } catch (IOException e) {
      throw new S3UploadFailException();
    }
  }

  protected String getUrl(String name) {
    URL resultUrl = amazonS3Client.getUrl(bucket, name);
    if (resultUrl == null) {
      throw new S3FetchFailException();
    }
    return resultUrl.toString();
  }

  protected void deleteFile(String url) {
    String[] fileURL = url.split(AWS_DOMAIN);
    String key = fileURL[fileURL.length - 1];
    if (!amazonS3Client.doesObjectExist(bucket, key)) {
      throw new S3DeleteFailException();
    }
    amazonS3Client.deleteObject(bucket, key);
  }
}
