package ngv.vn.naascccd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "CCCD_MESSAGES")
public class Message {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_msg_id", nullable = false, length = 128)
    private UUID clientMsgId;
    
    @Column(name = "useId")
    private String userId;

    @Column(name = "partner_code", nullable = false, length = 32)
    private String partnerCode;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "response_code", length = 32)
    private String responseCode;

    @Column(name = "response_description", length = 4000)
    private String responseDescription;

    @Column(name = "ci_request")
    private String ciRequest;

    @Column(name = "ci_response", length = 4000)
    private String ciResponse;

    @Column(name = "partner_request")
    private String partnerRequest;

    @Column(name = "partner_response", length = 4000)
    private String partnerResponse;
    
    @PrePersist
    public void prePersist() {
        if (clientMsgId == null) {
            clientMsgId = UUID.randomUUID();
        }
    }
}
