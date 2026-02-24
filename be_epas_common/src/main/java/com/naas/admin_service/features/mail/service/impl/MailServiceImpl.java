package com.naas.admin_service.features.mail.service.impl;

import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.core.kafka.dto.EmailVariablesDto;
import com.naas.admin_service.core.kafka.dto.MailProcessRequestDto;
import com.naas.admin_service.core.utils.AesEncryptionUtil;
import com.naas.admin_service.core.utils.PageUtils;
import com.naas.admin_service.features.mail.model.ComCfgMailFrom;
import com.naas.admin_service.features.mail.repository.ComCfgMailFromRepository;
import com.naas.admin_service.features.mail.mapper.ComCfgMailTemplateMapper;
import com.naas.admin_service.features.mail.model.ComCfgMailTemplate;
import com.naas.admin_service.features.mail.repository.ComCfgMailTemplateRepository;
import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateRequest;
import com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailDto;
import com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailResponse;
import com.naas.admin_service.features.mail.dto.comlogmail.ComLogMailSearchRequest;
import com.naas.admin_service.features.mail.mapper.ComLogMailMapper;
import com.naas.admin_service.features.mail.model.ComLogMail;
import com.naas.admin_service.features.mail.repository.ComLogMailRepository;
import com.naas.admin_service.features.mail.service.MailService;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.users.dto.InfUserDto;
import com.naas.admin_service.features.users.service.UserService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private final ComCfgMailTemplateMapper comCfgMailTemplateMapper;
    private final ComCfgMailFromRepository comCfgMailFromRepository;
    private final ComCfgMailTemplateRepository comCfgMailTemplateRepository;
    private final ComLogMailRepository comLogMailRepository;
    private final ComLogMailMapper comLogMailMapper;
    private final AesEncryptionUtil aesEncryptionUtil;
    private final ExcelService excelService;
    private final UserService userService;

    public MailServiceImpl(ComCfgMailTemplateMapper comCfgMailTemplateMapper, ComCfgMailFromRepository comCfgMailFromRepository, ComCfgMailTemplateRepository comCfgMailTemplateRepository, ComLogMailRepository comLogMailRepository, ComLogMailMapper comLogMailMapper, AesEncryptionUtil aesEncryptionUtil, ExcelService excelService, UserService userService) {
        this.comCfgMailTemplateMapper = comCfgMailTemplateMapper;
        this.comCfgMailFromRepository = comCfgMailFromRepository;
        this.comCfgMailTemplateRepository = comCfgMailTemplateRepository;
        this.comLogMailRepository = comLogMailRepository;
        this.comLogMailMapper = comLogMailMapper;
        this.aesEncryptionUtil = aesEncryptionUtil;
        this.excelService = excelService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void sendMail(MailProcessRequestDto dto) {

        ComCfgMailTemplate mailTemplate = getMailTemplate(dto.getEmailTemplateCode());
        ComCfgMailFrom comCfgMailFrom = getMailFrom(mailTemplate.getMailCode());

        String mailBody = replaceAtVariablesStrict(mailTemplate.getMailBody(), dto.getEmailVariables());

        // Lấy user theo ID (List<String>)
        List<InfUserDto> emailTos = dto.getUserNameTo() == null ? List.of()
                : userService.getUsernameList(dto.getUserNameTo());

        List<InfUserDto> emailCCs = dto.getUserNameCc() == null ? List.of()
                : userService.getUsernameList(dto.getUserNameCc());

        // Email từ user
        String[] toFromUsers = extractEmails(emailTos);
        String[] ccFromUsers = extractEmails(emailCCs);

        // Email cấu hình template
        String[] toFromTemplate = splitEmails(mailTemplate.getToEmails());
        String[] ccFromTemplate = splitEmails(mailTemplate.getCcEmails());

        // Merge user + template
        String[] sendTo = mergeEmails(toFromUsers, toFromTemplate);
        String[] sendCc = mergeEmails(ccFromUsers, ccFromTemplate);

        if (sendTo.length == 0) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "Danh sách người nhận (TO) trống");
        }

        try {
            JavaMailSenderImpl mailSenderImpl = setupMailSender(comCfgMailFrom);
            MimeMessage mimeMessage = createMimeMessage(
                    mailSenderImpl, comCfgMailFrom, mailTemplate, mailBody, sendTo, sendCc
            );
            mailSenderImpl.send(mimeMessage);

            saveLogMail(mailTemplate, Boolean.TRUE, dto, mailBody, sendTo, null);
            log.info("Send mail successfully {}", dto.getBusinessKey());
        } catch (Exception e) {
            log.error("Send mail failed {}", e.getMessage(), e);
            saveLogMail(mailTemplate, Boolean.FALSE, dto, mailBody, sendTo, e.getMessage());
        }
    }

    private String[] extractEmails(List<InfUserDto> users) {
        if (users == null) return new String[0];
        return users.stream()
                .map(InfUserDto::getEmail)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .distinct()
                .toArray(String[]::new);
    }

    private String[] splitEmails(String csv) {
        if (csv == null || csv.isBlank()) return new String[0];
        return Arrays.stream(csv.split(Constant.COMMA))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .distinct()
                .toArray(String[]::new);
    }

    private String[] mergeEmails(String[] a, String[] b) {
        return Stream.concat(
                        Arrays.stream(a == null ? new String[0] : a),
                        Arrays.stream(b == null ? new String[0] : b)
                )
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .distinct()
                .toArray(String[]::new);
    }

    // Lấy MailTemplate theo mã template
    private ComCfgMailTemplate getMailTemplate(String templateCode) {
        return comCfgMailTemplateRepository.findByMailTemplateCode(templateCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,
                        "Không tìm thấy template email với mã: " + templateCode));
    }

    private ComCfgMailFrom getMailFrom(String mailCode) {
        if (mailCode == null || mailCode.isEmpty()) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "không tồn tại mailFrom");
        }

        return comCfgMailFromRepository.findComCfgMailFromByMailCodeAndIsActive(mailCode, 1)
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy template from với mã: " + mailCode));
    }

    // Cấu hình JavaMailSenderImpl
    private JavaMailSenderImpl setupMailSender(ComCfgMailFrom comCfgMailFrom) {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(comCfgMailFrom.getHost());
        mailSenderImpl.setPort(comCfgMailFrom.getPort());
        mailSenderImpl.setProtocol(comCfgMailFrom.getMailProtocol());
        mailSenderImpl.setUsername(comCfgMailFrom.getMailFrom());
        mailSenderImpl.setPassword(comCfgMailFrom.getMailPassword());
        if (comCfgMailFrom.getIsEncrypted() != null && comCfgMailFrom.getIsEncrypted()) {
            String password = aesEncryptionUtil.decrypt(comCfgMailFrom.getMailPassword());

            mailSenderImpl.setPassword(password);
        }

        Properties props = mailSenderImpl.getJavaMailProperties();
        props.putAll(getAllProps(comCfgMailFrom));

        return mailSenderImpl;
    }

    private Map<String, String> getAllProps(ComCfgMailFrom comCfgMailFrom) {
        Map<String, String> props = new HashMap<>();
        if (Boolean.TRUE.equals(comCfgMailFrom.getIsUseSsl())) {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.mime.charset", "UTF-8");
        }
        return props;
    }

    //  Tạo nội dung email
    private MimeMessage createMimeMessage(JavaMailSenderImpl mailSenderImpl,
                                          ComCfgMailFrom comCfgMailFrom,
                                          ComCfgMailTemplate mailTemplate,
                                          String mailBody,
                                          String[] sendTo,
                                          String[] sendCc) throws MessagingException {

        MimeMessage mimeMessage = mailSenderImpl.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(comCfgMailFrom.getMailFrom());
        helper.setTo(sendTo);
        helper.setSubject(mailTemplate.getMailSubject());
        helper.setText(mailBody, true);

        String[] bcc = splitEmails(mailTemplate.getBccEmails());
        if (bcc.length > 0) helper.setBcc(bcc);

        if (sendCc != null && sendCc.length > 0) helper.setCc(sendCc);

        return mimeMessage;
    }

    private String replaceAtVariablesStrict(String body, EmailVariablesDto vars) {
        if (body == null || vars == null) return body;

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("processInstantCode", vars.getProcessInstantCode());
        map.put("user", vars.getUser());
        map.put("comment", vars.getComment());
        map.put("mailCode", vars.getMailCode());

        String result = body;
        for (Map.Entry<String, Object> e : map.entrySet()) {
            String key = e.getKey();
            Object val = e.getValue();
            if (key == null || key.isBlank() || val == null) continue;

            // match đúng @key và đảm bảo không dính chữ/số/_ ngay sau
            Pattern p = Pattern.compile("@" + Pattern.quote(key) + "(?![A-Za-z0-9_])");
            Matcher m = p.matcher(result);
            result = m.replaceAll(Matcher.quoteReplacement(String.valueOf(val)));
        }
        return result;
    }

    @Override
    public void saveLogMail(ComCfgMailTemplate comCfgMailTemplate, boolean isSuccess,
                            MailProcessRequestDto dto, String mailBody, String[] sendTo, String error) {

        ComCfgMailTemplateRequest request = comCfgMailTemplateMapper.toDto(comCfgMailTemplate);
        ComLogMail comLogMail = comLogMailMapper.toEntity(request);

        comLogMail.setMailBody(mailBody);
        comLogMail.setMailTempCode(comCfgMailTemplate.getMailTemplateCode());
        comLogMail.setToEmails(String.join(Constant.COMMA, sendTo));
        comLogMail.setProcessInstanceCode(dto.getBusinessKey());
        comLogMail.setDescription(error);
        comLogMail.setSendTime(Timestamp.from(ZonedDateTime.now(ZoneId.systemDefault()).toInstant()));

        if (isSuccess) {
            comLogMail.setMailStatus(Constant.SUCCESS);
        } else {
            comLogMail.setMailStatus(Constant.FAILED);
        }
        comLogMailRepository.save(comLogMail);
    }

    @Override
    public ComLogMailDto getLogMailDetail(Long id) {
        ComLogMail comLogMail = comLogMailRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "logMailId: " + id));
        return comLogMailMapper.toDto(comLogMail);
    }

    @Override
    public Page<ComLogMailResponse> searchLogMail(ComLogMailSearchRequest request) {
        Pageable pageableRequest = PageUtils.toPageable(request.getPageable());

        if (request.getFilter() == null || request.getFilter().trim().isEmpty()) {
            String toDate = ("".equals(request.getToDate()) || request.getToDate() == null)
                    ? request.getToDate()
                    : LocalDate.parse(request.getToDate()).plusDays(1).toString();
            return comLogMailRepository.searchAdvanceComLogMail(request.getProcessInstanceCode(), request.getMailTemplateCode(),
                    request.getMailSubject(), request.getMailStatus(), request.getFromDate(), toDate, pageableRequest);
        } else {
            String filter = request.getFilter().trim().toLowerCase();
            return comLogMailRepository.searchComLogMail(filter, pageableRequest);
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadLogMail(String fileName, ComLogMailSearchRequest request) {
        List<ComLogMailResponse> response;
        if (request.getFilter() == null || request.getFilter().trim().isEmpty()) {

            String toDate = ("".equals(request.getToDate()) || request.getToDate() == null)
                    ? request.getToDate()
                    : LocalDate.parse(request.getToDate()).plusDays(1).toString();
            response = comLogMailRepository.searchAdvanceComLogMail(request.getProcessInstanceCode(), request.getMailTemplateCode(),
                    request.getMailSubject(), request.getMailStatus(), request.getFromDate(), toDate);
        } else {
            String filter = request.getFilter().trim().toLowerCase();
            response = comLogMailRepository.searchComLogMail(filter);
        }
        byte[] res = excelService.exportToExcelContent(response, request.getLabels(), ComLogMailResponse.class);
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xlsx\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(res);
        } catch (Exception e) {
            log.error("Error: Lỗi tạo file Excel {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
