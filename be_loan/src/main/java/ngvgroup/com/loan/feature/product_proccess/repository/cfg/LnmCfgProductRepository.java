package ngvgroup.com.loan.feature.product_proccess.repository.cfg;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.product_proccess.dto.LnmCfgProductDTO;
import ngvgroup.com.loan.feature.product_proccess.model.cfg.LnmCfgProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LnmCfgProductRepository extends BaseRepository<LnmCfgProduct> {

    @Query("""
        SELECT new ngvgroup.com.loan.feature.product_proccess.dto.LnmCfgProductDTO(
            d.id,
            d.orgCode,
            d.lnmProductCode,
            d.lnmProductName,
            cvt.commonName,
            ir.interestRateName,
            d.currencyCode,
            CASE
                WHEN d.isActive = 1 THEN 'Đang hiệu lực'
                ELSE 'Hết hiệu lực'
            END
        )
        FROM LnmCfgProduct d
        LEFT JOIN CtgComCommon cvt
            ON cvt.commonCode = d.loanTermTypeCode
        LEFT JOIN LnmCfgIntRate ir
            ON ir.interestRateCode = d.interestRateCode
        WHERE d.isDelete = 0
          AND (:commonCodes IS NULL OR d.loanTermTypeCode IN :commonCodes)
          AND (
                :keyword IS NULL OR :keyword = ''
                OR LOWER(d.lnmProductCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(d.lnmProductName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(cvt.commonName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(ir.interestRateName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(d.currencyCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(
                    CASE
                        WHEN d.isActive = 1 THEN 'Đang hiệu lực'
                        ELSE 'Hết hiệu lực'
                    END
                ) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
        ORDER BY d.approvedDate DESC
    """)
    Page<LnmCfgProductDTO> search(
            @Param("keyword") String keyword,
            @Param("commonCodes") List<String> commonCodes,
            Pageable pageable
    );


    Optional<LnmCfgProduct> findByLnmProductCode(String lnmProductCode);

}
