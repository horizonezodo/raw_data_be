package com.ngvgroup.bpm.core.logging.audit.listener;

import com.ngvgroup.bpm.core.logging.audit.service.EntityChangeHandler;
import com.ngvgroup.bpm.core.logging.audit.dto.AuditFieldChange;
import com.ngvgroup.bpm.core.logging.audit.domain.ChangeType;
import com.ngvgroup.bpm.core.logging.audit.annotation.AuditIgnore;
import com.ngvgroup.bpm.core.logging.audit.domain.EntityChange;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

import java.lang.reflect.Field;
import java.util.Objects;

@Slf4j
public record EntityChangeEventListener(EntityChangeHandler handler) implements
        PreInsertEventListener, PreUpdateEventListener, PreDeleteEventListener {

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        EntityPersister persister = event.getPersister();

        EntityChange change = new EntityChange();
        change.setChangeType(ChangeType.INSERT);
        change.setEntityName(persister.getEntityName());
        change.setTableName(resolveTableName(persister, event.getEntity()));
        change.setEntityId(event.getId());

        String[] propertyNames = persister.getPropertyNames();
        Object[] state = event.getState();

        for (int i = 0; i < propertyNames.length; i++) {
            String prop = propertyNames[i];
            //  Bỏ qua field có @AuditIgnore
            if (isIgnoredField(event.getEntity(), prop)) {
                continue;
            }

            Object newVal = state[i];
            change.getFieldChanges().add(
                    AuditFieldChange.builder()
                            .fieldName(propertyNames[i])
                            .oldValue(null)
                            .newValue(newVal != null ? String.valueOf(newVal) : null)
                            .build()
            );
        }

        handler.handle(change);
        return false; // không chặn insert
    }

    public boolean onPreUpdate(PreUpdateEvent event) {
        EntityPersister persister = event.getPersister();

        EntityChange change = new EntityChange();
        change.setChangeType(ChangeType.UPDATE);
        change.setEntityName(persister.getEntityName());
        change.setTableName(this.resolveTableName(persister, event.getEntity()));
        change.setEntityId(event.getId());

        log.debug("[AUDIT] PreUpdate entity={}, table={}, id={}",
                change.getEntityName(), change.getTableName(), change.getEntityId());

        String[] propertyNames = persister.getPropertyNames();
        Object[] oldState = event.getOldState();
        Object[] newState = event.getState();

        for (int i = 0; i < propertyNames.length; ++i) {
            String prop = propertyNames[i];
            //  Bỏ qua field có @AuditIgnore
            if (isIgnoredField(event.getEntity(), prop)) {
                continue;
            }

            Object oldVal = oldState != null ? oldState[i] : null;
            Object newVal = newState[i];
            if (!Objects.equals(oldVal, newVal)) {
                change.getFieldChanges().add(
                        AuditFieldChange.builder()
                                .fieldName(propertyNames[i])
                                .oldValue(oldVal != null ? String.valueOf(oldVal) : null)
                                .newValue(newVal != null ? String.valueOf(newVal) : null)
                                .build()
                );
            }
        }

        if (!change.getFieldChanges().isEmpty()) {
            this.handler.handle(change);
        }

        return false;
    }

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        EntityPersister persister = event.getPersister();

        EntityChange change = new EntityChange();
        change.setChangeType(ChangeType.DELETE);
        change.setEntityName(persister.getEntityName());
        change.setTableName(resolveTableName(persister, event.getEntity()));
        change.setEntityId(event.getId());

        String[] propertyNames = persister.getPropertyNames();
        Object[] deletedState = event.getDeletedState();

        for (int i = 0; i < propertyNames.length; i++) {
            String prop = propertyNames[i];
            //  Bỏ qua field có @AuditIgnore
            if (isIgnoredField(event.getEntity(), prop)) {
                continue;
            }

            Object oldVal = deletedState[i];
            change.getFieldChanges().add(
                    AuditFieldChange.builder()
                            .fieldName(propertyNames[i])
                            .oldValue(oldVal != null ? String.valueOf(oldVal) : null)
                            .newValue(null)
                            .build()
            );
        }

        handler.handle(change);
        return false;
    }

    /**
     * Hibernate 6 không expose getTableName() nữa,
     * nên mình tự resolve bằng @Table trên entity.
     */
    private String resolveTableName(EntityPersister persister, Object entity) {
        // Ưu tiên lấy từ @Table(name = ...)
        if (entity != null) {
            Class<?> clazz = entity.getClass();
            Table table = clazz.getAnnotation(Table.class);
            if (table != null && table.name() != null && !table.name().isEmpty()) {
                return table.name();
            }
            // fallback: simpleName upper-case
            return clazz.getSimpleName().toUpperCase();
        }

        // Fallback cuối cùng: dùng entityName của Hibernate
        String entityName = persister.getEntityName();
        if (entityName != null) {
            int lastDot = entityName.lastIndexOf('.');
            return (lastDot >= 0 ? entityName.substring(lastDot + 1) : entityName)
                    .toUpperCase();
        }
        return null;
    }

    /**
     * Kiểm tra field entity có gắn @AuditIgnore không.
     */
    private boolean isIgnoredField(Object entity, String propertyName) {
        if (entity == null) {
            return false;
        }
        try {
            Field f = entity.getClass().getDeclaredField(propertyName);
            return f.isAnnotationPresent(AuditIgnore.class);
        } catch (NoSuchFieldException ex) {
            // property không map trực tiếp field (VD: @Embedded, @ManyToOne...) → cứ audit
            return false;
        }
    }
}
