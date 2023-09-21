package com.esc.bluespring.domain.university.major.classes;

import com.esc.bluespring.domain.university.classes.UniversityMapper;
import com.esc.bluespring.domain.university.major.classes.MajorDto.SearchListElement;
import com.esc.bluespring.domain.university.major.entity.Major;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = ComponentModel.SPRING)
public abstract class MajorMapper {
    UniversityMapper universityMapper = Mappers.getMapper(UniversityMapper.class);
    private static final String PK_FIELD = "식별번호";
    private static final String MAJOR_NAME_FIELD = "학과명";
    private static final String STATUS_FIELD = "학과상태명";
    private static final Set<String> DISABLE_STATUSES = Set.of("폐과");

    public abstract SearchListElement toSearchListElement(Major major);

    public List<Major> convertCSVtoMajor(final List<CSVRecord> records) {
        return records.stream()
//            .filter(record -> !DISABLE_SCHOOL_SORTS.contains(record.get(SCHOOL_SORT_FIELD)))
            .filter(r -> !DISABLE_STATUSES.contains(r.get(STATUS_FIELD)))
            .map(r -> Major.builder()
                .name(r.get(MAJOR_NAME_FIELD))
                .id(Long.valueOf(r.get(PK_FIELD)))
                .university(universityMapper.toEntity(r))
                .build()).toList();
    }


    public Major toEntity(final String name,
        final com.esc.bluespring.domain.university.entity.University university) {
        return Major.builder().name(name).university(university).build();
    }

    public Major toEntity(Long id) {
        return Major.builder().id(id).build();
    }

}
