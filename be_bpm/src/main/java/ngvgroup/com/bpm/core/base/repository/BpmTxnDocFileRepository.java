package ngvgroup.com.bpm.core.base.repository;

import ngvgroup.com.bpm.core.base.dto.FileDto;
import ngvgroup.com.bpm.core.base.model.BpmTxnDocFile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BpmTxnDocFileRepository extends JpaRepository<BpmTxnDocFile, Long> {

    List<BpmTxnDocFile> findByTaskId(String taskId);

    List<BpmTxnDocFile> findByProcessInstanceCode(String processInstanceCode);

    java.util.Optional<BpmTxnDocFile> findTopByProcessInstanceCodeOrderByIdDesc(String processInstanceCode);

    @Query("""
             SELECT new ngvgroup.com.bpm.core.base.dto.FileDto(
                 f.fileId,
                 f.fileName,
                 f.filePath,
                 f.fileSize
             ) FROM BpmTxnDocFile f
             WHERE f.fileId = :fileId
            """)
    FileDto findByFileId(@Param("fileId") String fileId);

    @Query("""
            SELECT new ngvgroup.com.bpm.core.base.dto.TaskRefDto(
                t.taskId,
                t.processInstanceCode
            )
            FROM BpmTxnDocFile t
            WHERE t.processInstanceCode = (
                SELECT pi.processInstanceCode
                FROM BpmTxnProcessInstance pi
                WHERE pi.businessStatus = 'COMPLETE'
                  AND pi.processInstanceCode IN (
                      SELECT DISTINCT df.processInstanceCode
                      FROM BpmTxnDocFile df
                      WHERE df.referenceCode = :referenceCode
                  )
                  AND pi.modifiedDate = (
                      SELECT MAX(pi2.modifiedDate)
                      FROM BpmTxnProcessInstance pi2
                      WHERE pi2.businessStatus = 'COMPLETE'
                        AND pi2.processInstanceCode IN (
                            SELECT DISTINCT df2.processInstanceCode
                            FROM BpmTxnDocFile df2
                            WHERE df2.referenceCode = :referenceCode
                        )
                  )
            )
            AND t.modifiedDate = (
                SELECT MAX(t2.modifiedDate)
                FROM BpmTxnDocFile t2
                WHERE t2.processInstanceCode = t.processInstanceCode
            )
            """)
    List<ngvgroup.com.bpm.core.base.dto.TaskRefDto> findLatestTaskInfoByReference(
            @Param("referenceCode") String referenceCode);

    @Modifying
    @Transactional
    void deleteByProcessInstanceCode(String processInstanceCode);
}
