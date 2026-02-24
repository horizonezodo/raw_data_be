package ngvgroup.com.crm.features.common.service;

import java.util.List;

import ngvgroup.com.crm.features.common.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommonService {

    /**
     * Lấy danh sách cấu hình chung (Giới tính, Dân tộc, Loại KH...).
     * @param commonTypeCode Mã loại cấu hình (VD: CM001, CM007)
     */
    List<ComCfgCommonDto> getCommonConfigsByType(String commonTypeCode);

    /**
     * Lấy danh sách khu vực theo chi nhánh.
     * @param orgCode Mã chi nhánh
     */
    List<ComInfAreaDto> getAreasByOrg(String orgCode);

    /**
     * Lấy danh sách tất cả Tỉnh/Thành phố.
     */
    List<ComInfProvinceDto> getAllProvinces();

    /**
     * Lấy danh sách Phường/Xã thuộc một Tỉnh/Thành phố.
     * @param provinceCode Mã tỉnh
     */
    List<ComInfWardDto> getWardsByProvince(String provinceCode);

    /**
     * Lấy danh sách Loại hình kinh tế.
     */
    List<ComInfEconomicTypeDto> getAllEconomicTypes();

    /**
     * Lấy danh sách Ngành kinh tế.
     */
    List<ComInfIndustryDto> getAllIndustries();

    /**
     * Lấy danh sách Tổ chức/Chi nhánh.
     */
    List<ComInfOrganizationDto> getAllOrganizations();

    List<ComCfgCommonDto> getCommonConfigsByTypeAndParentCode(String commonTypeCode, String parentCode);

    /**
     * Lấy danh sách Template , phân trang
     */
    Page<TemplateResDto> searchAllTemplate(String keyword, Pageable pageable);

    TemplateResDto getTemplateByCode(String templateCode);

    List<ComInfWardDto> getWardsByOrg(String orgCode);

    List<ComInfAreaDto> getAreasByWard();
}