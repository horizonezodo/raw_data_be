package ngvgroup.com.crm.features.common.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.common.model.ComInfProvince;
import ngvgroup.com.crm.features.common.dto.ComInfProvinceDto;

public interface ComInfProvinceRepository extends BaseRepository<ComInfProvince> {

    @Query("""
        SELECT new ngvgroup.com.crm.features.common.dto.ComInfProvinceDto(
            e.provinceCode,
            e.provinceName
        )
        FROM ComInfProvince e
        WHERE e.isDelete = 0
        ORDER BY e.provinceCode ASC
        """)
    List<ComInfProvinceDto> findAllDto();
}