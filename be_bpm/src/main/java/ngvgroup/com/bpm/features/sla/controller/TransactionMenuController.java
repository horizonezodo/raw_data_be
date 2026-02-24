package ngvgroup.com.bpm.features.sla.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.features.sla.dto.TransactionMenuDto;
import ngvgroup.com.bpm.features.sla.service.TransactionMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction-menu")
@AllArgsConstructor
public class TransactionMenuController {
    private final TransactionMenuService transactionMenuService;

    @Operation(summary = "Danh sách menu transaction 2 cấp: phân hệ và loại quy trình")
    @GetMapping
    public ResponseEntity<ResponseData<List<TransactionMenuDto>>> getTransactionMenu() {
        List<TransactionMenuDto> menuList = transactionMenuService.getTransactionMenu();
        return ResponseData.okEntity(menuList);
    }
}
