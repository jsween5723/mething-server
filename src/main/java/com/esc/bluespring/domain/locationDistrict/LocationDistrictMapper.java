package com.esc.bluespring.domain.locationDistrict;

import com.esc.bluespring.common.LazyLoadingAwareMapper;
import com.esc.bluespring.domain.locationDistrict.classes.LocationDistrictDto.NoParent;
import com.esc.bluespring.domain.locationDistrict.classes.LocationDistrictDto.SearchListElement;
import com.esc.bluespring.domain.locationDistrict.entity.LocationDistrict;
import java.util.regex.Pattern;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationDistrictMapper extends LazyLoadingAwareMapper {
    LocationDistrictMapper INSTANCE = Mappers.getMapper(LocationDistrictMapper.class);
    String SI_DO = "시도명";
    String SI_DO_CODE = "시도코드";
    String SI_GUN_GU = "시군구명";
    String SI_GUN_GU_CODE = "시군구코드";
    Pattern NAME_REGEX = Pattern.compile(".*[시군]$");
//    @Autowired
//    private LocationDistrictIndex index;

    abstract SearchListElement toSearchListElement(LocationDistrict locationDistrict);
    abstract NoParent toNoParent(LocationDistrict locationDistrict);
    abstract LocationDistrict toEntity(Long id, String name);

//    default List<LocationDistrict> toEntitiesWithMajorCSV(List<CSVRecord> records) {
//        return records.stream().map(this::toEntities).distinct().toList();
//    }

//    private LocationDistrict registerLocation(LocationDistrict parent, LocationDistrict child) {
//        if (index.isNotIndexed(parent.getId())) {
//            index.put(parent.getId(), parent);
//        }
//        parent = index.get(parent.getId());
//        if (!parent.isParentOf(child)) {
//            parent.addChildren(child);
//        }
//        return parent;
//    }

//    private LocationDistrict toEntities(CSVRecord record) {
//        LocationDistrict parent = toEntity(Long.valueOf(record.get(SI_DO_CODE)), record.get(SI_DO));
//        LocationDistrict child = toEntity(
//            Long.valueOf(record.get(SI_DO_CODE) + record.get(SI_GUN_GU_CODE)),
//            record.get(SI_GUN_GU));
//        return registerLocation(parent, child);
//    }

//    public LocationDistrict toEntity(CSVRecord record) {
//        LocationDistrict parent = toEntity(Long.valueOf(record.get(SI_DO_CODE)), record.get(SI_DO));
//        LocationDistrict child = toEntity(
//            Long.valueOf(record.get(SI_DO_CODE) + record.get(SI_GUN_GU_CODE)),
//            record.get(SI_GUN_GU));
//        parent = registerLocation(parent, child);
//        if (NAME_REGEX.matcher(parent.getName()).matches()) {
//            return parent;
//        }
//        return parent.getChildren().stream().filter(c -> c.equals(child)).findFirst().get();
//    }

    @Condition
    default boolean isNotLazyLoadedParent(
        LocationDistrict parent) {
        return isNotLazyLoaded(parent);
    }
}
