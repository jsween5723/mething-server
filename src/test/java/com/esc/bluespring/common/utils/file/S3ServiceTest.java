package com.esc.bluespring.common.utils.file;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3Client;
import com.esc.bluespring.domain.file.FileRepository;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

  @Mock
  private MultipartFile file;

  @Mock
  private S3Directory type;

  @Mock
  private AmazonS3Client amazonS3Client;

  @InjectMocks
  private S3Service s3Service;

  @Mock
  private FileRepository repository;

  @Test
  @DisplayName("S3에 파일을 업로드한다.")
  void upload_s3() throws IOException {
    // given
    String name = "test.jpeg";
    String contentType = "image/jpeg";
    String uploadUrl = "https://s3.amazonaws.com/member/test/test.jpeg";
    URL url = new URL(uploadUrl);

    // when
    when(file.isEmpty()).thenReturn(false);
    when(file.getContentType()).thenReturn(contentType);
    when(file.getInputStream()).thenReturn(getClass().getResourceAsStream(name));
    when(amazonS3Client.putObject(any(), any(), any(), any())).thenReturn(null);
    when(amazonS3Client.getUrl(any(), any())).thenReturn(url);
    s3Service.upload(file);

    //then
    verify(amazonS3Client, times(1)).putObject(any(), any(), any(), any());
  }

  @Test
  @DisplayName("S3에서 파일을 삭제한다.")
  void delete_s3() {
    // given
    String uploadUrl = "https://s3.amazonaws.com/member/test/test.jpeg";

    // when
    when(amazonS3Client.doesObjectExist(any(), any())).thenReturn(true);
    s3Service.deleteFile(uploadUrl);

    //then
    verify(amazonS3Client, times(1)).doesObjectExist(any(), any());
    verify(amazonS3Client, times(1)).deleteObject(any(), any());
  }

}
