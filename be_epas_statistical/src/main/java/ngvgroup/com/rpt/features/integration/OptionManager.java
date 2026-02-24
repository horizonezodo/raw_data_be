package ngvgroup.com.rpt.features.integration;

import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OptionManager {

    private final OptionRepository optionRepository;

    public Option create() {
        return null;
    }

    public Option create(Option option) {
        return optionRepository.save(option);
    }

    public Option find(String settingName) {
        return optionRepository.findByParamCode(settingName)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
    }

    public Option update(String settingName, String newValue) {
        Option option = find(settingName);
        option.setParamValue(newValue);
        return optionRepository.save(option);
    }

    public void delete(String settingName) {
        Option option = find(settingName);
        optionRepository.delete(option);
    }

    public List<Option> createBatch(List<Option> options) {
        return optionRepository.saveAll(options);
    }

    public List<Option> updateBatch(List<Option> options) {
        return optionRepository.saveAll(options);
    }

    public String getSettingValueForApplicationAsync(String settingName) {
        Option option = optionRepository.findByParamCode(settingName).orElse(null);
        if (option == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        return option.getParamValue();
    }
}
