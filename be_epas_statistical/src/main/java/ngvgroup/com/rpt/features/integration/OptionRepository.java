package ngvgroup.com.rpt.features.integration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {

    Optional<Option> findByParamCode(String settingName);
}