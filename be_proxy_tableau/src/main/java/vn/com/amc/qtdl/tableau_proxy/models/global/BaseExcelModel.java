package vn.com.amc.qtdl.tableau_proxy.models.global;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @Date: 20/09/2023
 * @Author: HungND.Os
 * @Comment: Base excel model
 */

@Data
@AllArgsConstructor
public class BaseExcelModel {
    private int headerRow;
    private int startRow;
    private String sheetName;
    private Map<String, Integer> headerColumnMapProperties;
}

