package com.naas.category_service.repository;

import com.naas.category_service.model.CtgCfgScoringIndcMapp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgScoringIndcMappRepository extends JpaRepository<CtgCfgScoringIndcMapp,Long> {

    List<CtgCfgScoringIndcMapp> getCtgCfgScoringIndcMappsByScoringIndcGroupCode(@Param("scoringIndcGroupCode") String scoringIndcGroupCode);
}
