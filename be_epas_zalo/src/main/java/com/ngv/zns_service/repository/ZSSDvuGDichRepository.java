package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.response.dvu.CtDvMapGdDto;
import com.ngv.zns_service.model.entity.ZSSDvuGDich;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZSSDvuGDichRepository extends JpaRepository<ZSSDvuGDich, String> {
        @Query(value = """
                        SELECT zdg.MA_DVU, zd.TEN_DVU
                        FROM ZSS_DVU_GDICH zdg
                        JOIN ZSS_DVU zd ON zdg.MA_DVU = zd.MA_DVU
                        WHERE zdg.MA_DVU IN (:mdv)
                        """, nativeQuery = true)
        List<Object[]> checkExistMaDvu(@Param("mdv") List<String> mdv);

        List<ZSSDvuGDich> findAllByMaDvu(String maDvu);

        @Query("SELECT new com.ngv.zns_service.dto.response.dvu.CtDvMapGdDto(" +
                        "gd.maLoaiGdich , dc.tenLoaiGdich) " +
                        "FROM ZSSDvuGDich gd " +
                        "INNER JOIN DCLoaiGd dc ON gd.maLoaiGdich = dc.maLoaiGdich " +
                        "WHERE gd.maDvu = :mdv ")
        List<CtDvMapGdDto> listMappingGd(@Param("mdv") String mdv);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM ZSS_DVU_GDICH  ct\n" +
                        " WHERE ct.MA_DVU IN (:maDvu) AND ct.MA_LOAI_GDICH  IN (:maGdich)", nativeQuery = true)
        void deleteByMaDvuAndMaLoaiGdich(@Param("maDvu") String maDvu, @Param("maGdich") List<String> maGdich);

        @Query(value = "SELECT ct.MA_LOAI_GDICH FROM ZSS_DVU_GDICH  ct\n" +
                        " WHERE ct.MA_DVU  IN (:maDvu) AND ct.MA_LOAI_GDICH NOT IN (:maGdich)", nativeQuery = true)
        List<String> findMaGoiDvuToDelete(@Param("maGdich") List<String> maGdich, @Param("maDvu") String maDvu);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM ZSS_DVU_GDICH ct WHERE ct.MA_DVU = :maDvu", nativeQuery = true)
        void deleteByMaDvu(@Param("maDvu") String maDvu);

        @Query(value = "SELECT gd.MA_LOAI_GDICH FROM ZSS_DVU_GDICH gd WHERE gd.MA_DVU = :maDvu", nativeQuery = true)
        List<String> findByMaDvu(@Param("maDvu") String maDvu);
}
