package ngvgroup.com.loan.feature.product_proccess.repository.txn;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.product_proccess.model.txn.LnmTxnProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LnmTxnProductRepository extends BaseRepository<LnmTxnProduct> {

    boolean existsByLnmProductCode(String lnmProductCode);

    Optional<LnmTxnProduct> findByProcessInstanceCode(String processInstanceCode);

    List<LnmTxnProduct> findAllByLnmProductCode(String lnmProductCode);

    @Query("""
                SELECT l FROM LnmTxnProduct l
                WHERE l.lnmProductCode = :lnmProductCode
                AND l.businessStatus NOT IN (:businessStatus)
            """)
    Optional<LnmTxnProduct> findByLnmProductCode(@Param("lnmProductCode")String lnmProductCode, @Param("businessStatus") List<String> businessStatus);
}
