package ngv.vn.naascccd.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "CCCD_REGISTERS")
public class Register {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private String userId;

    @CreatedDate
    @Column(name = "registration_date", nullable = false, updatable = false)
    private Instant registrationDate = Instant.now();

    @CreatedDate
    @Column(name = "effective_date", nullable = false, updatable = false)
    private Instant effectiveDate = Instant.now();

    @CreatedDate
    @Column(name = "expiration_date", updatable = false)
    private Instant expirationDate;
    
    @Column(name = "use_life_cycle", length = 32)
    private String useLifeCycle;

    @Column(name = "is_auto_renew")
    @ColumnDefault("0")
    private boolean isAutoRenew;

    @CreatedDate
    @Column(name = "creation_time", nullable = false, updatable = false)
    private Instant creationTime = Instant.now();

    @Column(name = "creator_user_id")
    private String creatorUserId;

    @Column(name = "deleter_user_id")
    private String deleterUserId;

    @CreatedDate
    @Column(name = "deletion_time", updatable = false)
    private Instant deletionTime = Instant.now();

    @Column(name = "is_deleted")
    private boolean isDeleted;

}
