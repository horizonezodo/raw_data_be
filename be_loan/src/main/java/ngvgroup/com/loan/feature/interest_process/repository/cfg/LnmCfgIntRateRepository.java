package ngvgroup.com.loan.feature.interest_process.repository.cfg;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.interest_process.dto.LnmCfgIntRateDTO;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LnmCfgIntRateRepository extends BaseRepository<LnmCfgIntRate> {

    @Query("""
                SELECT new ngvgroup.com.loan.feature.interest_process.dto.LnmCfgIntRateDTO(
                    d.id,
                    d.orgCode,
                    d.interestRateCode,
                    d.interestRateName,
                    cvt.commonName,
                    d.currencyCode,
                    CASE
                        WHEN d.isActive = 1 THEN 'Đang hiệu lực'
                        ELSE 'Hết hiệu lực'
                    END
                )
                FROM LnmCfgIntRate d
                LEFT JOIN CtgComCommon cvt
                    ON cvt.commonCode = d.interestRateType
                WHERE d.isDelete = 0
                  AND (:commonCodes IS NOT NULL
                       AND d.interestRateType IN :commonCodes)
                   AND (
                          :keyword IS NULL OR :keyword = ''
                          OR LOWER(d.interestRateCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(d.interestRateName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(cvt.commonName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(d.currencyCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(
                               CASE
                                   WHEN d.isActive = 1 THEN 'Đang hiệu lực'
                                   ELSE 'Hết hiệu lực'
                               END
                          ) LIKE LOWER(CONCAT('%', :keyword, '%'))
                     )
                ORDER BY d.modifiedDate DESC     
            """)
    Page<LnmCfgIntRateDTO> search(
            @Param("keyword") String keyword,
            @Param("commonCodes") List<String> commonCodes,
            Pageable pageable
    );

    @Query("""
    SELECT new ngvgroup.com.loan.feature.interest_process.dto.LnmCfgIntRateDTO(
                    d.id,
                    d.orgCode,
                    d.interestRateCode,
                    d.interestRateName,
                    cvt.commonName,
                    d.currencyCode,
                    CASE
                        WHEN d.isActive = 1 THEN 'Đang hiệu lực'
                        ELSE 'Hết hiệu lực'
                    END
                )
                FROM LnmCfgIntRate d
                LEFT JOIN CtgComCommon cvt
                    ON cvt.commonCode = d.interestRateType
                 WHERE d.isDelete = 0    
""")
    List<LnmCfgIntRateDTO> exportExcelData();


    Boolean existsByInterestRateCode(String interestRateCode);

    Optional<LnmCfgIntRate> findByInterestRateCode(String interestRateCode);
}