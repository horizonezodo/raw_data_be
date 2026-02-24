package ngvgroup.com.rpt.features.powerbi_cloud.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.rpt.features.powerbi_cloud.dto.EmbedConfigDTO;
import ngvgroup.com.rpt.features.powerbi_cloud.service.PowerBiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/power-bi-cloud")
@Slf4j
@RequiredArgsConstructor
public class PowerBiController{

    private final PowerBiService powerBiService;

    @LogActivity(function = "Lấy Power BI token")
    @GetMapping("/token")
    public ResponseEntity<ResponseData<EmbedConfigDTO>> getEmbedToken(
            @RequestParam String reportSource
    ) {
        try {
            EmbedConfigDTO config = powerBiService.getEmbedConfig(reportSource);
            return ResponseData.okEntity(config);

        } catch (BusinessException e) {
            throw e;

        } catch (Exception e) {
            throw new BusinessException(
                    ErrorCode.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    e
            );
        }
    }


}
