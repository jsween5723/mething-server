package com.esc.bluespring.domain.university.classes;

import com.esc.bluespring.domain.locationDistrict.LocationDistrictMapper;
import com.esc.bluespring.domain.locationDistrict.entity.LocationDistrict;
import com.esc.bluespring.domain.university.classes.UniversityDto.SearchListElement;
import com.esc.bluespring.domain.university.entity.University;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVRecord;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = LocationDistrictMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = ComponentModel.SPRING)
public abstract class UniversityMapper {

    public static final String SCHOOL_SORT_FIELD = "학교구분명";
    private static final String NAME_DELIMITER = "[,\\s]+";
    private static final String SCHOOL_NAME_FIELD = "학교명";
    //    public static final Set<String> DISABLE_SCHOOL_SORTS = Set.of("특수대학원", "일반대학원", "전문대학원");
    @Autowired
    private LocationDistrictMapper locationDistrictMapper;

    @Mapping(target = "id", source = "id")
    public abstract University map(Long id);

    public abstract SearchListElement toSearchListElement(University university);

    public List<University> toEntitiesWithMajorCSV(List<CSVRecord> csvRecords) {
        return csvRecords.stream()
//            .filter(record -> !DISABLE_SCHOOL_SORTS.contains(record.get(SCHOOL_SORT_FIELD)))
            .map(this::toEntity).distinct().toList();
    }

    public University toEntity(CSVRecord record) {
        LocationDistrict locationDistrict = locationDistrictMapper.toEntity(record);
        return getUniversity(record.get(SCHOOL_NAME_FIELD), record.get(SCHOOL_SORT_FIELD),
            locationDistrict);
    }

    private University getUniversity(String name, String type, LocationDistrict locationDistrict) {
        String[] fullName = name.replaceAll("[()]", " ").trim().split(NAME_DELIMITER);
        String universityName = fullName[0];
        String campus;
        if (fullName.length == 1) {
            campus = "본교";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1; i < fullName.length; i++) {
                stringBuilder.append(" ").append(fullName[i]);
            }
            campus = stringBuilder.toString().trim();
        }
        return University.builder().name(universityName).type(type).campus(campus)
            .locationDistrict(locationDistrict).build();
    }

    public Map<University, University> getIndexes(List<University> universities) {
        return universities.stream().collect(Collectors.toMap(u -> u, u -> u));
    }
}
