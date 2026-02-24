package ngvgroup.com.rpt.features.transactionreport.service.impl;

import com.aspose.cells.*;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.StoredProcedureService;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.*;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplate;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateSheet;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTemplateKpiRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTemplateRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTemplateSheetRepository;
import ngvgroup.com.rpt.features.ctgcfgstatus.model.CtgCfgStatus;
import ngvgroup.com.rpt.features.ctgcfgstatus.repository.CtgCfgStatusRepository;
import ngvgroup.com.rpt.features.report.repository.ComInfOrganizationRepository;
import ngvgroup.com.rpt.features.transactionreport.common.FileDownloadUtil;
import ngvgroup.com.rpt.features.transactionreport.common.GetOrderedColumns;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.AdjustmentInformationDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.CheckDetailDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.DumpTableConfigDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.ExcelChangeIndexDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.KeepTrackActionsDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.ReportFileRequestDto;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatTemplate;
import ngvgroup.com.rpt.features.transactionreport.repository.ComCfgParameterRepository;
import ngvgroup.com.rpt.features.transactionreport.repository.RptTxnStatTemplateKpiRepository;
import ngvgroup.com.rpt.features.transactionreport.repository.RptTxnStatTemplateRepository;
import ngvgroup.com.rpt.features.transactionreport.repository.RptTxnStatTemplateStatusRepository;
import ngvgroup.com.rpt.features.transactionreport.service.TransactionReportSubService;
import org.hibernate.dialect.OracleTypes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionReportServiceSubImpl implements TransactionReportSubService {

    private final RptTxnStatTemplateKpiRepository kpiRepo;
    private final RptTxnStatTemplateStatusRepository statusRepo;
    private final RptTxnStatTemplateRepository rptRepo;

    private final CtgCfgStatTemplateRepository templateRepository;
    private final CtgCfgStatTemplateSheetRepository templateSheetRepository;
    private final CtgCfgStatTemplateKpiRepository templateKpiRepository;
    private final CtgCfgStatusRepository ctgCfgStatusRepo;

    private final StoredProcedureService procedureService;

    private final ComCfgParameterRepository comCfgParameterRepository;
    private final ComInfOrganizationRepository organizationRepository;

    private final GetOrderedColumns columns;

    private final JdbcTemplate jdbc;

    @Override
    public Page<CheckDetailDto> getCheckDetail(String statInstanceCode, String search, Pageable pageable) {
        return kpiRepo.searchCheckDetail(statInstanceCode, search, pageable);
    }

    @Override
    public List<AdjustmentInformationDto> getAdjustmentInformation(String statInstanceCode, String search) {
        return kpiRepo.searchAdjustmentInformation(statInstanceCode, search);
    }

    @Override
    public Page<KeepTrackActionsDto> getKeepTrackActions(String statInstanceCode, String search, Pageable pageable) {
        Page<KeepTrackActionsDto> result = statusRepo.searchKeepTrackActions(statInstanceCode, search, pageable);
        for(KeepTrackActionsDto dto : result) {
            if(VariableConstants.ACHIEVED.equalsIgnoreCase(dto.getSlaStatus())) {
                dto.setSlaStatus(VariableConstants.GUI_IN_TERM);
            } else if(VariableConstants.BREACHED.equalsIgnoreCase(dto.getSlaStatus())) {
                dto.setSlaStatus(VariableConstants.GUI_OVERDUE);
            }
        }
        return result;
    }

    @Override
    public CtgCfgStatTemplate getFileByTemplateCode(String templateCode) {
        CtgCfgStatTemplate ctgCfgStatTemplate = templateRepository.findByTemplateCode(templateCode);
        if (ctgCfgStatTemplate == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, templateCode);
        }
        return ctgCfgStatTemplate;
    }

    @Override
    public Map<String, String> getReportSheetsAsHtml(String templateCode, ReportFileRequestDto requestDto) throws BusinessException {
        try {
            CtgCfgStatTemplate template = getTemplateOrThrow(templateCode);

            // Lấy biến isExport từ DTO
            Workbook workbook = buildWorkbookFromTemplate(template);

            // header CM0x chung cho tất cả sheet (Lấy instanceCode và guiStatus từ DTO)
            fillCommonHeader(workbook, template, requestDto.getInstanceCode(), requestDto.getGuiStatus());

            List<CtgCfgStatTemplateSheet> sheets = getTemplateSheetsOrThrow(templateCode);

            // Đổ dữ liệu từng sheet + lưu index cell được phép sửa
            Map<Integer, List<List<Integer>>> sheetIndexs = fillSheetsWithData(workbook, template, sheets, requestDto.getInstanceCode());

            // nếu có công thức phụ thuộc vào dữ liệu vừa đổ
            workbook.calculateFormula();

            boolean editable = computeEditable(template, requestDto.getInstanceCode());

            // Xuất từng sheet → HTML
            return exportSheetsAsHtml(workbook, sheetIndexs, editable);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(StatisticalErrorCode.EXPORT_EXCEL_FAILED, e.getMessage());
        }
    }

    /* ===== helper cho getReportSheetsAsHtml ===== */

    private CtgCfgStatTemplate getTemplateOrThrow(String templateCode) {
        CtgCfgStatTemplate template = templateRepository.findByTemplateCode(templateCode);
        if (template == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, templateCode);
        }
        return template;
    }

    private Workbook buildWorkbookFromTemplate(CtgCfgStatTemplate template) throws BusinessException {
        byte[] blob = template.getTemplateFile();

        if (blob == null || blob.length == 0) {
            throw new BusinessException(StatisticalErrorCode.EMPTY_TEMPLATE_FILE, template.getTemplateCode());
        }

        try (InputStream is = new ByteArrayInputStream(blob)) {
            return new Workbook(is);
        } catch (Exception e) {
            throw new BusinessException(StatisticalErrorCode.BAD_READ_FILE, template.getTemplateCode());
        }
    }

    private Workbook buildWorkbookFromResultTemplate(CtgCfgStatTemplate template) throws BusinessException {
        byte[] blob = template.getTemplateReportFile();

        if (blob == null || blob.length == 0) {
            throw new BusinessException(StatisticalErrorCode.EMPTY_TEMPLATE_FILE, template.getTemplateCode());
        }

        try (InputStream is = new ByteArrayInputStream(blob)) {
            return new Workbook(is);
        } catch (Exception e) {
            throw new BusinessException(StatisticalErrorCode.BAD_READ_FILE, template.getTemplateCode());
        }
    }

    private List<CtgCfgStatTemplateSheet> getTemplateSheetsOrThrow(String templateCode) {
        List<CtgCfgStatTemplateSheet> sheets =
                templateSheetRepository.findByTemplateCodeOrderBySheetDataAsc(templateCode);
        if (sheets.isEmpty()) {
            throw new BusinessException(StatisticalErrorCode.INVALID_SHEET_CONFIG, templateCode);
        }
        return sheets;
    }

    private Map<Integer, List<List<Integer>>> fillSheetsWithData(Workbook workbook,
                                                                 CtgCfgStatTemplate template,
                                                                 List<CtgCfgStatTemplateSheet> sheets,
                                                                 String statInstanceCode) {
        Map<Integer, List<List<Integer>>> sheetIndexs = new HashMap<>();

        for (CtgCfgStatTemplateSheet sh : sheets) {
            // SHEET_DATA = "1", "2", "3" → Aspose index 0-based
            int sheetIdx = toInt(Double.parseDouble(sh.getSheetData())) - 1;
            if (sheetIdx < 0 || sheetIdx >= workbook.getWorksheets().getCount()) {
                continue;
            }

            Worksheet ws = workbook.getWorksheets().get(sheetIdx);

            // Xoá các hàng “placeholder” trước khi đổ dữ liệu (nếu có cấu hình)
            if (sh.getRowToDelete() > 0) {
                ws.getCells().deleteRows(toInt(sh.getRowStart()) - 1, toInt(sh.getRowToDelete()));
            }

            if (VariableConstants.TEMPLATE_DATA_EDIT.equalsIgnoreCase(template.getTemplateDataType())) {
                // trả về list các ô (row, col) đã được map dữ liệu – 0-based
                sheetIndexs.put(sheetIdx, fillFixedKpi(ws, sh, statInstanceCode));
            } else {
                fillDumpTable(ws, sh, template.getColumnStart(), statInstanceCode);
            }
        }

        return sheetIndexs;
    }

    private Map<String, String> exportSheetsAsHtml(Workbook workbook,
                                                   Map<Integer, List<List<Integer>>> sheetIndexs,
                                                   boolean editable) throws BusinessException {
        Map<String, String> sheetHtmlMap = new LinkedHashMap<>();

        try {
            for (int i = 0; i < workbook.getWorksheets().getCount(); i++) {
                Worksheet sheet = workbook.getWorksheets().get(i);
                int sheetIdx = sheet.getIndex();

                Workbook temp = new Workbook();
                temp.getWorksheets().clear();
                Worksheet one = temp.getWorksheets().add(sheet.getName());
                one.copy(sheet);

                HtmlSaveOptions opt = createHtmlSaveOptions();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                temp.save(out, opt);

                String html = out.toString(StandardCharsets.UTF_8);
                if (editable) {
                    // chỉ bật sửa các ô đã map
                    html = enableEditable(html, sheetIndexs.get(sheetIdx));
                }

                sheetHtmlMap.put(sheet.getName(), html);
            }

            return sheetHtmlMap;
        } catch (Exception e) {
            throw new BusinessException(StatisticalErrorCode.EXPORT_EXCEL_FAILED, e.getMessage());
        }
    }

    private HtmlSaveOptions createHtmlSaveOptions() {
        HtmlSaveOptions opt = new HtmlSaveOptions(SaveFormat.HTML);
        opt.setExportActiveWorksheetOnly(true);
        opt.setExportImagesAsBase64(true);
        opt.setExportGridLines(true);
        opt.setExportRowColumnHeadings(true); //xuất không cần thêm header
        return opt;
    }


    private String fillCommonHeader(Workbook workbook,
                                  CtgCfgStatTemplate template,
                                  String statInstanceCode,
                                  String guiStatus) {

        // ---- 1. Lấy thông tin từ RPT_TXN_STAT_TEMPLATE ----
        RptTxnStatTemplate rpt = rptRepo.findByStatInstanceCode(statInstanceCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, statInstanceCode));

        String templateCode = template.getTemplateCode();
        String orgCode = rpt.getOrgCode();
        Date reportDataDate = rpt.getReportDataDate();
        Integer exportCount = rpt.getExportCount();

        // ---- 2. Tính các phần tử CM01–CM06 ----

        // MaDVBC – Mã đơn vị gửi báo cáo
        String maDvBc = organizationRepository.findOrgLegalCodeByOrgCode(orgCode);

        // MaDVPS – Mã đơn vị phát sinh dữ liệu
        String maDvPs = organizationRepository.findOrgLegalCodeByOrgCode(orgCode);

        // NgayDL: yyyyMMdd
        String ngayDl = new java.text.SimpleDateFormat("yyyyMMdd").format(reportDataDate);

        // TinhTrangGui: SI / BI
        String tinhTrangGui;
        if (VariableConstants.GUI_IN_TERM_CODE.equalsIgnoreCase(guiStatus)) {
            tinhTrangGui = VariableConstants.SEND_STATUS_SI;
        } else if (VariableConstants.GUI_OVERDUE_CODE.equalsIgnoreCase(guiStatus)) {
            tinhTrangGui = VariableConstants.SEND_STATUS_BI;
        } else {
            tinhTrangGui = "";
        }

        // DuLieuPS: N / M tuỳ GENERATED_DATA_TYPE
        String duLieuPs;
        if (VariableConstants.GENERATE_DATA_NORMAL.equalsIgnoreCase(template.getGenerateDataType())) {
            duLieuPs = VariableConstants.POSTING_DATA_NORMAL;
        } else if (VariableConstants.GENERATE_DATA_MODIFIED.equalsIgnoreCase(template.getGenerateDataType())) {
            duLieuPs = VariableConstants.POSTING_DATA_MODIFIED;
        } else {
            duLieuPs = "";
        }

        // LanXuat
        String lanXuat = (exportCount == null ? "1" : exportCount.toString());

        // CM01 – Tên file [MaMauBieu]-[MaDVBC]-[MaDVPS]-[NgayDL]-[TinhTrangGui]-[DuLieuPS]-[LanXuat].xlsx
        String fileName = String.format("%s-%s-%s-%s-%s-%s-%s.xlsx",
                templateCode,
                maDvBc,
                maDvPs,
                ngayDl,
                tinhTrangGui,
                duLieuPs,
                lanXuat
        );

        // CM04 – Ngày dữ liệu hiển thị
        String cm04 = new java.text.SimpleDateFormat("dd/MM/yyyy").format(reportDataDate);

        // CM05 – Tên/SĐT người kiểm duyệt
        String cm05 = comCfgParameterRepository.findParamValueByParamCodeAndOrgCode(
                "RPT_STAT_USERNAME_APPROVE", "%");

        // CM06 – ID người kiểm duyệt
        String cm06 = comCfgParameterRepository.findParamValueByParamCodeAndOrgCode(
                "RPT_STAT_USERID_APPROVE", "%");

        // ---- 3. Ghi vào B1..C8 cho TẤT CẢ worksheet ----
        for (int i = 0; i < workbook.getWorksheets().getCount(); i++) {
            Worksheet ws = workbook.getWorksheets().get(i);
            Cells cells = ws.getCells();

            int startRow = -1;

            // check từ dòng 1 -> 8 (index 0 -> 7)
            for (int row = 0; row <= 7; row++) {
                String colB = cells.get(row, 1).getStringValue(); // cột B
                String colC = cells.get(row, 2).getStringValue(); // cột C

                if ((colB != null && !colB.trim().isEmpty())
                        || (colC != null && !colC.trim().isEmpty())) {
                    startRow = row;
                    break;
                }
            }

            // nếu tìm được dòng bắt đầu thì ghi dữ liệu
            if (startRow != -1) {
                cells.get(startRow, 2).putValue(fileName);
                cells.get(startRow + 1, 2).putValue(maDvBc);
                cells.get(startRow + 2, 2).putValue(maDvPs);
                cells.get(startRow + 3, 2).putValue(cm04);
                cells.get(startRow + 4, 2).putValue(cm05);
                cells.get(startRow + 5, 2).putValue(cm06);
            }
        }


        return fileName;
    }

    /**
     * CASE 1: Cố định hàng/cột – đổ dữ liệu KPI theo join
     * CTG_CFG_STAT_TEMPLATE_KPI với bảng TABLE_DATA (TABLE_DATA.KPI_VALUE).
     */
    private List<List<Integer>> fillFixedKpi(Worksheet ws,
                                             CtgCfgStatTemplateSheet sh,
                                             String statInstanceCode) {

        // --- 1. Lấy range cấu hình trên sheet ---
        int rStart = toInt(sh.getRowStart());
        int rEnd   = toInt(sh.getRowEnd());
        int cStart = toInt(sh.getColumnStart());
        int cEnd   = toInt(sh.getColumnEnd());

        // --- 2. Lấy TABLE_DATA ---
        String table = whitelistTable(sh.getTableData());   // ví dụ: RPT_TXN_A02211

        // --- 3. Join CTG_CFG_STAT_TEMPLATE_KPI với TABLE_DATA theo KPI_CODE ---
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT k.ROW_INDEX, k.COLUMN_INDEX, d.KPI_VALUE ");
        sql.append("FROM CTG_CFG_STAT_TEMPLATE_KPI k ");
        sql.append("JOIN ").append(table).append(" d ON k.TEMPLATE_KPI_CODE = d.KPI_CODE ");
        sql.append("WHERE k.TEMPLATE_CODE = ? AND d.STAT_INSTANCE_CODE = ? AND d.IS_ACTIVE = 1");

        List<Object> params = new ArrayList<>();
        params.add(sh.getTemplateCode());
        params.add(statInstanceCode);

        if (rStart > 0) {
            sql.append(" AND k.ROW_INDEX >= ?");
            params.add(rStart);
        }
        if (rEnd > 0) {
            sql.append(" AND k.ROW_INDEX <= ?");
            params.add(rEnd);
        }
        if (cStart > 0) {
            sql.append(" AND k.COLUMN_INDEX >= ?");
            params.add(cStart);
        }
        if (cEnd > 0) {
            sql.append(" AND k.COLUMN_INDEX <= ?");
            params.add(cEnd);
        }

        List<Map<String, Object>> rows =
                jdbc.queryForList(sql.toString(), params.toArray());

        if (rows.isEmpty()) {
            return List.of();
        }

        // --- 4. Gán KPI_VALUE vào đúng vị trí trên Excel + ghi nhớ index (0-based) ---
        List<List<Integer>> indexes = new ArrayList<>();
        Cells cells = ws.getCells();

        for (Map<String, Object> row : rows) {
            int r = toIntObj(row.get("ROW_INDEX")) - 1; // 0-based
            int c = toIntObj(row.get("COLUMN_INDEX")) - 1; // 0-based

            Object rawVal = row.get("KPI_VALUE");
            String value = Objects.toString(rawVal, "");

            Cell cell = cells.get(r, c);

            // Nếu ô thuộc merged range → ghi vào góc trên trái vùng gộp
            Range merged = cell.getMergedRange();
            if (merged != null) {
                r = merged.getFirstRow();
                c = merged.getFirstColumn();
                cell = cells.get(r, c);
            }

            // ================================
            // ✅ XỬ LÝ GIÁ TRỊ KPI (FIX E+)
            // ================================
            if (rawVal == null) {
                cell.putValue("");
            }
            else if (rawVal instanceof java.math.BigDecimal) {
                java.math.BigDecimal bd = (java.math.BigDecimal) rawVal;
                cell.putValue(formatNumberView(bd));
                applyNumberStyle(cell);
            }
            else if (rawVal instanceof Number) {
                java.math.BigDecimal bd = new java.math.BigDecimal(rawVal.toString());
                cell.putValue(formatNumberView(bd));
                applyNumberStyle(cell);
            }
            else if (value.matches("-?\\d+(\\.\\d+)?")) {
                java.math.BigDecimal bd = new java.math.BigDecimal(value);
                cell.putValue(formatNumberView(bd));
                applyNumberStyle(cell);
            }
            else {
                cell.putValue(value);
            }

            // lưu index 0-based để map sang HTML
            indexes.add(List.of(r, c));
        }

        return indexes;
    }

    private String formatNumberView(Object rawVal) {
        if (rawVal == null) return "";

        try {
            java.math.BigDecimal bd = new java.math.BigDecimal(rawVal.toString());

            java.text.DecimalFormatSymbols symbols = new java.text.DecimalFormatSymbols();
            symbols.setGroupingSeparator(','); // ngăn cách hàng nghìn
            symbols.setDecimalSeparator('.');  // dấu thập phân

            java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.##", symbols);
            return df.format(bd);
        } catch (Exception e) {
            return rawVal.toString();
        }
    }


    /**
     * ✅ Set format Excel để tránh scientific notation (2.2E+13)
     */
    private void applyNumberStyle(Cell cell) {
        Style style = cell.getStyle();
        style.setCustom("#,##0.00");
        style.setCustom("0"); // hiển thị số đầy đủ
        Font font = style.getFont();
        font.setName("Times New Roman");
        style.setHorizontalAlignment(TextAlignmentType.RIGHT);
        cell.setStyle(style);
    }

    /**
     * Helper: convert Object -> int an toàn (dùng cho giá trị lấy từ DB)
     */
    private static int toIntObj(Object o) {
        if (o == null) return 0;
        if (o instanceof Number n) {
            return n.intValue();
        }
        return Integer.parseInt(o.toString());
    }

    /**
     * Đổ dữ liệu dạng bảng:
     * - dataColumnStart: cột THỨ MẤY trong bảng dữ liệu bắt đầu lấy (1-based, ví dụ 15)
     * - sh.getRowStart(), sh.getColumnStart(): vị trí bắt đầu đổ trên template (1-based)
     * - nếu có ROW_END/COLUMN_END thì sẽ không đổ quá phạm vi đó
     */
    private void fillDumpTable(Worksheet ws,
                               CtgCfgStatTemplateSheet sh,
                               int dataColumnStart,
                               String statInstanceCode) {

        DumpTableConfigDto config = createDumpTableConfig(sh, dataColumnStart, statInstanceCode);
        if (config.getColsToUse() == null || config.getColsToUse().isEmpty()) {
            return;
        }

        List<Map<String, Object>> rows = loadDumpRows(config);
        if (rows.isEmpty()) {
            return;
        }

        writeDumpRows(ws.getCells(), config, rows);
    }

    /* ===== helper cho fillDumpTable ===== */

    private DumpTableConfigDto createDumpTableConfig(CtgCfgStatTemplateSheet sh, int dataColumnStart, String statInstanceCode) {
        DumpTableConfigDto cfg = new DumpTableConfigDto();

        cfg.setStartRow(toInt(sh.getRowStart()) - 1);
        cfg.setEndRow(toInt(sh.getRowEnd()) - 1);
        cfg.setStartCol(toInt(sh.getColumnStart()) - 1);
        cfg.setEndCol(toInt(sh.getColumnEnd()) - 1);
        cfg.setTable(sh.getTableData());

        cfg.setStatInstanceCode(statInstanceCode);

        // 1) Lấy danh sách cột theo đúng thứ tự trong Oracle
        List<String> orderedCols = columns.getOrderedColumns(cfg.getTable());
        if (orderedCols.isEmpty()) {
            List<Map<String, Object>> tmp =
                    jdbc.queryForList("SELECT * FROM " + cfg.getTable() + " WHERE ROWNUM <= 1");
            if (!tmp.isEmpty()) {
                orderedCols = new ArrayList<>(tmp.get(0).keySet());
            }
        }

        if (orderedCols.isEmpty()) {
            cfg.setColsToUse(Collections.emptyList());
            return cfg;
        }

        // 2) Xác định danh sách cột sẽ lấy từ dataColumnStart trở đi (1-based -> 0-based)
        int dataStartIndex = Math.max(0, dataColumnStart - 1);
        if (dataStartIndex >= orderedCols.size()) {
            cfg.setColsToUse(Collections.emptyList());
            return cfg;
        }

        cfg.setColsToUse(orderedCols.subList(dataStartIndex, orderedCols.size()));
        return cfg;
    }

    private List<Map<String, Object>> loadDumpRows(DumpTableConfigDto cfg) {
        if (cfg.getColsToUse() == null || cfg.getColsToUse().isEmpty()) {
            return Collections.emptyList();
        }

        String selectCols = String.join(", ", cfg.getColsToUse());

        // Primary SQL with sorting
        String sql = "SELECT " + selectCols + " FROM " + cfg.getTable() 
            + " WHERE IS_ACTIVE = 1 AND STAT_INSTANCE_CODE = ? ORDER BY SORT_NUMBER ASC";

        try {
            return jdbc.queryForList(sql, cfg.getStatInstanceCode());
        } catch (DataAccessException ex) {
            // Fallback SQL: Same filtering, but removed ORDER BY SORT_NUMBER
            String fallbackSql = "SELECT " + selectCols + " FROM " + cfg.getTable()
                + " WHERE IS_ACTIVE = 1 AND STAT_INSTANCE_CODE = ?";
                
            return jdbc.queryForList(fallbackSql, cfg.getStatInstanceCode());
        }
    }

    private void writeDumpRows(Cells cells,
                               DumpTableConfigDto cfg,
                               List<Map<String, Object>> rows) {

        int curRow = cfg.getStartRow();

        // Tính số cột tối đa sẽ ghi, tránh dùng break trong vòng lặp
        int maxColumns = cfg.getColsToUse().size();
        if (cfg.getEndCol() >= 0) {
            int allowedCols = cfg.getEndCol() - cfg.getStartCol() + 1;
            maxColumns = Math.min(maxColumns, allowedCols);
        }

        for (Map<String, Object> row : rows) {
            // nếu có giới hạn ROW_END và đã vượt → kết thúc method
            if (cfg.getEndRow() >= 0 && curRow > cfg.getEndRow()) {
                return; // tương đương break outer loop, nhưng không vi phạm rule Sonar
            }

            int curCol = cfg.getStartCol();
            for (int colIndex = 0; colIndex < maxColumns; colIndex++) {
                String colName = cfg.getColsToUse().get(colIndex);

                Object v = row.get(colName);
                Cell cell = cells.get(curRow, curCol);

                if (v instanceof Number) {
                    cell.putValue(v);
                    Style style = cell.getStyle();
                    style.setCustom("0"); // hiển thị số nguyên đầy đủ
                    cell.setStyle(style);
                } else {
                    cell.putValue(v == null ? "" : v.toString());
                }

                curCol++;
            }

            curRow++;
        }
    }

    /**
     * Quyền sửa theo nghiệp vụ
     */
    private boolean computeEditable(CtgCfgStatTemplate template, String statInstanceCode) {
        boolean editable = false;

        Optional<RptTxnStatTemplate> rptOpt = rptRepo.findByStatInstanceCode(statInstanceCode);
        if (rptOpt.isEmpty()) return false;
        RptTxnStatTemplate rpt = rptOpt.get();

        if (!template.getTemplateCode().equalsIgnoreCase(rpt.getTemplateCode())) {
            return false;
        }

        Optional<CtgCfgStatus> stOpt = ctgCfgStatusRepo.findByStatusCode(rpt.getCurrentStatusCode());
        if (stOpt.isEmpty()) return false;
        CtgCfgStatus st = stOpt.get();

        if (Integer.valueOf(1).equals(st.getIsAllowEdit())
                && VariableConstants.TEMPLATE_DATA_EDIT.equalsIgnoreCase(template.getTemplateDataType())) {
            editable = true;
        }

        return editable;
    }

    // --- Helper: set contenteditable cho đúng cell đã map ---

    private String enableEditable(String html, List<List<Integer>> sheetIndexs) {
        if (sheetIndexs == null || sheetIndexs.isEmpty()) {
            return html;
        }

        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("tr");

        for (List<Integer> index : sheetIndexs) {

            boolean valid = index != null && index.size() >= 2 && (index.get(0) + 1) >= 0 && (index.get(0) + 1) < rows.size();

            if (valid) {
                int r = index.get(0) + 1; // 1-based
                int c = index.get(1) + 1; // 1-based

                Element tr = rows.get(r);
                // lấy cả td & th
                Elements cols = tr.select("td, th");
                if (c >= 0 && c < cols.size()) {
                    cols.get(c).attr("contenteditable", "true");
                }
            }
        }

        return doc.outerHtml();
    }

    private String whitelistTable(String tableName) {
        if (tableName == null || !tableName.matches("[A-Za-z0-9_.]+")) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, tableName);
        }
        return tableName;
    }

    private static int toInt(double d) {
        return (int) Math.floor(d);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getKpiDetail(String templateCode, String kpiCode, String orgCode, Date reportDataDate) {
        List<StoredProcedureParameter> parameters = List.of(
                StoredProcedureParameter.input("p_template_code", templateCode),
                StoredProcedureParameter.input("p_org_code", orgCode),
                StoredProcedureParameter.input("p_report_data_date",
                        new java.sql.Timestamp(reportDataDate.getTime())),
                StoredProcedureParameter.input("p_kpi_code", kpiCode),
                StoredProcedureParameter.output("p_cursor", OracleTypes.CURSOR)
        );

        return (List<Map<String, Object>>) procedureService.execute(VariableConstants.COMMON_KPI_DETAIL_SP, parameters)
                .getOutputParameters().get("p_cursor");
    }

    public void fillUnsavedData(Workbook workbook, 
                                Map<String, List<ExcelChangeIndexDto>> listChanges) {
        
        // 1. Kiểm tra dữ liệu đầu vào
        if (workbook == null || listChanges == null || listChanges.isEmpty()) {
            return;
        }

        // 2. Duyệt qua từng Entry trong Map (Key: Sheet Index, Value: List các thay đổi)
        for (Map.Entry<String, List<ExcelChangeIndexDto>> entry : listChanges.entrySet()) {
            String sheetName = entry.getKey();
            List<ExcelChangeIndexDto> changes = entry.getValue();

            // Lấy sheet theo index
            Worksheet ws = workbook.getWorksheets().get(sheetName);
            if (ws == null) continue;

            // 3. Duyệt qua danh sách các cell cần thay đổi trong sheet này
            if (changes != null) {
                Cells cells = ws.getCells();
                for (ExcelChangeIndexDto change : changes) {
                    Cell cell = cells.get(change.getRow(), change.getCol());
                    cell.putValue(change.getData());
                }
            }
        }
    }

        @Override
        public ResponseEntity<byte[]> getReportResultExcel(String templateCode, ReportFileRequestDto requestDto) {
            try {
                CtgCfgStatTemplate template = getTemplateOrThrow(templateCode);

                // Lấy biến isExport từ DTO
                Workbook workbook = buildWorkbookFromResultTemplate(template);

                // header CM0x chung cho tất cả sheet (Lấy instanceCode và guiStatus từ DTO)
                String fileName = fillCommonHeader(workbook, template, requestDto.getInstanceCode(), requestDto.getGuiStatus());

                List<CtgCfgStatTemplateSheet> sheets = getTemplateSheetsOrThrow(templateCode);

                // Đổ dữ liệu từng sheet + lưu index cell được phép sửa
                fillSheetsWithData(workbook, template, sheets, requestDto.getInstanceCode());

                fillUnsavedData(workbook, requestDto.getListChanges());

                // nếu có công thức phụ thuộc vào dữ liệu vừa đổ
                workbook.calculateFormula();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                workbook.save(outputStream, SaveFormat.XLSX);
                outputStream.close();

                return FileDownloadUtil.buildXlsxResponse(fileName, outputStream.toByteArray());

            } catch (BusinessException e) {
                throw e;
            } catch (Exception e) {
                throw new BusinessException(StatisticalErrorCode.EXPORT_EXCEL_FAILED, e.getMessage());
            }
        }
    }
