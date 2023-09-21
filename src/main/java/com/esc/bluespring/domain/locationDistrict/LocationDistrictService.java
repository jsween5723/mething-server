package com.esc.bluespring.domain.locationDistrict;

import com.esc.bluespring.domain.locationDistrict.classes.LocationDistrictDto.SearchCondition;
import com.esc.bluespring.domain.locationDistrict.entity.LocationDistrict;
import com.esc.bluespring.domain.locationDistrict.repository.LocationDistrictRepository;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationDistrictService {

    private final LocationDistrictRepository repository;

//    @Transactional
//    public List<LocationDistrict> saveWithCSV(List<LocationDistrict> entities) {
//        repository.deleteAll();
//        return repository.saveAll(entities);
//    }

    @Transactional(readOnly = true)
    public Map<Long, LocationDistrict> getRoots() {
        return repository.findAll().stream()
            .collect(Collectors.toMap(LocationDistrict::getId, l -> l));
    }

    @Transactional
    public Slice<LocationDistrict> search(SearchCondition condition, Pageable pageable) {
        return repository.search(condition, pageable);
    }
}