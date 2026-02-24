package ngvgroup.com.loan.feature.currency.feign;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.loan.feature.currency.dto.CtgInfCurrencyTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "CurrencyFeign", url = "${service.common.url}")
public interface CurrencyFeign {
    @GetMapping("api/currency-type/get-all")
    ResponseData<List<CtgInfCurrencyTypeDto>> getAll();
}