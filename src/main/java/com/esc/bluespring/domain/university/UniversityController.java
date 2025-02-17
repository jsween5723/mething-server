package com.esc.bluespring.domain.university;

import com.esc.bluespring.common.BaseResponse;
import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.university.classes.UniversityDto.SearchCondition;
import com.esc.bluespring.domain.university.classes.UniversityDto.SearchListElement;
import com.esc.bluespring.domain.university.classes.UniversityMapper;
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
@RequestMapping("/api/v1/universities")
public class UniversityController {

    private final UniversityMapper universityMapper = UniversityMapper.INSTANCE;
    private final UniversityService universityService;
//  private final CsvParser csvParser;

    //  @PostMapping("/csv")
//  @Operation(summary = "csv 파일을 이용해 전국 대학교 정보를 일괄 등록한다.")
//  public void createUniversities(
//      @RequestPart MultipartFile file
//  ) {
//    List<CSVRecord> records = csvParser.getCSVrecords(file);
//    List<University> universities = universityMapper.toEntitiesWithMajorCSV(records);
//    universityService.saveAllWithMajorCSV(universities);
//  }
    @GetMapping
    @Operation(description = "회원가입 등에서 사용되는 학교 검색 API")
    public BaseResponse<CustomSlice<SearchListElement>> search(@ParameterObject SearchCondition condition,
                                                                  @ParameterObject Pageable pageable) {
        Slice<SearchListElement> result = universityService.search(condition, pageable)
            .map(universityMapper::toSearchListElement);
        return new BaseResponse<>(new CustomSlice<>(result));
    }
}
