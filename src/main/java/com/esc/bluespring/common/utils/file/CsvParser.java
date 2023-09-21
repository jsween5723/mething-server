package com.esc.bluespring.common.utils.file;

import com.esc.bluespring.common.utils.file.exception.FileException.FileFormatException;
import com.esc.bluespring.common.utils.file.exception.FileException.FileParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class CsvParser {

  private static final String TYPE = "text/csv";
  private static final Charset ENCODE = StandardCharsets.UTF_8;

  private boolean isCSVtype(final MultipartFile file) {
    return TYPE.equals(file.getContentType());
  }

  public List<CSVRecord> getCSVrecords(final MultipartFile file) {
    if(!isCSVtype(file)) {
      throw new FileFormatException();
    }
    try {
      InputStream is = file.getInputStream();
      BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, ENCODE));
      CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
          .setHeader()
          .setSkipHeaderRecord(true)
          .setIgnoreHeaderCase(true)
          .setTrim(true)
          .build();
      CSVParser csvParser = new CSVParser(fileReader, csvFormat);
      return csvParser.getRecords();
    } catch (IOException e) {
      throw new FileParseException();
    }
  }

}
