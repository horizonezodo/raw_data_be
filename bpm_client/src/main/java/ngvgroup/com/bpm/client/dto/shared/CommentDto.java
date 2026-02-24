package ngvgroup.com.bpm.client.dto.shared;

import lombok.Data;
import java.util.Date;

@Data
public class CommentDto {
    private String content;
    private String createdByCode;
    private String createdByName;
    private String createdByPosition;
    private Date createdTime;
    private String taskName;
    private String status;
}
