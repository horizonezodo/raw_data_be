package ngvgroup.com.loan.feature.type_of_capital_use.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.type_of_capital_use.dto.LnmCfgCapitalUseDTO;
import ngvgroup.com.loan.feature.type_of_capital_use.model.LnmCfgCapitalUse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LnmCfgCapitalUseRepository extends BaseRepository<LnmCfgCapitalUse> {
    Optional<LnmCfgCapitalUse> findByCapitalUseCodeAndOrgCode(String code,String orgCode);

    @Query("""
        SELECT new ngvgroup.com.loan.feature.type_of_capital_use.dto.LnmCfgCapitalUseDTO(
            l.id,
            l.capitalUseCode,
            l.capitalUseName,
            l.description
        )FROM LnmCfgCapitalUse l
        WHERE (
            :keyword is null or
             LOWER(l.capitalUseCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
             LOWER(l.capitalUseName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
             LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
""")
    Page<LnmCfgCapitalUseDTO> search(@Param("keyword")String keyword, Pageable pageable);

    @Query("SELECT l.capitalUseCode from LnmCfgCapitalUse l")
    List<String> getAllCapitalCode();
}
