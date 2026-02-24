package com.naas.category_service.repository;

import com.naas.category_service.model.CtgCfgScoringBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgScoringBenchmarkRepository extends JpaRepository<CtgCfgScoringBenchmark, Integer> {

    List<CtgCfgScoringBenchmark> getCtgCfgScoringBenchmarkByScoringTypeCode(@Param("scoringTypeCode") String scoringTypeCode);
}
