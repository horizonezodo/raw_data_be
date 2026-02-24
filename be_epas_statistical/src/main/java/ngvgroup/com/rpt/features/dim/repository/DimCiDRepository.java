package ngvgroup.com.rpt.features.dim.repository;

import ngvgroup.com.rpt.features.dim.dto.DimCiDDTO;
import ngvgroup.com.rpt.features.dim.model.DimCiD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DimCiDRepository extends JpaRepository<DimCiD,Long> {
    @Query("""
        SELECT new ngvgroup.com.rpt.features.dim.dto.DimCiDDTO(
            d.ciId,
            d.ciCode,
            d.ciName
        )FROM DimCiD d
        WHERE d.isActive = 1
""")
    List<DimCiDDTO> getAllData();
}
