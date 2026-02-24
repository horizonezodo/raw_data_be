package ngvgroup.com.rpt.features.dim.repository;

import ngvgroup.com.rpt.features.dim.model.DimProvD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DimProvDRepository extends JpaRepository<DimProvD,Long> {

    @Query("""
                SELECT d
                FROM DimProvD d
                INNER JOIN DimCiBrD dcbd on dcbd.provinceCode = d.provinceCode
                WHERE dcbd.ciId = :ciId
            """)
    List<DimProvD> getAll(@Param("ciId") String ciId);
}
