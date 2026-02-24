package ngvgroup.com.bpm.client.dto.shared;

import java.util.List;

import lombok.Data;

@Data
public class ProcessFileDto {
    private String processFileCode;
    private String processFileName;
    private List<FileDto> files;
}
