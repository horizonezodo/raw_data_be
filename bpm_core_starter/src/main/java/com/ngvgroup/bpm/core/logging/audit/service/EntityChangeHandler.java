package com.ngvgroup.bpm.core.logging.audit.service;

import com.ngvgroup.bpm.core.logging.audit.domain.EntityChange;

public interface EntityChangeHandler {
    void handle(EntityChange change);
}