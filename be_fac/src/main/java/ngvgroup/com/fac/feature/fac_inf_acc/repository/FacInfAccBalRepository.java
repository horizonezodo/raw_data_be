package ngvgroup.com.fac.feature.fac_inf_acc.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBal;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacInfAccBalRepository  extends BaseRepository<FacInfAccBal>{
    Optional<FacInfAccBal> findByAccNo(String accNo);
    List<FacInfAccBal> findByAccNoIn(Collection<String> accNos);

}
