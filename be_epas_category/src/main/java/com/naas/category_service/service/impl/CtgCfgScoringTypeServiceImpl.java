package com.naas.category_service.service.impl;

import com.naas.category_service.dto.CtgCfgScoringIndcGroup.CtgCfgScoringIndcGroupDto;
import com.naas.category_service.dto.CtgCfgScoringType.CtgCfgScoringTypeDto;
import com.naas.category_service.exception.CategoryErrorCode;
import com.naas.category_service.model.CtgCfgScoringBenchmark;
import com.naas.category_service.model.CtgCfgScoringIndcGroup;
import com.naas.category_service.model.CtgCfgScoringType;
import com.naas.category_service.model.CtgInfPosition;
import com.naas.category_service.repository.CtgCfgScoringBenchmarkRepository;
import com.naas.category_service.repository.CtgCfgScoringIndcGroupRepository;
import com.naas.category_service.repository.CtgCfgScoringTypeRepository;
import com.naas.category_service.service.CtgCfgScoringTypeService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
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

public class CtgCfgScoringTypeServiceImpl implements CtgCfgScoringTypeService {
    private final CtgCfgScoringTypeRepository ctgCfgScoringTypeRepository;
    private final ExcelService excelService;
    private final MinioClient minioClient;
    private final CtgCfgScoringIndcGroupRepository ctgCfgScoringIndcGroupRepository;
    private final CtgCfgScoringBenchmarkRepository ctgCfgScoringBenchmarkRepository;

    public CtgCfgScoringTypeServiceImpl(CtgCfgScoringTypeRepository  ctgCfgScoringTypeRepository, ExcelService excelService, MinioClient minioClient, CtgCfgScoringIndcGroupRepository ctgCfgScoringIndcGroupRepository,CtgCfgScoringBenchmarkRepository ctgCfgScoringBenchmarkRepository) {
        this.ctgCfgScoringTypeRepository = ctgCfgScoringTypeRepository;
        this.excelService = excelService;
        this.minioClient = minioClient;
        this.ctgCfgScoringIndcGroupRepository = ctgCfgScoringIndcGroupRepository;
        this.ctgCfgScoringBenchmarkRepository = ctgCfgScoringBenchmarkRepository;
    }

    @Value("${minio.bucket}")
    private String bucketName;

    @Override
    public List<CtgCfgScoringTypeDto> getAll(){
        return ctgCfgScoringTypeRepository.getAll();
    }

    @Override
    public Page<CtgCfgScoringTypeDto> searchAll(String keyword, Pageable pageable){
        return ctgCfgScoringTypeRepository.searchAll(keyword, pageable);
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(String keyword, String fileName, List<String> labels){
        List<CtgCfgScoringTypeDto> ctgCfgScoringTypeDtos=ctgCfgScoringTypeRepository.exportToExcel(keyword);
        return excelService.exportToExcel(ctgCfgScoringTypeDtos,labels,CtgCfgScoringTypeDto.class,fileName);
    }

    @Override
    public void create(CtgCfgScoringTypeDto ctgCfgScoringTypeDto){
        Optional<CtgCfgScoringType> ctgCfgScoringType=ctgCfgScoringTypeRepository.findCtgCfgScoringTypeByScoringTypeCode(ctgCfgScoringTypeDto.getScoringTypeCode());
        if(ctgCfgScoringType.isPresent()){
            throw new BusinessException(CategoryErrorCode.SCORING_TYPE_ALREADY_EXISTS);
        }

        CtgCfgScoringType scoringType=new CtgCfgScoringType(
                ctgCfgScoringTypeDto.getScoringTypeCode(),
                ctgCfgScoringTypeDto.getScoringTypeName(),
                ctgCfgScoringTypeDto.getDescription(),
                ctgCfgScoringTypeDto.getSqlDataCollection(),
                ctgCfgScoringTypeDto.getTemplateCollectionCode(),
                ctgCfgScoringTypeDto.getSqlCalcResult(),
                ctgCfgScoringTypeDto.getTemplateResultCode()
        );
        ctgCfgScoringTypeRepository.save(scoringType);
    }

    @Override
    @Transactional
    public void update(CtgCfgScoringTypeDto ctgCfgScoringTypeDto){
        Optional<CtgCfgScoringType> ctgCfgScoringType=ctgCfgScoringTypeRepository.findCtgCfgScoringTypeByScoringTypeCode(ctgCfgScoringTypeDto.getScoringTypeCode());
        if(!ctgCfgScoringType.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgCfgScoringType.get().setScoringTypeName(ctgCfgScoringTypeDto.getScoringTypeName());
        ctgCfgScoringType.get().setDescription(ctgCfgScoringTypeDto.getDescription());
        ctgCfgScoringType.get().setSqlDataCollection(ctgCfgScoringTypeDto.getSqlDataCollection());
        ctgCfgScoringType.get().setTemplateCollectionCode(ctgCfgScoringTypeDto.getTemplateCollectionCode());
        ctgCfgScoringType.get().setSqlCalcResult(ctgCfgScoringTypeDto.getSqlCalcResult());
        ctgCfgScoringType.get().setTemplateResultCode(ctgCfgScoringTypeDto.getTemplateResultCode());
        ctgCfgScoringTypeRepository.save(ctgCfgScoringType.get());
    }

    @Override
    public void delete(String scoringTypeCode){
        Optional<CtgCfgScoringType> ctgCfgScoringType=ctgCfgScoringTypeRepository.findCtgCfgScoringTypeByScoringTypeCode(scoringTypeCode);
        if(!ctgCfgScoringType.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        List<CtgCfgScoringIndcGroup> ctgCfgScoringIndcGroups=ctgCfgScoringIndcGroupRepository.getCtgCfgScoringIndcGroupByScoringTypeCode(scoringTypeCode);
        if(!ctgCfgScoringIndcGroups.isEmpty()){
            throw new BusinessException(CategoryErrorCode.SCORING_TYPE_IS_USER);
        }

        List<CtgCfgScoringBenchmark> ctgCfgScoringBenchmarks = ctgCfgScoringBenchmarkRepository.getCtgCfgScoringBenchmarkByScoringTypeCode(scoringTypeCode);
        if(!ctgCfgScoringBenchmarks.isEmpty()){
            throw new BusinessException(CategoryErrorCode.SCORING_TYPE_IS_USER);
        }
        ctgCfgScoringTypeRepository.delete(ctgCfgScoringType.get());
    }

    @Override
    public CtgCfgScoringTypeDto getDetail(String scoringTypeCode){
        return ctgCfgScoringTypeRepository.getDetail(scoringTypeCode);
    }

    @Override
    public void uploadFile(MultipartFile file, String objectCode) throws Exception {
        if (file == null || file.isEmpty()) {
            return;
        }

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectCode)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }


    @Override
    public ResponseEntity<byte[]> downloadFile(String objectCode) throws Exception{
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
            System.out.println("Deleted file: " + scoringTypeCode);

        } catch (io.minio.errors.ErrorResponseException ex) {
            if ("NoSuchKey".equals(ex.errorResponse().code())) {
                System.out.println("File not found: " + scoringTypeCode);
            } else {
                throw new RuntimeException("MinIO error: " + ex.getMessage(), ex);
            }

        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }

    @Override
    public  boolean checkExist(String code){
        Optional<CtgCfgScoringType> position = ctgCfgScoringTypeRepository.findCtgCfgScoringTypeByScoringTypeCode(code);
        if(position.isPresent()) return true;
        return false;
    }



}
