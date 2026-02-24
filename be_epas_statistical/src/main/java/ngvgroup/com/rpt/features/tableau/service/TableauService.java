package ngvgroup.com.rpt.features.tableau.service;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.report.dto.ComCfgParameterDto;
import ngvgroup.com.rpt.features.report.feign.ParameterFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableauService {

    private final ParameterFeignClient parameterFeignClient;
    private final RestTemplate restTemplate;

    @Value("${tableau.username}")
    private String usernameTableau;

    public String getTicket(String targetSite) throws BusinessException {
        try {
            ResponseData<ComCfgParameterDto> tableauParameter =
                    parameterFeignClient.getParameterByCode(VariableConstants.TABLEAU_DOMAIN);

            String urlProxyTableau = Optional.ofNullable(tableauParameter)
                    .map(ResponseData::getData)
                    .map(ComCfgParameterDto::getParamValue)
                    .orElseThrow(() -> new BusinessException(StatisticalErrorCode.BAD_REQUEST));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("username", usernameTableau);
            if (targetSite != null && !targetSite.isEmpty()) {
                body.add("target_site", targetSite);
            }
            HttpEntity<MultiValueMap<String, String>> entity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            urlProxyTableau + "/tableau/get-ticket",
                            entity,
                            String.class
                    );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new BusinessException(StatisticalErrorCode.BAD_REQUEST);
            }

            return Optional.ofNullable(response.getBody())
                    .map(String::trim)
                    .filter(ticket -> !"-1".equals(ticket))
                    .orElseThrow(() -> new BusinessException(StatisticalErrorCode.BAD_REQUEST));

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error when getting Tableau ticket", e);
            throw new BusinessException(StatisticalErrorCode.BAD_REQUEST);
        }
    }


}
