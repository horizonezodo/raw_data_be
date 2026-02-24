package ngvgroup.com.loan.feature.scoring_type.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.feature.scoring_benchmark.model.CtgCfgScoringBenchmark;
import ngvgroup.com.loan.feature.scoring_benchmark.repository.CtgCfgScoringBenchmarkRepository;
import ngvgroup.com.loan.feature.scoring_indc_group.model.CtgCfgScoringIndcGroup;
import ngvgroup.com.loan.feature.scoring_indc_group.repository.CtgCfgScoringIndcGroupRepository;
import ngvgroup.com.loan.feature.scoring_type.dto.CtgCfgScoringTypeDTO;
import ngvgroup.com.loan.feature.scoring_type.model.CtgCfgScoringType;
import ngvgroup.com.loan.feature.scoring_type.repository.CtgCfgScoringTypeRepository;
import ngvgroup.com.loan.feature.scoring_type.service.CtgCfgScoringTypeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CtgCfgScoringTypeServiceImpl implements CtgCfgScoringTypeService {
    private final CtgCfgScoringTypeRepository ctgCfgScoringTypeRepository;
    private final MinioClient minioClient;
    private final CtgCfgScoringIndcGroupRepository ctgCfgScoringIndcGroupRepository;
    private final CtgCfgScoringBenchmarkRepository ctgCfgScoringBenchmarkRepository;
    private final ExportExcel exportExcel;

    @Value("${minio.bucket}")
    private String bucketName;

    @Override
    public List<CtgCfgScoringTypeDTO> getAllData() {
        return ctgCfgScoringTypeRepository.getAllData();
    }

    @Override
    public Page<CtgCfgScoringTypeDTO> pageData(String keyword, Pageable pageable) {
        return ctgCfgScoringTypeRepository.pageData(keyword, pageable);
    }

    @Override
    public List<CtgCfgScoringTypeDTO> getAll(){
        return ctgCfgScoringTypeRepository.getAll();
    }

    @Override
    public Page<CtgCfgScoringTypeDTO> searchAll(String keyword, Pageable pageable){
        return ctgCfgScoringTypeRepository.searchAll(keyword, pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(String keyword, String fileName, List<String> labels){
        try {
            List<CtgCfgScoringTypeDTO> ctgCfgScoringTypeDtos=ctgCfgScoringTypeRepository.exportToExcel(keyword);
            return exportExcel.exportExcel(ctgCfgScoringTypeDtos, fileName);
        }
        catch (Exception e) {
            throw new BusinessException(LoanErrorCode.WRITE_EXCEL_ERROR, e);
        }
    }

    @Override
    public void create(CtgCfgScoringTypeDTO scoringTypeDTO){
        Optional<CtgCfgScoringType> ctgCfgScoringType=ctgCfgScoringTypeRepository.findCtgCfgScoringTypeByScoringTypeCode(scoringTypeDTO.getScoringTypeCode());
        if(ctgCfgScoringType.isPresent()){
            throw new BusinessException(LoanErrorCode.SCORING_TYPE_ALREADY_EXISTS);
        }

        CtgCfgScoringType scoringType=new CtgCfgScoringType(
                scoringTypeDTO.getScoringTypeCode(),
                scoringTypeDTO.getScoringTypeName(),
                scoringTypeDTO.getDescription(),
                scoringTypeDTO.getSqlDataCollection(),
                scoringTypeDTO.getTemplateCollectionCode(),
                scoringTypeDTO.getSqlCalcResult(),
                scoringTypeDTO.getTemplateResultCode()
        );
        ctgCfgScoringTypeRepository.save(scoringType);
    }

    @Override
    @Transactional
    public void update(CtgCfgScoringTypeDTO scoringTypeDTO){
        Optional<CtgCfgScoringType> ctgCfgScoringType=ctgCfgScoringTypeRepository.findCtgCfgScoringTypeByScoringTypeCode(scoringTypeDTO.getScoringTypeCode());
        if(ctgCfgScoringType.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgCfgScoringType.get().setScoringTypeName(scoringTypeDTO.getScoringTypeName());
        ctgCfgScoringType.get().setDescription(scoringTypeDTO.getDescription());
        ctgCfgScoringType.get().setSqlDataCollection(scoringTypeDTO.getSqlDataCollection());
        ctgCfgScoringType.get().setTemplateCollectionCode(scoringTypeDTO.getTemplateCollectionCode());
        ctgCfgScoringType.get().setSqlCalcResult(scoringTypeDTO.getSqlCalcResult());
        ctgCfgScoringType.get().setTemplateResultCode(scoringTypeDTO.getTemplateResultCode());
        ctgCfgScoringTypeRepository.save(ctgCfgScoringType.get());
    }

    @Override
    public void delete(String scoringTypeCode){
        Optional<CtgCfgScoringType> ctgCfgScoringType=ctgCfgScoringTypeRepository.findCtgCfgScoringTypeByScoringTypeCode(scoringTypeCode);
        if(ctgCfgScoringType.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        List<CtgCfgScoringIndcGroup> ctgCfgScoringIndcGroups=ctgCfgScoringIndcGroupRepository.getCtgCfgScoringIndcGroupByScoringTypeCode(scoringTypeCode);
        if(!ctgCfgScoringIndcGroups.isEmpty()){
            throw new BusinessException(LoanErrorCode.SCORING_TYPE_IS_USE);
        }

        List<CtgCfgScoringBenchmark> ctgCfgScoringBenchmarks = ctgCfgScoringBenchmarkRepository.getCtgCfgScoringBenchmarkByScoringTypeCode(scoringTypeCode);
        if(!ctgCfgScoringBenchmarks.isEmpty()){
            throw new BusinessException(LoanErrorCode.SCORING_TYPE_IS_USE);
        }
        ctgCfgScoringTypeRepository.delete(ctgCfgScoringType.get());
    }

    @Override
    public CtgCfgScoringTypeDTO getDetail(String scoringTypeCode){
        return ctgCfgScoringTypeRepository.getDetail(scoringTypeCode);
    }

    @Override
    public void uploadFile(MultipartFile file, String objectCode) {

        if (file == null || file.isEmpty()) {
            throw new BusinessException(LoanErrorCode.FILE_EMPTY);
        }
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectCode)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        }
        catch (Exception e) {
            throw new BusinessException(LoanErrorCode.UPLOAD_FILE_FAILED, e);
        }
    }


    @Override
    public ResponseEntity<byte[]> downloadFile(String objectCode){
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectCode)
                        .build())) {

            byte[] content = stream.readAllBytes();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectCode + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(content);

        } catch (io.minio.errors.ErrorResponseException ex) {
            if ("NoSuchKey".equals(ex.errorResponse().code())) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(("File not found: " + objectCode).getBytes());
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(("MinIO error: " + ex.getMessage()).getBytes());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(("Server error: " + e.getMessage()).getBytes());
        }

    }


    @Override
    public void deleteFile(String scoringTypeCode) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(scoringTypeCode)
                            .build()
            );
        } catch (io.minio.errors.ErrorResponseException ex) {
            if ("NoSuchKey".equals(ex.errorResponse().code())) {
               log.error("File not found: " + scoringTypeCode);
            } else {
                throw new BusinessException(LoanErrorCode.MINIO_ERROR, ex);
            }

        } catch (Exception e) {
            throw new BusinessException(LoanErrorCode.MINIO_ERROR, e);
        }
    }

    @Override
    public boolean checkExist(String code) {
        return ctgCfgScoringTypeRepository
                .findCtgCfgScoringTypeByScoringTypeCode(code)
                .isPresent();
    }

}
