package ngvgroup.com.hrm.feature.category.repository;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.hrm.feature.category.dto.HrmCfgTitleDTO;
import ngvgroup.com.hrm.feature.category.dto.ExportHrmCfgTitleDTO;
import ngvgroup.com.hrm.feature.category.model.HrmCfgTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HrmCfgTitleRepository extends BaseRepository<HrmCfgTitle> {
    @Query("""
        select new ngvgroup.com.hrm.feature.category.dto.HrmCfgTitleDTO(
        c.id, c.titleCode, c.titleName, c.orgCode, o.orgName, c.isActive, c.description)
    from HrmCfgTitle c 
    join ComInfOrganization o on c.orgCode = o.orgCode
    where (:keyword is null or :keyword = '' 
        or LOWER(c.titleCode) like concat('%', LOWER(:keyword), '%')
        or LOWER(c.titleName) like concat('%', LOWER(:keyword), '%')
        or LOWER(o.orgName) like concat('%', LOWER(:keyword), '%')
        or (
            (:keyword = 'hiệu lực' and c.isActive = 1)
            or (:keyword = 'hết hiệu lực' and c.isActive = 0)
        )
    )
    order by c.isActive desc, c.modifiedDate desc
    """)
    Page<HrmCfgTitleDTO> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    select new ngvgroup.com.hrm.feature.category.dto.ExportHrmCfgTitleDTO(
        c.titleCode, c.titleName, o.orgName, c.isActive)
    from HrmCfgTitle c 
    join ComInfOrganization o on c.orgCode = o.orgCode
    order by c.isActive desc, c.modifiedDate desc
    """)
    List<ExportHrmCfgTitleDTO> exportData();

    Optional<HrmCfgTitle> findByTitleCode(String titleCode);

    @Query("select c from HrmCfgTitle  c where c.isDelete = :isDelete and (c.titleCode = :titleCode or c.titleName = :titleName)")
    HrmCfgTitle findTitle(@Param("titleCode") String titleCode, @Param("titleName") String titleName, @Param("isDelete") int isDelete);

    boolean existsByTitleCode(String titleCode);

    @Query("""
        select count(p)
        from HrmCfgPosition p
        where p.titleCode = :titleCode 
    """)
    long checkUsed(String titleCode);

    void deleteByTitleCode(String titleCode);
}