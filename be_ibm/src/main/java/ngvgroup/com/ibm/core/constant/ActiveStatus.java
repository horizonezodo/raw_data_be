package ngvgroup.com.ibm.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActiveStatus {
    INACTIVE(0),
    ACTIVE(1);

    private final int value;
}
