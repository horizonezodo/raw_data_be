package com.ngv.zns_service.repository;

import com.ngv.zns_service.model.entity.ZSSDvuMauZNS;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZSSDvuMauZNSRepository extends JpaRepository<ZSSDvuMauZNS, String> {

    List<ZSSDvuMauZNS> findByMaDvu(String maDvu);

    void deleteByMaDvuAndZoaIdAndMaMau(String maDvu, String zoaId, String maMau);

    List<ZSSDvuMauZNS> findAllByMaDvi(String maDvi);

    @Query(value = """
            SELECT zdm.MA_DVU, zd.TEN_DVU
            FROM ZSS_DVU_MAU_ZNS zdm
            JOIN ZSS_DVU zd ON zdm.MA_DVU = zd.MA_DVU
            WHERE zdm.MA_DVU IN (:mdv)
            """, nativeQuery = true)
    List<Object[]> checkExistMaDvuInMauBieu(@Param("mdv") List<String> mdv);
}
