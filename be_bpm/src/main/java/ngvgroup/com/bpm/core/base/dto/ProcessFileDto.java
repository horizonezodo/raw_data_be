package ngvgroup.com.bpm.core.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessFileDto {
    private String processFileCode;
    private String processFileName;
    private List<FileDto> files;
}
