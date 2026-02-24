package com.naas.admin_service.features.setting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naas.admin_service.features.setting.model.ComCfgSetting;

import java.util.Optional;
import java.util.List;

@Repository
public interface ComCfgSettingRepository extends JpaRepository<ComCfgSetting, Long> {
    Optional<ComCfgSetting> findBySettingCode(String settingCode);

    List<ComCfgSetting> findBySettingCodeIn(List<String> settingCodes);
}
