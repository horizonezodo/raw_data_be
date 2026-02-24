package ngvgroup.com.fac.feature.fac_inf_acc.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.*;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAcc;
import ngvgroup.com.fac.feature.fac_inf_acc.service.FacInfAccService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("fac-inf-acc")
public class FacInfAccController extends BaseController<FacInfAcc, FacInfAccDetailDto, FacInfAccService> {
    private final ExportExcel exportExcel;

    public FacInfAccController(FacInfAccService service, ExportExcel exportExcel) {
        super(service);
        this.exportExcel = exportExcel;
    }

    @GetMapping("search")
    public ResponseEntity<ResponseData<Page<FacInfAccDto>>> getData(
            @RequestParam(required = false) List<String> accScope,
            Pageable pageable) {
        return ResponseData.okEntity(service.search(accScope, pageable));
    }

    @PostMapping("add")
    public ResponseEntity<ResponseData<String>> create(
            @Valid @RequestBody FacInfAccRequest request) {
        service.createFacInfAcc(request);
        return ResponseData.okEntity("Tạo mới thành công");
    }

    @PutMapping("/update")
    @Operation(summary = "Cập nhật thông tin tài khoản nội bộ")
    public ResponseEntity<ResponseData<String>> update(@Valid @RequestBody FacInfAccRequest req) {
        service.updateFacInfAcc(req);
        return ResponseData.okEntity("Cập nhật thành công");
    }

    @DeleteMapping("delete")
    @Operation(summary = "Xoá thông tin tài khoản nội bộ (update isDelete = 1)")
    public ResponseEntity<ResponseData<String>> delete(@RequestParam String accNo) {
        service.deleteFacInfAcc(accNo);
        return ResponseData.okEntity("Xoá thành công");
    }

    @PostMapping("/generate-account-no")
    public ResponseEntity<ResponseData<String>> generateAccountNo(
            @RequestBody @Valid GenerateAccountNoRequest request) {
        String response =
                service.generateAccountNo(request);
        return ResponseData.okEntity(response);
    }

    @GetMapping("/get-by-org-code")
    public ResponseEntity<ResponseData<List<FacInfAccDto>>> getAllByOrgCode(@RequestParam String orgCode){
        return ResponseData.okEntity(service.getByOrgCode(orgCode));
    }

    @PostMapping("export-excel")
    public ResponseEntity<byte[]> exportExcel() throws Exception {
        return exportExcel.exportExcel(service.exportExcel(), "Danh sách tài khoản nội bộ.xlsx");
    }

    @GetMapping("/get-acc-no")
    public ResponseEntity<ResponseData<List<String>>> getAccNo(
            @RequestParam(required = false) String accClassCode,
            @RequestParam(required = false) String orgCode
    ) {
        return ResponseData.okEntity(service.getAccNo(accClassCode, orgCode));
    }

    @GetMapping("/get-all-by-acc-no")
    public ResponseEntity<ResponseData<FacInfAccDtoRes>> getAllByAccNo(
            @RequestParam(required = false) String accNo
    ) {
        return ResponseData.okEntity(service.getAllByAccNo(accNo));
    }

    @GetMapping("/get-acc-classes")
    public ResponseEntity<List<String>> getAccClasses() {
        return ResponseEntity.ok(service.getAccClassCodes());
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<FacInfAccDto>> getAccounts(
            @RequestParam String accClassCode,
            @RequestParam String orgCode) {
        return ResponseEntity.ok(service.getAccountsByClass(accClassCode, orgCode));
    }

}
