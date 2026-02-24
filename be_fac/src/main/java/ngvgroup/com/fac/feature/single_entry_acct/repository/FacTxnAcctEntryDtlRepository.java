package ngvgroup.com.fac.feature.single_entry_acct.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct_entry_dtl.FacTxnAcctEntryDtlDTO;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntryDtl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacTxnAcctEntryDtlRepository extends BaseRepository<FacTxnAcctEntryDtl> {
    List<FacTxnAcctEntryDtl> getFacTxnAcctEntryDtlsByTxnAcctEntryCode(String txnAcctEntryCode);


    List<FacTxnAcctEntryDtl> getAllByProcessInstanceCode(@Param("processInstanceCode") String processInstanceCode);

    @Query("""
            SELECT new ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct_entry_dtl.FacTxnAcctEntryDtlDTO(
                f.id,
                f.entryType,
                f.accNo,
                f.accClassCode,
                f.lineForeignAmt,
                f.orgCode,
                f.txnDate,
                f.txnAcctEntryDtlCode,
                f.processInstanceCode,
                f.currencyCode,
                f.lineBaseAmt,
                f.accCoaCode,
                f.businessStatus,
                f.txnAcctEntryCode,
                fi.accName,
                fi.balAvailable,
                fi.balActual
            )
            FROM FacTxnAcctEntryDtl f
            JOIN FacInfAcc fi
                ON f.accNo = fi.accNo
                WHERE f.processInstanceCode=:processInstanceCode
            """)
    List<FacTxnAcctEntryDtlDTO> getDetailDto(@Param("processInstanceCode") String processInstanceCode);

    List<FacTxnAcctEntryDtl> findAllByProcessInstanceCode(String processInstanceCode);
}
