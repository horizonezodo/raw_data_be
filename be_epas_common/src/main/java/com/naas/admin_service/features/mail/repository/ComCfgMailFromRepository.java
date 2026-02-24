package com.naas.admin_service.features.mail.repository;

import com.naas.admin_service.features.mail.dto.comcfgmailfrom.ComCfgMailFromResponse;
import com.naas.admin_service.features.mail.model.ComCfgMailFrom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComCfgMailFromRepository extends JpaRepository<ComCfgMailFrom, Long> {
    @Query("SELECT COUNT(c) FROM ComCfgMailFrom c " +
            "WHERE c.createdDate >= :startDate AND c.createdDate < :endDate")
    long countByCreatedDateBetween(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    @Query("select new com.naas.admin_service.features.mail.dto.comcfgmailfrom.ComCfgMailFromResponse(" +
            "ccmf.id, ccmf.mailCode, ccmf.mailFrom, ccmf.mailFromName, ccmf.mailPassword, " +
            "ccmf.host, ccmf.port) from ComCfgMailFrom ccmf " +
            "where ccmf.isDelete = 0 and (:filter is null or ccmf.mailCode like concat('%', :filter, '%') " +
            "or lower(ccmf.mailFrom) like concat('%', :filter, '%') " +
            "or lower(ccmf.mailFromName) like concat('%', :filter, '%') " +
            "or lower(ccmf.mailPassword) like concat('%', :filter, '%')" +
            "or lower(ccmf.host)  like concat('%', :filter, '%') " +
            "or to_char(ccmf.port) = :filter) " +
            "order by ccmf.modifiedDate desc")
    Page<ComCfgMailFromResponse> searchComCfgMailFrom(String filter, Pageable pageable);

    @Query("select new com.naas.admin_service.features.mail.dto.comcfgmailfrom.ComCfgMailFromResponse(" +
            "ccmf.id, ccmf.mailCode, ccmf.mailFrom, ccmf.mailFromName, ccmf.mailPassword, " +
            "ccmf.host, ccmf.port) from ComCfgMailFrom ccmf " +
            "where ccmf.isDelete = 0 and (:filter is null or ccmf.mailCode like concat('%', :filter, '%') " +
            "or lower(ccmf.mailFrom) like concat('%', :filter, '%') " +
            "or lower(ccmf.mailFromName) like concat('%', :filter, '%') " +
            "or lower(ccmf.mailPassword) like concat('%', :filter, '%')" +
            "or lower(ccmf.host)  like concat('%', :filter, '%') " +
            "or to_char(ccmf.port) = :filter)")
    List<ComCfgMailFromResponse> searchComCfgMailFrom(String filter);

    Optional<ComCfgMailFrom> findComCfgMailFromByMailCodeAndIsActive(String mailCode, Integer isActive);
}
