package ngvgroup.com.fac.feature.fac_cfg_voucher.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import feign.Param;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherRuleN0;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacCfgVoucherRuleN0Repository extends BaseRepository<FacCfgVoucherRuleN0> {

    Optional<FacCfgVoucherRuleN0> findByVoucherTypeCode(String voucherTypeCode);

    @Query("SELECT e.voucherTypeName FROM FacCfgVoucherRuleN0 e WHERE e.voucherTypeCode = :code")
    String getVoucherTypeNameByVoucherTypeCode(@Param("code") String code);
}
