package com.naas.admin_service.features.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.CtgCfgResourceMappingDto1;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ListResourceMappingDto;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ResourceMappingDto;
import com.naas.admin_service.features.users.model.CtgCfgResourceMapping;

import java.util.List;

@Repository
public interface CtgCfgResourceMappingRepository extends JpaRepository<CtgCfgResourceMapping, Long> {

    boolean existsComCfgResourceMappingByResourceTypeCode(String resourceTypeCode);

    /** Danh sách mapping có hiệu lực: EFFECTIVE_DATE <= today < EXPIRY_DATE (dùng cho phân quyền) */
    @Query("Select new com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ListResourceMappingDto(" +
            "ccrm.id, ccrm.resourceCode, ccrm.resourceName, ccrm.effectiveDate, ccrm.expiryDate) " +
            "FROM CtgCfgResourceMapping ccrm" +
            " where ccrm.resourceTypeCode = 'CM032.001' and ccrm.userId = :userId and ccrm.isDelete = 0 and ccrm.isActive = 1" +
            " and (ccrm.effectiveDate is null or ccrm.effectiveDate <= CURRENT_DATE) and (ccrm.expiryDate is null or ccrm.expiryDate > CURRENT_DATE)")
    List<ListResourceMappingDto> findAllListResourceMappingDto(@Param("userId") String userId);

    @Query("select ccrm from CtgCfgResourceMapping ccrm where " +
            "ccrm.groupId = :groupId and ccrm.resourceTypeCode = :resourceTypeCode " +
            "and (:isActive is null or ccrm.isActive = :isActive) ")
    List<CtgCfgResourceMapping> findByGroupIdAndResourceTypeCodeAndIsActive(String groupId, String resourceTypeCode, Integer isActive);



    @Query("select ccrm from CtgCfgResourceMapping ccrm where " +
            "ccrm.userId = :userId and ccrm.resourceTypeCode = :resourceTypeCode " +
            "and (:isActive is null or ccrm.isActive = :isActive) " +
            "and (:fieldSearch is null or lower(ccrm.resourceCode) like concat('%', :fieldSearch, '%')  " +
            "or lower(ccrm.resourceName) like concat('%', :fieldSearch, '%')" +
            "or lower(ccrm.description) like concat('%', :fieldSearch, '%'))")
    Page<CtgCfgResourceMapping> findByUserIdAndResourceTypeCodeAndIsActive(String userId, String resourceTypeCode, Integer isActive, Pageable pageable, String fieldSearch);

    /** Chỉ lấy mapping đang hiệu lực: EFFECTIVE_DATE <= today < EXPIRY_DATE (dùng cho phân quyền) */
    @Query("select ccrm from CtgCfgResourceMapping ccrm where " +
            "ccrm.userId = :userId and ccrm.resourceTypeCode = :resourceTypeCode " +
            "and (:isActive is null or ccrm.isActive = :isActive) " +
            "and (ccrm.effectiveDate is null or ccrm.effectiveDate <= CURRENT_DATE) and (ccrm.expiryDate is null or ccrm.expiryDate > CURRENT_DATE) " +
            "and (:fieldSearch is null or lower(ccrm.resourceCode) like concat('%', :fieldSearch, '%')  " +
            "or lower(ccrm.resourceName) like concat('%', :fieldSearch, '%')" +
            "or lower(ccrm.description) like concat('%', :fieldSearch, '%'))")
    Page<CtgCfgResourceMapping> findByUserIdAndResourceTypeCodeAndIsActiveEffective(String userId, String resourceTypeCode, Integer isActive, Pageable pageable, String fieldSearch);

    @Query("select ccrm from CtgCfgResourceMapping ccrm where " +
            "ccrm.userId = :userId and ccrm.resourceTypeCode = :resourceTypeCode " +
            "and (:isActive is null or ccrm.isActive = :isActive) ")
    List<CtgCfgResourceMapping> findByUserIdAndResourceTypeCodeAndIsActive(String userId, String resourceTypeCode, Integer isActive);

    /** Chỉ lấy mapping đang hiệu lực (dùng cho phân quyền) */
    @Query("select ccrm from CtgCfgResourceMapping ccrm where " +
            "ccrm.userId = :userId and ccrm.resourceTypeCode = :resourceTypeCode " +
            "and (:isActive is null or ccrm.isActive = :isActive) " +
            "and (ccrm.effectiveDate is null or ccrm.effectiveDate <= CURRENT_DATE) and (ccrm.expiryDate is null or ccrm.expiryDate > CURRENT_DATE) ")
    List<CtgCfgResourceMapping> findByUserIdAndResourceTypeCodeAndIsActiveEffective(String userId, String resourceTypeCode, Integer isActive);

    @Query("select ccrm from CtgCfgResourceMapping ccrm where " +
            "ccrm.userId = :userId and ccrm.resourceTypeCode in (:resourceTypeCode) " +
            "and (:isActive is null or ccrm.isActive = :isActive) ")
    List<CtgCfgResourceMapping> findByUserIdAndResourceTypeCodeAndIsActive(String userId, List<String> resourceTypeCode, Integer isActive);

    @Query("select ccrm from CtgCfgResourceMapping ccrm where " +
            "ccrm.userId = :userId and ccrm.resourceTypeCode = :resourceTypeCode " +
            "and (:isActive is null or ccrm.isActive = :isActive) " +
            "and (:fieldSearch is null or lower(ccrm.resourceCode) like concat('%', :fieldSearch, '%')  " +
            "or lower(ccrm.resourceName) like concat('%', :fieldSearch, '%')" +
            "or lower(ccrm.description) like concat('%', :fieldSearch, '%'))")
    List<CtgCfgResourceMapping> findAllByUserIdAndResourceTypeCodeAndIsActive(String userId, String resourceTypeCode, Integer isActive, String fieldSearch);

    /** Chỉ lấy mapping đang hiệu lực (dùng cho phân quyền) */
    @Query("select ccrm from CtgCfgResourceMapping ccrm where " +
            "ccrm.userId = :userId and ccrm.resourceTypeCode = :resourceTypeCode " +
            "and (:isActive is null or ccrm.isActive = :isActive) " +
            "and (ccrm.effectiveDate is null or ccrm.effectiveDate <= CURRENT_DATE) and (ccrm.expiryDate is null or ccrm.expiryDate > CURRENT_DATE) " +
            "and (:fieldSearch is null or lower(ccrm.resourceCode) like concat('%', :fieldSearch, '%')  " +
            "or lower(ccrm.resourceName) like concat('%', :fieldSearch, '%')" +
            "or lower(ccrm.description) like concat('%', :fieldSearch, '%'))")
    List<CtgCfgResourceMapping> findAllByUserIdAndResourceTypeCodeAndIsActiveEffective(String userId, String resourceTypeCode, Integer isActive, String fieldSearch);

    @Query("select ccrm from CtgCfgResourceMapping ccrm where " +
            "ccrm.groupId = :groupId and ccrm.resourceTypeCode = :resourceTypeCode " +
            "and (:isActive is null or ccrm.isActive = :isActive) " +
            "and (:fieldSearch is null or lower(ccrm.resourceCode) like concat('%', :fieldSearch, '%')  " +
            "or lower(ccrm.resourceName) like concat('%', :fieldSearch, '%')" +
            "or lower(ccrm.description) like concat('%', :fieldSearch, '%'))")
    List<CtgCfgResourceMapping> findAllByGroupIdAndResourceTypeCodeAndIsActive(String groupId, String resourceTypeCode, Integer isActive, String fieldSearch);

    /** Chỉ lấy mapping đang hiệu lực (dùng cho phân quyền) */
    @Query("select ccrm from CtgCfgResourceMapping ccrm where " +
            "ccrm.groupId = :groupId and ccrm.resourceTypeCode = :resourceTypeCode " +
            "and (:isActive is null or ccrm.isActive = :isActive) " +
            "and (ccrm.effectiveDate is null or ccrm.effectiveDate <= CURRENT_DATE) and (ccrm.expiryDate is null or ccrm.expiryDate > CURRENT_DATE) " +
            "and (:fieldSearch is null or lower(ccrm.resourceCode) like concat('%', :fieldSearch, '%')  " +
            "or lower(ccrm.resourceName) like concat('%', :fieldSearch, '%')" +
            "or lower(ccrm.description) like concat('%', :fieldSearch, '%'))")
    List<CtgCfgResourceMapping> findAllByGroupIdAndResourceTypeCodeAndIsActiveEffective(String groupId, String resourceTypeCode, Integer isActive, String fieldSearch);

    /** Chỉ lấy mapping đang hiệu lực (dùng cho phân quyền) */
    @Query("select ccrm from CtgCfgResourceMapping ccrm where " +
            "ccrm.groupId = :groupId and ccrm.resourceTypeCode = :resourceTypeCode " +
            "and (:isActive is null or ccrm.isActive = :isActive) " +
            "and (ccrm.effectiveDate is null or ccrm.effectiveDate <= CURRENT_DATE) and (ccrm.expiryDate is null or ccrm.expiryDate > CURRENT_DATE) ")
    List<CtgCfgResourceMapping> findByGroupIdAndResourceTypeCodeAndIsActiveEffective(String groupId, String resourceTypeCode, Integer isActive);

    /** Chỉ lấy mapping đang hiệu lực: EFFECTIVE_DATE <= today < EXPIRY_DATE */
    @Query("select new com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ResourceMappingDto(" +
            "rm.resourceCode, rm.resourceName, rm.effectiveDate, rm.expiryDate) " +
            "FROM CtgCfgResourceMapping rm " +
            "WHERE rm.resourceTypeCode = :resourceTypeCode AND rm.userId = :userId " +
            " and (rm.effectiveDate is null or rm.effectiveDate <= CURRENT_DATE) and (rm.expiryDate is null or rm.expiryDate > CURRENT_DATE)")
    List<ResourceMappingDto> getListOfResourceMappingDto(@Param("resourceTypeCode") String resourceTypeCode,
                                                         @Param("userId") String userId);

    /** Chỉ lấy mapping đang hiệu lực */
    @Query("SELECT new com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.CtgCfgResourceMappingDto1(" +
            "c.resourceCode, c.resourceName, c.effectiveDate, c.expiryDate) " +
            "FROM CtgCfgResourceMapping c " +
            "WHERE c.resourceTypeCode='CM032.001' AND c.userId=:userId " +
            " and (c.effectiveDate is null or c.effectiveDate <= CURRENT_DATE) and (c.expiryDate is null or c.expiryDate > CURRENT_DATE)")
    List<CtgCfgResourceMappingDto1> listOrg(@Param("userId") String userId);

    /** Chỉ lấy mapping đang hiệu lực */
    @Query("SELECT new com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.CtgCfgResourceMappingDto1(" +
            "c.resourceCode, c.resourceName, NULL, ct.areaCode, c.effectiveDate, c.expiryDate) " +
            "FROM CtgCfgResourceMapping c " +
            "JOIN ComInfArea ct ON c.resourceCode = ct.areaCode " +
            "WHERE c.resourceTypeCode = 'CM032.004' " +
            "AND c.userId = :userId " +
            " and (c.effectiveDate is null or c.effectiveDate <= CURRENT_DATE) and (c.expiryDate is null or c.expiryDate > CURRENT_DATE) " +
            "AND (:resourceCode IS NULL OR ct.orgCode = :resourceCode)")
    List<CtgCfgResourceMappingDto1> listArea(@Param("userId") String userId, @Param("resourceCode") String resourceCode);


    /** Chỉ lấy mapping đang hiệu lực */
    @Query("select new com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.CtgCfgResourceMappingDto1(" +
            "rm.resourceCode, rm.resourceName, cia.orgCode, NULL, rm.effectiveDate, rm.expiryDate) " +
            "from CtgCfgResourceMapping rm join ComInfArea cia on cia.areaCode = rm.resourceCode " +
            "where rm.resourceTypeCode = :resourceTypeCode and rm.userId = :userId " +
            " and (rm.effectiveDate is null or rm.effectiveDate <= CURRENT_DATE) and (rm.expiryDate is null or rm.expiryDate > CURRENT_DATE) and " +
            "cia.orgCode in (:orgCode)")
    List<CtgCfgResourceMappingDto1> findAreaByResourceCode(String resourceTypeCode, List<String> orgCode, String userId);


}
