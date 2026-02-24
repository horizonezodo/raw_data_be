package ngvgroup.com.fac.feature.regulated_account.service;
import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaVersionDTO;
import java.util.List;

public interface CtgCfgCoaVersionService {
    List<CtgCfgCoaVersionDTO> getAllData();
    List<CtgCfgCoaVersionDTO> getAllWithCommon(String commonTypeCode);
}
