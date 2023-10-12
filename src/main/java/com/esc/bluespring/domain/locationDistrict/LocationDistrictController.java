package com.esc.bluespring.domain.locationDistrict;

import com.esc.bluespring.common.CustomSlice;
import com.esc.bluespring.domain.locationDistrict.classes.LocationDistrictDto.SearchCondition;
import com.esc.bluespring.domain.locationDistrict.classes.LocationDistrictDto.SearchListElement;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/location-districts")
@RequiredArgsConstructor
class LocationDistrictController {

    private final LocationDistrictService locationDistrictService;
    //  private final CsvParser csvParser;
    private final LocationDistrictMapper mapper = LocationDistrictMapper.INSTANCE;

    //  @PostMapping("csv")
//  @ResponseStatus(HttpStatus.CREATED)
//  @Operation(summary = "csv를 업로드해 초기 데이터를 구축합니다.")
//  public void csvUpload(MultipartFile csvFile) {
//    List<CSVRecord> records = csvParser.getCSVrecords(csvFile);
//    List<LocationDistrict> entities = mapper.toEntitiesWithMajorCSV(records);
//    locationDistrictService.saveWithCSV(entities);
//  }
    @GetMapping
    @Operation(description = "[사용처 없음] 지역 검색")
    public CustomSlice<SearchListElement> searchListElements(
        @ParameterObject SearchCondition condition, @ParameterObject Pageable pageable) {
        Slice<SearchListElement> result = locationDistrictService.search(condition, pageable)
            .map(mapper::toSearchListElement);
        return new CustomSlice<>(result);
    }
}
