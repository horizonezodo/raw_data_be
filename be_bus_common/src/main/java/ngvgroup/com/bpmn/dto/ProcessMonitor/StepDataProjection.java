package ngvgroup.com.bpmn.dto.ProcessMonitor;

import java.sql.Blob;

public interface StepDataProjection {
    String getName();
    String getByteArrayId();
    Blob getBytes(); // sẽ convert sang byte[] trong Service
    Double getDoubleValue();
    Long getLongValue();
    String getText();
}
