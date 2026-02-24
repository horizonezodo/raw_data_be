package ngvgroup.com.loan.feature.type_of_capital_use.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.type_of_capital_use.dto.LnmCfgCapitalUseDTO;
import ngvgroup.com.loan.feature.type_of_capital_use.service.LnmCfgCapitalUseService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("capital")
public class LnmCfgCapitalUseController {
    private final LnmCfgCapitalUseService capitalUseService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<LnmCfgCapitalUseDTO>> getOne(@PathVariable("id")Long id){
        return ResponseData.okEntity(capitalUseService.findById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<LnmCfgCapitalUseDTO>>> search(@RequestParam(required = false) String keyword, @ParameterObject Pageable pageable){
        return ResponseData.okEntity(capitalUseService.search(keyword, pageable));
    }

    @PostMapping
    public ResponseEntity<ResponseData<LnmCfgCapitalUseDTO>> create(@RequestBody LnmCfgCapitalUseDTO dto){
        return ResponseData.okEntity(capitalUseService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<LnmCfgCapitalUseDTO>> update(@PathVariable("id")Long id,@RequestBody LnmCfgCapitalUseDTO dto){
        return ResponseData.okEntity(capitalUseService.update(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable("id")Long id){
        capitalUseService.delete(id);
        return ResponseData.okEntity();
    }

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportToExcel( @RequestParam  String fileName){
        return capitalUseService.exportToExcel(fileName);
    }

    @GetMapping("/all-code")
    public ResponseEntity<ResponseData<List<String>>> getAllCapitalCode(){
        return ResponseData.okEntity(capitalUseService.getAllCode());
    }
}
