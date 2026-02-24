package ngvgroup.com.rpt.features.report.service.impl;


import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.rpt.features.report.dto.ComCfgParameterDto;
import ngvgroup.com.rpt.features.report.feign.ParameterFeignClient;
import ngvgroup.com.rpt.features.report.service.ComCfgParameterService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

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
