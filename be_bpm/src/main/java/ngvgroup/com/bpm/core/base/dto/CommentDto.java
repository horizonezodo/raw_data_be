package ngvgroup.com.bpm.core.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String content;
    private String createdByCode;
    private String createdByName;
    private String createdByPosition;
    private Date createdTime;
    private String taskName;
    private String status;

    public CommentDto(String content, String createdByCode, Date createdTime, String taskName, String status) {
        this.content = content;
        this.createdByCode = createdByCode;
        this.createdTime = createdTime;
        this.taskName = taskName;
        this.status = status;
    }
}
