package ngvgroup.com.loan.feature.product_proccess.repository.cfg;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.product_proccess.model.cfg.LnmCfgProductDtl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LnmCfgProductDtlRepository extends BaseRepository<LnmCfgProductDtl> {

    void deleteAllByLnmProductCode(String lnmProductCode);

    List<LnmCfgProductDtl> findAllByLnmProductCode(String lnmProductCode);
}
