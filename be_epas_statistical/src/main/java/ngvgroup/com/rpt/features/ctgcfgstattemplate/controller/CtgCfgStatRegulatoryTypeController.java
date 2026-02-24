package ngvgroup.com.rpt.features.ctgcfgstattemplate.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatRegulatoryTypeService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regulatory")
@RequiredArgsConstructor
public class CtgCfgStatRegulatoryTypeController {
    private final ExportExcel exportExcel;
    private final CtgCfgStatRegulatoryTypeService service;

    @LogActivity(function = "Lấy danh sách loại quy định")
    @GetMapping("/list")
    public ResponseEntity<ResponseData<List<CtgCfgStatRegulatoryTypeDTO>>> getAllData(){
        return ResponseData.okEntity(service.getAllData());
    }

    @LogActivity(function = "Tìm kiếm loại quy định")
    @GetMapping("/search")
    public ResponseEntity<ResponseData<Page<CtgCfgStatRegulatoryTypeDTO>>> search(@RequestParam String keyword, @ParameterObject Pageable pageable){
        return ResponseData.okEntity(service.pageRegulatory(keyword, pageable));
    }

    @LogActivity(function = "Xuất Excel loại quy định")
    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestParam String keyword) throws Exception {
        List<CtgCfgStatRegulatoryTypeDTO> dataList = this.service.exportExcel(keyword);
        return exportExcel.exportExcel(dataList,"Danh_sach_loai_quy_tac.xlsx");
    }

    @LogActivity(function = "Tạo mới loại quy định")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgCfgStatRegulatoryTypeDTO dto){
        this.service.createRegulatory(dto);
        return ResponseData.createdEntity();
    }

    @LogActivity(function = "Cập nhật loại quy định")
    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseData<Void>> update(@RequestBody CtgCfgStatRegulatoryTypeDTO dto, @PathVariable Long id){
        this.service.updateRegulatory(dto,id);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Xóa loại quy định")
    @PostMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable Long id){
        this.service.deleteRegulatory(id);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Lấy chi tiết loại quy định")
    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseData<CtgCfgStatRegulatoryTypeDTO>> getDetail(@PathVariable Long id){
        return ResponseData.okEntity(this.service.getDetail(id));
    }
}
