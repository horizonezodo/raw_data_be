package ngvgroup.com.rpt.features.transactionreport.common;

import com.aspose.cells.HtmlSaveOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ReadExcelUtil {
    private ReadExcelUtil() {}

    public static byte[] convertExcelToHtml(byte[] data) throws Exception {
        Workbook workbook = new Workbook(new ByteArrayInputStream(data));
        HtmlSaveOptions options = new HtmlSaveOptions(SaveFormat.HTML);
        options.setExportActiveWorksheetOnly(true);
        options.setExportImagesAsBase64(true);
        options.setExportGridLines(true);
        options.setExportRowColumnHeadings(true);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.save(out, options);
        return out.toByteArray();
    }
}
