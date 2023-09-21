package com.esc.bluespring.common.utils.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class CsvParserTest {

  @Mock
  private MultipartFile file;

  @InjectMocks
  CsvParser csvParser;

  @Test
  @DisplayName("csv 파일에서 CSV records를 파싱한다.")
  void parse_csv_file() throws Exception {
    // given
    when(file.getContentType()).thenReturn("text/csv");
    when(file.getInputStream()).thenReturn(createInputStream());

    // when
    List<CSVRecord> records = csvParser.getCSVrecords(file);

    //then
    assertEquals(1, records.size());
    CSVRecord record = records.get(0);
    assertEquals("충북대학교", record.get("학교명"));
  }

  private InputStream createInputStream() {
    StringBuilder csvBuilder = new StringBuilder();
    csvBuilder.append("학교명\n");
    csvBuilder.append("충북대학교");
    return new ByteArrayInputStream(csvBuilder.toString().getBytes());
  }

}
