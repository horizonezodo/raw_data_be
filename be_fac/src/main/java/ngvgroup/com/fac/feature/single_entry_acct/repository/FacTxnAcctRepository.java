package ngvgroup.com.fac.feature.single_entry_acct.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.single_entry_acct.dto.SingleEntryAcctResDetailDTO;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacTxnAcctRepository extends BaseRepository<FacTxnAcct> {


    Optional<FacTxnAcct> findByProcessInstanceCode(String processInstanceCode);

    @Query("""
        SELECT distinct new ngvgroup.com.fac.feature.single_entry_acct.dto.SingleEntryAcctResDetailDTO (
               en.voucherTypeCode, en.processInstanceCode, en.entryTypeCode, en.entryForeignAmt,
               f.totalForeignAmt, f.txnDate, f.txnContent,
               f.orgCode, f.txnTime, f.objectTypeCode, f.objectTxnCode, f.objectTxnName,
               f.identificationId, f.issueDate, f.issuePlace, f.address,
               dtl.accClassCode, dtl.entryType, dtl.accNo, dtl.accCoaCode, dtl.lineForeignAmt, dtl.lineBaseAmt,
               dtl.id, inf.balAvailable, inf.balActual)
         from FacTxnAcct f
         left join FacTxnAcctEntry en on en.processInstanceCode = f.processInstanceCode
         left join FacTxnAcctEntryDtl dtl on dtl.processInstanceCode = en.processInstanceCode
         left join FacInfAcc inf on dtl.accNo = inf.accNo
         WHERE f.processInstanceCode = :processInstanceCode
    """)
    List<SingleEntryAcctResDetailDTO> getListByProcessInstanceCode(@Param("processInstanceCode") String processInstanceCode);
}
