package ngvgroup.com.bpmn.service.impl;

import ngvgroup.com.bpmn.service.ComCfgParameterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import com.ngvgroup.bpm.core.dto.ResponseData;

import ngvgroup.com.bpmn.dto.ComCfgParameter.ComCfgParameterDto;
import ngvgroup.com.bpmn.openfeign.ParameterFeignClient;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComCfgParameterServiceImpl implements ComCfgParameterService {

    private final ParameterFeignClient parameterFeignClient;

    @NotNull
    public ComCfgParameterDto getParameterByCode(String paramCode) {
        try {
            ResponseData<ComCfgParameterDto> response = parameterFeignClient.getParameterByCode(paramCode);
            if (response != null && response.getData() != null) {
                return response.getData();
            } else {
                log.error("Không tìm thấy parameter với mã: {}", paramCode);
                throw new BusinessException(ErrorCode.NOT_FOUND,
                        "Không lấy được thông tin parameter với mã: " + paramCode);
            }
        } catch (Exception ex) {
            log.error("Lỗi khi gọi ParameterFeignClient: {}", ex.getMessage(), ex);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Lỗi lấy thông tin parameter: " + ex.getMessage());
        }
    }
}
