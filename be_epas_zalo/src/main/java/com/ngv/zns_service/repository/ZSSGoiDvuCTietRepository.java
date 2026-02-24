package com.ngv.zns_service.repository;

import com.ngv.zns_service.model.entity.ZSSGoiDvuCTiet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZSSGoiDvuCTietRepository extends JpaRepository<ZSSGoiDvuCTiet, String> {
        @Query(value = """
                        SELECT zgdc.MA_DVU, zd.TEN_DVU
                        FROM ZSS_GOI_DVU_CTIET zgdc
                        JOIN ZSS_DVU zd ON zgdc.MA_DVU = zd.MA_DVU
                        WHERE zgdc.MA_DVU IN (:mdv)
                        """, nativeQuery = true)
        List<Object[]> checkExistMaDvuInGoi(@Param("mdv") List<String> mdv);

        List<ZSSGoiDvuCTiet> findAllByMaDvu(String maDvu);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM ZSS_GOI_DVU_CTIET  ct\n" +
                        " WHERE ct.MA_DVU  IN (:maDvu) AND ct.MA_GOI_DVU  IN (:maGoiDvu)", nativeQuery = true)
        // Gói dịch vụ
        void deleteByInMaGoiDvuAndInMaDvu(@Param("maGoiDvu") String maGoiDvu, @Param("maDvu") List<String> maDvu);

        @Query(value = "SELECT ct.MA_DVU FROM ZSS_GOI_DVU_CTIET  ct\n" +
                        " WHERE ct.MA_DVU NOT IN (:maDvu) AND ct.MA_GOI_DVU  IN (:maGoiDvu)", nativeQuery = true)
        List<String> findMaDvuToDelete(@Param("maGoiDvu") String maGoiDvu, @Param("maDvu") List<String> maDvu);

        @Query(value = "SELECT ct.MA_DVU FROM ZSS_GOI_DVU_CTIET ct WHERE ct.MA_GOI_DVU = :maGoiDvu", nativeQuery = true)
        List<String> findAllMaDvuByMaGoiDvu(@Param("maGoiDvu") String maGoiDvu);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM ZSS_GOI_DVU_CTIET  ct\n" +
                        " WHERE ct.MA_DVU  IN (:maDvu) AND ct.MA_GOI_DVU  IN (:maGoiDvu)", nativeQuery = true)
        // Dịch vụ
        void deleteByNotInMaGoiDvuAndInMaDvu(@Param("maGoiDvu") List<String> maGoiDvu, @Param("maDvu") String maDvu);

        @Query(value = "SELECT ct.MA_GOI_DVU FROM ZSS_GOI_DVU_CTIET  ct\n" +
                        " WHERE ct.MA_DVU  IN (:maDvu) AND ct.MA_GOI_DVU NOT IN (:maGoiDvu)", nativeQuery = true)
        List<String> findMaGoiDvuToDelete(@Param("maGoiDvu") List<String> maGoiDvu, @Param("maDvu") String maDvu);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM ZSS_GOI_DVU_CTIET ct WHERE ct.MA_DVU = :maDvu", nativeQuery = true)
        void deleteByMaDvu(@Param("maDvu") String maDvu);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM ZSS_GOI_DVU_CTIET ct WHERE ct.MA_GOI_DVU IN :ids", nativeQuery = true)
        void deleteByMaGoiDvuList(@Param("ids") List<String> ids);
}
