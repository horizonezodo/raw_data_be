package com.ngv.zns_service.intergation;

import com.ngv.zns_service.constant.ZaloErrorCode;
import com.ngvgroup.bpm.core.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OptionManager {

    private final OptionRepository optionRepository;

    public Option create(String partnerCode, String settingName) {
        return null;
    }

    public Option create(Option option) {
        return optionRepository.save(option);
    }

    public Option find(String partnerCode, String settingName) {
        return optionRepository.findByPartnerCodeAndParamCode(partnerCode, settingName)
                .orElseThrow(() -> new BusinessException(ZaloErrorCode.NOT_FOUND));
    }

    public Option update(String partnerCode, String settingName, String newValue) {
        Option option = find(partnerCode, settingName);
        option.setParamValue(newValue);
        return optionRepository.save(option);
    }

    public void delete(String partnerCode, String settingName) {
        Option option = find(partnerCode, settingName);
        optionRepository.delete(option);
    }

    public List<Option> createBatch(List<Option> options) {
        return optionRepository.saveAll(options);
    }

    public List<Option> updateBatch(List<Option> options) {
        return optionRepository.saveAll(options);
    }


    public String getSettingValueForApplicationAsync(String partnerCode, String settingName) {
        Option option = optionRepository.findByPartnerCodeAndParamCode(partnerCode, settingName).orElse(null);
        if (option == null) {
            throw new BusinessException(ZaloErrorCode.NOT_FOUND);
        }
        return option.getParamValue();
    }
}