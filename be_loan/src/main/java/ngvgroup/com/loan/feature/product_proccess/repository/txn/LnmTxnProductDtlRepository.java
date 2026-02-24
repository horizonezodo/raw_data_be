package ngvgroup.com.loan.feature.product_proccess.repository.txn;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.product_proccess.model.txn.LnmTxnProductDtl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LnmTxnProductDtlRepository extends BaseRepository<LnmTxnProductDtl> {

    List<LnmTxnProductDtl> findByProcessInstanceCode(String processInstanceCode);
}
