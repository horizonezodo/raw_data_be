package ngvgroup.com.bpm.client.service;

public interface DocumentTemplateService {
    /**
     * Generates a file by loading template and mapping, then filling data.
     * 
     * @param templatePath Path to the template file
     * @param mappingPath  Path to the mapping file
     * @param dataObject   The data object to map
     * @return Byte array of the generated file
     */
    byte[] generateFile(String templatePath, String mappingPath, Object dataObject);
}
