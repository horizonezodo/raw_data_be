package ngvgroup.com.fac.feature.fac_cfg_voucher.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import feign.Param;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherPrint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacCfgVoucherPrintRepository extends BaseRepository<FacCfgVoucherPrint> {
    @Query("SELECT t.templateCode FROM FacCfgVoucherPrint t WHERE t.voucherTypeCode = :voucherTypeCode")
    Optional<String> findTemplateCode(@Param("voucherTypeCode") String voucherTypeCode);
}
