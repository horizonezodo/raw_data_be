package ngvgroup.com.ibm.feature.dep_product.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.ibm.feature.dep_product.dto.IbmCfgDepProductDtlDTO;
import ngvgroup.com.ibm.feature.dep_product.model.IbmCfgDepProductDtl;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IbmCfgDepProductDtlRepository extends BaseRepository<IbmCfgDepProductDtl> {
    List<IbmCfgDepProductDtl> findAllByIbmDepProductCode(String ibmDepProductCode);

    @Query("""
        SELECT new ngvgroup.com.ibm.feature.dep_product.dto.IbmCfgDepProductDtlDTO(
        d.id, d.ibmDepProductCode, d.orgCode, d.effectiveDate, d.interestCalcMethod,
        d.interestBaseCode, d.interestDateMethod, d.isPartialWithdraw, d.isEarlySettlement,
        d.interestStartMethod, d.maturityDateRule, c1.commonName, c2.commonName, c3.baseIntName)
        FROM IbmCfgDepProductDtl d 
        LEFT JOIN CtgComCommon c1 ON d.interestCalcMethod = c1.commonCode
        LEFT JOIN CtgComCommon c2 ON d.interestDateMethod = c2.commonCode
        LEFT JOIN ComCfgBaseInt c3 ON d.interestBaseCode = c3.baseIntCode
        WHERE d.ibmDepProductCode = :ibmDepProductCode
    """)
    List<IbmCfgDepProductDtlDTO> findAllDtlByIbmDepProductCode(String ibmDepProductCode);

    void deleteByIbmDepProductCode(String ibmDepProductCode);
}