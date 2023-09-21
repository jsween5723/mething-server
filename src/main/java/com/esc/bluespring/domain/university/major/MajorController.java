package com.esc.bluespring.domain.university.major;

import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.university.major.classes.MajorDto.SearchCondition;
import com.esc.bluespring.domain.university.major.classes.MajorDto.SearchListElement;
import com.esc.bluespring.domain.university.major.classes.MajorMapper;
import com.esc.bluespring.domain.university.major.entity.Major;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/majors")
public class MajorController {

    private final MajorService majorService;
    //  private final CsvParser csvParser;
    private final MajorMapper majorMapper;

    //  @PostMapping("/majors/csv")
//  @Operation(summary = "csv 파일을 이용해 전국 대학교별 학과 정보를 일괄 등록한다.")
//  public void createMajors(
//      @RequestPart MultipartFile file
//  ) {
//    List<CSVRecord> records = csvParser.getCSVrecords(file);
//    List<Major> majors = majorMapper.convertCSVtoMajor(records);
//    majorService.saveAllWithMajorCSV(majors);
//  }
    @GetMapping
    public CustomSlice<SearchListElement> search(SearchCondition condition, Pageable pageable) {
        Slice<Major> result = majorService.search(condition, pageable);
        return new CustomSlice<>(result.map(majorMapper::toSearchListElement));
    }
}
