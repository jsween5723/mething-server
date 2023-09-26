package com.esc.bluespring.domain.university.major.classes;

import com.esc.bluespring.domain.university.entity.University;
import com.esc.bluespring.domain.university.major.classes.MajorDto.SearchListElement;
import com.esc.bluespring.domain.university.major.entity.Major;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MajorMapper {
    MajorMapper INSTANCE = Mappers.getMapper(MajorMapper.class);
    String PK_FIELD = "식별번호";
    String MAJOR_NAME_FIELD = "학과명";
    String STATUS_FIELD = "학과상태명";
    Set<String> DISABLE_STATUSES = Set.of("폐과");

    SearchListElement toSearchListElement(Major major);

//    public List<Major> convertCSVtoMajor(final List<CSVRecord> records) {
//        return records.stream()
////            .filter(record -> !DISABLE_SCHOOL_SORTS.contains(record.get(SCHOOL_SORT_FIELD)))
//            .filter(r -> !DISABLE_STATUSES.contains(r.get(STATUS_FIELD)))
//            .map(r -> Major.builder()
//                .name(r.get(MAJOR_NAME_FIELD))
//                .id(Long.valueOf(r.get(PK_FIELD)))
//                .university(universityMapper.toEntity(r))
//                .build()).toList();
//    }


    Major toEntity(final String name, final University university);

    Major toEntity(Long id);

}
