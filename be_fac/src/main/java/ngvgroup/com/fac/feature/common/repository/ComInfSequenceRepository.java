package ngvgroup.com.fac.feature.common.repository;

import jakarta.persistence.LockModeType;
import ngvgroup.com.fac.feature.common.model.ComInfSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComInfSequenceRepository extends JpaRepository<ComInfSequence, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ComInfSequence> findFirstByOrgCodeAndTableNameAndFieldNameAndPrefixAndPeriodValue(
            String orgCode,
            String tableName,
            String fieldName,
            String prefix,
            String periodValue
    );
}
