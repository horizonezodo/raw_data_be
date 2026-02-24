package ngvgroup.com.rpt.features.smrscore.repository;

import ngvgroup.com.rpt.features.smrscore.model.SmrScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmrScoreRepository extends JpaRepository<SmrScore,Long> {

}
