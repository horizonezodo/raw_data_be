package ngvgroup.com.bpmn.dto.ProcessMonitor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.sql.Blob;
@Getter
@Setter
public class StepDataDto {
    private String name;
    private String byteArrayId;
    private byte[] bytes;
    private Double doubleValue;
    private Long longValue;
    private String text;

    private String stepDataJson;



}

