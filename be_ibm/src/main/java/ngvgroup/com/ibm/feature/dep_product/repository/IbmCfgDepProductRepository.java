package ngvgroup.com.ibm.feature.dep_product.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.ibm.feature.dep_product.dto.IbmCfgDepProductDTO;
import ngvgroup.com.ibm.feature.dep_product.model.IbmCfgDepProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IbmCfgDepProductRepository extends BaseRepository<IbmCfgDepProduct> {
    Optional<IbmCfgDepProduct> findByIbmDepProductCode(String ibmDepProductCode);

    @Query("""
        SELECT new ngvgroup.com.ibm.feature.dep_product.dto.IbmCfgDepProductDTO(
            p.id, p.ibmDepProductCode, p.ibmDepProductName, c1.commonName, c2.commonName,
            c3.commonName
        )
        FROM IbmCfgDepProduct p
        JOIN IbmCfgDepIntRate r ON p.interestRateCode = r.interestRateCode
        LEFT JOIN CtgComCommon c1 ON p.ibmDepProductTypeCode = c1.commonCode
        LEFT JOIN CtgComCommon c2 ON p.interestPaymentMethod = c2.commonCode
        LEFT JOIN CtgComCommon c3 ON r.interestRateType = c3.commonCode
        WHERE (:ibmDepProductTypeCode IS NULL OR p.ibmDepProductTypeCode in :ibmDepProductTypeCode)
           AND ( :keyword IS NULL
             OR LOWER(p.ibmDepProductCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(p.ibmDepProductName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(c1.commonName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(c2.commonName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(c3.commonName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           )
    """)
    Page<IbmCfgDepProductDTO> search(String keyword, List<String> ibmDepProductTypeCode, Pageable pageable);


}