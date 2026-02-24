package com.ngv.aia_service.intergation;

import com.ngv.aia_service.constant.ErrorCode;
import com.ngv.aia_service.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND.code(), ErrorCode.NOT_FOUND.message()));
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
            throw new BusinessException(ErrorCode.NOT_FOUND.code(), ErrorCode.NOT_FOUND.message());
        }
        return option.getParamValue();
    }
}