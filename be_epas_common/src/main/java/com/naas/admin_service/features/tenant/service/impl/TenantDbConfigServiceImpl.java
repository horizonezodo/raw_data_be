package com.naas.admin_service.features.tenant.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.tenant.dto.TenantRequestDto;
import com.naas.admin_service.features.tenant.dto.TenantResponseDto;
import com.naas.admin_service.features.tenant.mapper.TenantDbConfigMapper;
import com.naas.admin_service.features.tenant.model.TenantDbConfig;
import com.naas.admin_service.features.tenant.repository.TenantDbConfigRepository;
import com.naas.admin_service.features.tenant.service.TenantDbConfigService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.persistence.dto.TenantCreatedEvent;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.UncheckedIOException;

@Slf4j
@Service
public class TenantDbConfigServiceImpl
        extends BaseServiceImpl<TenantDbConfig, TenantRequestDto>
        implements TenantDbConfigService {

    private final TenantDbConfigRepository tenantDbConfigRepository;
    private final MultitenancyProperties mtp;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final String topicName;

    public TenantDbConfigServiceImpl(
            TenantDbConfigRepository repository,
            TenantDbConfigMapper mapper,
            MultitenancyProperties mtp,
            KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper,
            @Value("${multitenancy.kafka.topic:}") String topicName
    ) {
        super(repository, mapper);
        this.tenantDbConfigRepository = repository;
        this.mtp = mtp;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topicName = topicName;
    }

    @Override
    public Page<TenantResponseDto> search(String keyword, Pageable pageable) {
        return tenantDbConfigRepository.search(keyword, pageable);
    }

    @Override
    public boolean isMultitenancyEnabled() {
        return mtp.isEnabled();
    }

    @Override
    public boolean existsByTenantIdAndActive(String tenantId) {
        boolean exists = tenantDbConfigRepository.existsByTenantIdAndActive(tenantId, "Y");
        if (!exists) {
            throw new BusinessException(CommonErrorCode.TENANT_INACTIVE, tenantId);
        }
            return true;
    }

    @Override
    protected void afterSaveCreate(TenantDbConfig entity) {
        if (!mtp.isEnabled()) return;

        if (entity == null || entity.getTenantId() == null || entity.getTenantId().isBlank()) {
            log.warn("[MT][TENANT_CREATED] skip: empty tenantId");
            return;
        }

        if (topicName == null || topicName.isBlank()) {
            throw new IllegalStateException("Missing required config: multitenancy.kafka.topic");
        }

        String tenantId = entity.getTenantId().trim();
        TenantCreatedEvent event = new TenantCreatedEvent(tenantId);

        // ✅ send sau commit, nhưng chỉ khi synchronization active
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    String payload = null;
                    try {
                        payload = objectMapper.writeValueAsString(event);
                    } catch (JsonProcessingException e) {
                        throw new UncheckedIOException("Failed to serialize tenantCreated event to JSON", e);
                    }
                    kafkaTemplate.send(topicName, tenantId, payload);
                    log.info("[MT][TENANT_CREATED] published tenantId={} topic={}", tenantId, topicName);
                }
            });
        } else {
            // fallback: vẫn publish, nhưng log rõ (tránh mất message)
            kafkaTemplate.send(topicName, tenantId, String.valueOf(event));
            log.warn("[MT][TENANT_CREATED] published WITHOUT transaction synchronization tenantId={} topic={}", tenantId, topicName);
        }
    }
}