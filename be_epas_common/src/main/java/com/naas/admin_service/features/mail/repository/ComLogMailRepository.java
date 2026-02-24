package com.naas.admin_service.features.mail.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailResponse;
import com.naas.admin_service.features.mail.model.ComLogMail;

import java.util.List;

@Repository
public interface ComLogMailRepository extends JpaRepository<ComLogMail, Long> {
    @Query("select new com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailResponse(" +
            "clm.id, clm.processInstanceCode, clm.mailTempCode, clm.mailSubject, clm.sendTime, clm.mailStatus) " +
            "from ComLogMail clm " +
            "where :filter is null or lower(clm.processInstanceCode) like concat('%', :filter, '%') " +
            "or lower(clm.mailTempCode) like concat('%', :filter, '%') " +
            "or lower(clm.mailSubject) like concat('%', :filter, '%') " +
            "or lower(clm.mailStatus) like concat('%', :filter, '%') " +
            "or to_char(clm.sendTime) like concat('%', :filter, '%')")
    Page<ComLogMailResponse> searchComLogMail(String filter, Pageable pageable);

    @Query("select new com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailResponse(" +
            "clm.id, clm.processInstanceCode, clm.mailTempCode, clm.mailSubject, clm.sendTime, clm.mailStatus) " +
            "from ComLogMail clm " +
            "where :filter is null or lower(clm.processInstanceCode) like concat('%', :filter, '%') " +
            "or lower(clm.mailTempCode) like concat('%', :filter, '%') " +
            "or lower(clm.mailSubject) like concat('%', :filter, '%') " +
            "or lower(clm.mailStatus) like concat('%', :filter, '%') " +
            "or to_char(clm.sendTime) like concat('%', :filter, '%') " +
            "order by clm.id desc")
    List<ComLogMailResponse> searchComLogMail(String filter);

    @Query("select new com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailResponse(" +
            "clm.id, clm.processInstanceCode, clm.mailTempCode, clm.mailSubject, clm.sendTime, clm.mailStatus) " +
            "from ComLogMail clm " +
            "where (:processInstanceCode is null or clm.processInstanceCode = :processInstanceCode) " +
            "and (:mailTemplateCode is null or clm.mailTempCode = :mailTemplateCode) " +
            "and (:mailStatus is null or clm.mailStatus = :mailStatus) " +
            "and (:mailSubject is null or clm.mailSubject = :mailSubject) " +
            "and (:fromDate is null or clm.sendTime >= to_timestamp(:fromDate, 'YYYY-MM-DD HH24:MI:SS')) " +
            "and (:toDate is null or clm.sendTime <= to_timestamp(:toDate, 'YYYY-MM-DD HH24:MI:SS')) ")
    Page<ComLogMailResponse> searchAdvanceComLogMail(String processInstanceCode, String mailTemplateCode,
                                                     String mailSubject, String mailStatus, String fromDate,
                                                     String toDate, Pageable pageable);

    @Query("select new com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailResponse(" +
            "clm.id, clm.processInstanceCode, clm.mailTempCode, clm.mailSubject, clm.sendTime, clm.mailStatus) " +
            "from ComLogMail clm " +
            "where (:processInstanceCode is null or clm.processInstanceCode = :processInstanceCode) " +
            "and (:mailTemplateCode is null or clm.mailTempCode = :mailTemplateCode) " +
            "and (:mailStatus is null or clm.mailStatus = :mailStatus) " +
            "and (:mailSubject is null or clm.mailSubject = :mailSubject) " +
            "and (:fromDate is null or clm.sendTime >= to_timestamp(:fromDate, 'YYYY-MM-DD HH24:MI:SS')) " +
            "and (:toDate is null or clm.sendTime <= to_timestamp(:toDate, 'YYYY-MM-DD HH24:MI:SS')) " +
            "order by clm.id desc")
    List<ComLogMailResponse> searchAdvanceComLogMail(String processInstanceCode, String mailTemplateCode,
                                                     String mailSubject, String mailStatus, String fromDate,
                                                     String toDate);
}
