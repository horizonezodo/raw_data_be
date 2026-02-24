package com.naas.admin_service.core.utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.json.data.JsonDataSource;
import org.springframework.stereotype.Component;

@Component
public class JasperUtils {

    public byte[] exportToWord(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        exporter.exportReport();

        return outputStream.toByteArray();
    }

    public byte[] exportToExcel(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.setConfiguration(new SimpleXlsxReportConfiguration());
        exporter.exportReport();
        return out.toByteArray();
    }

    public JasperPrint fillFromJson(InputStream jrxmlStream,
            InputStream jsonStream) throws JRException {
        JasperReport report =
                JasperCompileManager.compileReport(jrxmlStream);
        JsonDataSource dataSource = new JsonDataSource(jsonStream);
        Map<String, Object> params = new HashMap<>();
        return JasperFillManager.fillReport(report, params, dataSource);
    }
}