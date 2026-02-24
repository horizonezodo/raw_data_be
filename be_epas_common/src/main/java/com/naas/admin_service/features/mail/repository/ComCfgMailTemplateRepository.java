package com.naas.admin_service.features.mail.repository;

import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateResponse;
import com.naas.admin_service.features.mail.model.ComCfgMailTemplate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComCfgMailTemplateRepository extends JpaRepository<ComCfgMailTemplate, Long> {

    Boolean existsByMailTemplateCodeAndIsDelete (String mailTemplateCode, int isDelete);
    @Query("select ccmt from ComCfgMailTemplate ccmt " +
            "where ccmt.mailTemplateCode = :mailTemplateCode and ccmt.isDelete = 0")
    Optional<ComCfgMailTemplate> findByMailTemplateCode(String mailTemplateCode);

    @Query("SELECT new com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateResponse(" +
            "c.id, c.mailTemplateCode, c.mailTemplateName, c.mailSubject, c.mailBody, c.toEmails, c.ccEmails, c.bccEmails, c.mailCode) " +
            "FROM ComCfgMailTemplate c " +
            "WHERE c.isActive = 1 AND c.isDelete = 0 AND (" +
            "LOWER(c.mailTemplateCode) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(c.mailTemplateName) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(c.mailSubject) LIKE LOWER(CONCAT('%', :filter, '%')))") 
    Page<ComCfgMailTemplateResponse> getAll(@Param("filter") String filter, Pageable pageable);

    @Query("SELECT new com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateResponse(" +
            "c.id, c.mailTemplateCode, c.mailTemplateName, c.mailSubject, c.mailBody, c.toEmails, c.ccEmails, c.bccEmails, c.mailCode) " +
            "FROM ComCfgMailTemplate c " +
            "WHERE c.isActive = 1 AND c.isDelete = 0 AND c.mailTemplateCode = :mailTemplateCode")
    Optional<ComCfgMailTemplateResponse> getDetail(@Param("mailTemplateCode") String mailTemplateCode);

    List<ComCfgMailTemplate> findComCfgMailTemplatesByMailCode(String mailCode);

    @Query("select ccmt from ComCfgMailTemplate ccmt where ccmt.id in (:ids)")
    List<ComCfgMailTemplate> findComCfgMailTemplatesByIdIs(List<Long> ids);

    @Query("SELECT new com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateResponse(" +
            "c.id, c.mailTemplateCode, c.mailTemplateName, c.mailSubject, c.mailBody, c.toEmails, c.ccEmails, c.bccEmails, c.mailCode) " +
            "FROM ComCfgMailTemplate c " +
            "WHERE c.isActive = 1 AND c.isDelete = 0 AND (" +
            ":filter IS NULL OR :filter = '' OR " +
            "LOWER(c.mailTemplateCode) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(c.mailTemplateName) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(c.mailSubject) LIKE LOWER(CONCAT('%', :filter, '%')))") 
    List<ComCfgMailTemplateResponse> getAllList(@Param("filter") String filter);
}
