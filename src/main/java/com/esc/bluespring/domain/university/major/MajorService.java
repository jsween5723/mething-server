package com.esc.bluespring.domain.university.major;

import com.esc.bluespring.domain.university.major.classes.MajorDto.SearchCondition;
import com.esc.bluespring.domain.university.major.entity.Major;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;
//    private final UniversityService universityService;
//    private final UniversityMapper universityMapper;

//    @Transactional
//    public List<Major> saveAllWithMajorCSV(List<Major> majors) {
//        Map<University, University> indexes = universityMapper.getIndexes(
//            universityService.findAll());
//        majorRepository.deleteAll();
//        majors.forEach(major -> {
//            University university = indexes.get(major.getUniversity());
//            major.changeUniversity(university);
//        });
//        return majorRepository.saveAll(majors);
//    }

    @Transactional(readOnly = true)
    public Slice<Major> search(SearchCondition condition, Pageable pageable) {
        return majorRepository.search(condition, pageable);
    }
}
