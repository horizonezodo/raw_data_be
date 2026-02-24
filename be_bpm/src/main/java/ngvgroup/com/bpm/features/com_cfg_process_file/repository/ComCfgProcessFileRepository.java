package ngvgroup.com.bpm.features.com_cfg_process_file.repository;

import ngvgroup.com.bpm.core.base.dto.ComCfgProcessFileDto;
import ngvgroup.com.bpm.core.base.dto.FileFlatDto;
import ngvgroup.com.bpm.features.com_cfg_process_file.model.ComCfgProcessFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComCfgProcessFileRepository extends JpaRepository<ComCfgProcessFile, Long> {
  @Query("""
         select new ngvgroup.com.bpm.core.base.dto.ComCfgProcessFileDto(
            f.processFileCode, f.processFileName, f.fileSize, f.isAvatar, f.isSent, f.isPrint, f.isUpload, f.fileType, f.fileTypeCode
         )
         from ComCfgProcessFile f
         where f.processTypeCode = :processTypeCode
           and f.isDelete = 0
           and f.isActive = 1
        order by f.sortNumber ASC
      """)
  List<ComCfgProcessFileDto> findByProcessTypeCode(@Param("processTypeCode") String processTypeCode);

  @Query("""
          SELECT new ngvgroup.com.bpm.core.base.dto.FileFlatDto(
              c.processFileCode,
              c.processFileName,
              d.fileId,
              d.fileName,
              d.filePath,
              d.fileSize
          )
          FROM ComCfgProcessFile c
          LEFT JOIN BpmTxnDocFile d ON c.processFileCode = d.processFileCode
                                    AND d.taskId = :taskId
                                    AND d.processInstanceCode = :processInstanceCode
          WHERE c.processTypeCode = :processTypeCode
            AND c.tabGroupCode LIKE :tabGroupCode
          ORDER BY c.sortNumber ASC, d.modifiedDate DESC
      """)
  List<FileFlatDto> findFilesByTaskId(@Param("taskId") String taskId,
      @Param("processInstanceCode") String processInstanceCode,
      @Param("processTypeCode") String processTypeCode,
      @Param("tabGroupCode") String tabGroupCode);
}
