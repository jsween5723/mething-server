package com.esc.bluespring.domain.university.major;

import com.esc.bluespring.common.BaseResponse;
import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.university.major.classes.MajorDto.SearchCondition;
import com.esc.bluespring.domain.university.major.classes.MajorDto.SearchListElement;
import com.esc.bluespring.domain.university.major.classes.MajorMapper;
import com.esc.bluespring.domain.university.major.entity.Major;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
    private final MajorMapper majorMapper = MajorMapper.INSTANCE;

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
    @Operation(description = "회원가입 등에서 사용되는 학과 검색 API")
    public BaseResponse<CustomSlice<SearchListElement>> search(@ParameterObject SearchCondition condition,
                                                              @ParameterObject Pageable pageable) {
        Slice<Major> result = majorService.search(condition, pageable);
        return new BaseResponse<>(new CustomSlice<>(result.map(majorMapper::toSearchListElement)));
    }
}
