package ngvgroup.com.rpt.features.transactionreport.common;

import com.aspose.words.Document;
import com.aspose.words.HtmlSaveOptions;
import com.aspose.words.SaveFormat;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class ReadWordUtil {
    private ReadWordUtil() {}

    public static byte[] convertWordToHtml(byte[] data) throws Exception {

        Document doc = new Document(new ByteArrayInputStream(data));

        HtmlSaveOptions options = new HtmlSaveOptions(SaveFormat.HTML);
        options.setExportImagesAsBase64(true);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out, options);
        return out.toByteArray();
    }
}
