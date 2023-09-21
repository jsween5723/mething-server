package com.esc.bluespring.domain.university;

import com.esc.bluespring.domain.locationDistrict.LocationDistrictService;
import com.esc.bluespring.domain.university.classes.UniversityDto.SearchCondition;
import com.esc.bluespring.domain.university.entity.University;
import com.esc.bluespring.domain.university.repository.UniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final LocationDistrictService locationDistrictService;

//    @Transactional
//    public List<University> saveAllWithMajorCSV(List<University> universities) {
//        universityRepository.deleteAll();
//        Map<Long, LocationDistrict> roots = locationDistrictService.getRoots();
//        universities.forEach(u -> {
//            LocationDistrict location = roots.get(u.getLocationDistrict().getId());
//            u.changeLocation(location);
//        });
//        return universityRepository.saveAll(universities);
//    }

    @Transactional(readOnly = true)
    public List<University> findAll() {
        return universityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Slice<University> search(SearchCondition condition, Pageable pageable) {
        return universityRepository.search(condition, pageable);
    }
}
