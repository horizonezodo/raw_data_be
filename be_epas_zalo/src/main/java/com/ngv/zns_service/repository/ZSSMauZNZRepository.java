package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.response.MauZNSResponse;
import com.ngv.zns_service.dto.response.ZSSMauZNZResponse;
import com.ngv.zns_service.model.entity.ZSSMauZNZ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZSSMauZNZRepository extends JpaRepository<ZSSMauZNZ, String> {
    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSMauZNZResponse( " +
            "m.maDvi, dvi.tenVtat, m.zoaId, m.maMau, m.maMauDtac, m.tenMau, " +
            "loaiNd.tenDmucCtiet, " +
            "chatLuong.tenDmucCtiet, " +
            "m.donGia, m.tthaiNvu, " +
            "trangThai.tenDmucCtiet, m.lyDo ) " +
            "FROM ZSSMauZNZ m " +
            "JOIN DCDvi dvi ON m.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSDmucCtiet loaiNd ON m.maLoaiMau = loaiNd.maDmucCtiet AND loaiNd.maDmuc = 'LOAI_ND_ZNS' " +
            "LEFT JOIN ZSSDmucCtiet chatLuong ON m.maCluongMau = chatLuong.maDmucCtiet AND chatLuong.maDmuc = 'CHAT_LUONG_MAU' " +
            "LEFT JOIN ZSSDmucCtiet trangThai ON m.tthaiZns = trangThai.maDmucCtiet AND trangThai.maDmuc = 'TTHAI_ZNS'" +
            " WHERE (:maLoaiMau IS NULL OR m.maLoaiMau = :maLoaiMau) AND " +
            "(:tthaiNvu IS NULL OR m.tthaiNvu = :tthaiNvu) AND " +
            "(:tthaiZns IS NULL OR m.tthaiZns = : tthaiZns) AND " +
            "(:maDvi IS NULL OR m.maDvi = :maDvi)")
    Page<ZSSMauZNZResponse> search(@Param("maLoaiMau") String maLoaiMau,
                                   @Param("tthaiNvu") String tthaiNvu,
                                   @Param("tthaiZns") String tthaiZns,
                                   @Param("maDvi") String maDvi,
                                   Pageable pageable);

    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSMauZNZResponse( " +
            "m.maDvi, dvi.tenVtat, m.zoaId, m.maMau, m.maMauDtac, m.tenMau, " +
            "loaiNd.tenDmucCtiet, " +
            "chatLuong.tenDmucCtiet, " +
            "m.donGia, m.tthaiNvu, " +
            "trangThai.tenDmucCtiet, m.lyDo ) " +
            "FROM ZSSMauZNZ m " +
            "JOIN DCDvi dvi ON m.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSDmucCtiet loaiNd ON m.maLoaiMau = loaiNd.maDmucCtiet AND loaiNd.maDmuc = 'LOAI_ND_ZNS' " +
            "LEFT JOIN ZSSDmucCtiet chatLuong ON m.maCluongMau = chatLuong.maDmucCtiet AND chatLuong.maDmuc = 'CHAT_LUONG_MAU' " +
            "LEFT JOIN ZSSDmucCtiet trangThai ON m.tthaiZns = trangThai.maDmucCtiet AND trangThai.maDmuc = 'TTHAI_ZNS'" +
            " WHERE (:maLoaiMau IS NULL OR m.maLoaiMau = :maLoaiMau) AND " +
            "(:tthaiNvu IS NULL OR m.tthaiNvu = :tthaiNvu) AND " +
            "(:tthaiZns IS NULL OR m.tthaiZns = : tthaiZns) AND " +
            "(:maDvi IS NULL OR m.maDvi = :maDvi)")
    List<ZSSMauZNZResponse> searchToExportExcel(@Param("maLoaiMau") String maLoaiMau,
                                                @Param("tthaiNvu") String tthaiNvu,
                                                @Param("tthaiZns") String tthaiZns,
                                                @Param("maDvi") String maDvi);


    @Query("SELECT new com.ngv.zns_service.dto.response.ZSSMauZNZResponse( " +
            "m.maDvi, dvi.tenVtat, m.zoaId, m.maMau, m.maMauDtac, m.tenMau, " +
            "loaiNd.tenDmucCtiet, " +
            "chatLuong.tenDmucCtiet, " +
            "m.donGia, m.tthaiNvu," +
            "trangThai.tenDmucCtiet, m.lyDo ) " +
            "FROM ZSSMauZNZ m " +
            "JOIN DCDvi dvi ON m.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSDmucCtiet loaiNd ON m.maLoaiMau = loaiNd.maDmucCtiet AND loaiNd.maDmuc = 'LOAI_ND_ZNS' " +
            "LEFT JOIN ZSSDmucCtiet chatLuong ON m.maCluongMau = chatLuong.maDmucCtiet AND chatLuong.maDmuc = 'CHAT_LUONG_MAU' " +
            "LEFT JOIN ZSSDmucCtiet trangThai ON m.tthaiZns = trangThai.maDmucCtiet AND trangThai.maDmuc = 'TTHAI_ZNS'" +
            "WHERE (:filter IS NULL OR " +
            "LOWER(m.maDvi) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(dvi.tenVtat) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(m.zoaId) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(m.maMau) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(m.maMauDtac) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(m.tenMau) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(m.lyDo) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(loaiNd.tenDmucCtiet) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(chatLuong.tenDmucCtiet) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(trangThai.tenDmucCtiet) LIKE LOWER(CONCAT('%', :filter, '%')))")
    Page<ZSSMauZNZResponse> searchAll(@Param("filter") String filter, Pageable pageable);

    Boolean existsByMaDvi(String maDvi);


    @Query("SELECT new com.ngv.zns_service.dto.response.MauZNSResponse( " +
            "m.maDvi, dvi.tenDvi, m.zoaId, m.maMau, m.tenMau, m.loaiZns, m.maLoaiMau, m.donGia, hanh.loaiHanh, " +
            "hanh.logoSang, hanh.tenLogoSang, hanh.logoToi, hanh.tenLogoToi, hanh.anh1, hanh.tenAnh1, hanh.anh2, hanh.tenAnh2, " +
            "hanh.anh3, hanh.tenAnh3, m.tdeMau, m.ndungTde, m.vban1, m.vban2, m.vban3, m.vban4, m.tgianCho, m.tthaiZns, " +
            "m.ghiChu, m.lket, m.maMauDtac, m.timeout) " +
            "FROM ZSSMauZNZ m " +
            "JOIN DCDvi dvi ON m.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSMauZNSHanh hanh ON m.maMau = hanh.maMau " +
            "WHERE (:maDvi IS NULL OR m.maDvi = :maDvi) AND m.maMauDtac IS NOT NULL")
    List<MauZNSResponse> syncTemplateZns(@Param("maDvi") String maDvi);

    @Query("SELECT new com.ngv.zns_service.dto.response.MauZNSResponse( " +
            "m.maDvi, dvi.tenDvi, m.zoaId, m.maMau, m.tenMau, m.loaiZns, m.maLoaiMau, m.donGia, hanh.loaiHanh, " +
            "hanh.logoSang, hanh.tenLogoSang, hanh.logoToi, hanh.tenLogoToi, hanh.anh1, hanh.tenAnh1, hanh.anh2, hanh.tenAnh2, " +
            "hanh.anh3, hanh.tenAnh3, m.tdeMau, m.ndungTde, m.vban1, m.vban2, m.vban3, m.vban4, m.tgianCho, m.tthaiZns, " +
            "m.ghiChu, m.lket, m.maMauDtac, m.timeout) " +
            "FROM ZSSMauZNZ m " +
            "JOIN DCDvi dvi ON m.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSMauZNSHanh hanh ON m.maMau = hanh.maMau " +
            "WHERE m.maMau = :maMau")
    MauZNSResponse templateZnsDetail(@Param("maMau") String maMau);

    @Query("SELECT new com.ngv.zns_service.dto.response.MauZNSResponse( " +
            "m.maDvi, dvi.tenDvi, m.zoaId, m.maMau, m.tenMau, m.loaiZns, m.maLoaiMau, m.donGia, hanh.loaiHanh, " +
            "hanh.logoSang, hanh.tenLogoSang, hanh.logoToi, hanh.tenLogoToi, hanh.anh1, hanh.tenAnh1, hanh.anh2, hanh.tenAnh2, " +
            "hanh.anh3, hanh.tenAnh3, m.tdeMau, m.ndungTde, m.vban1, m.vban2, m.vban3, m.vban4, m.tgianCho, m.tthaiZns, " +
            "m.ghiChu, m.lket, m.maMauDtac, ttoan.maNganHang, ttoan.tenTkhoan, ttoan.stk, ttoan.soTien, ttoan.noiDungCKhoan) " +
            "FROM ZSSMauZNZ m " +
            "JOIN DCDvi dvi ON m.maDvi = dvi.maDvi " +
            "LEFT JOIN ZSSMauZNSHanh hanh ON m.maMau = hanh.maMau " +
            "LEFT JOIN ZSSMauZNSTToan ttoan ON m.maMau = ttoan.maMau " +
            "WHERE m.maMau = :maMau")
    MauZNSResponse templateZnsDetailThanhToan(@Param("maMau") String maMau);

    Optional<ZSSMauZNZ> findByMaMau(String maMau);

    void deleteByMaMau(String maMau);

    Boolean existsByMaMauDtac(String maMauDtac);

    @Query("SELECT m.maMau FROM ZSSMauZNZ m WHERE m.maMauDtac = :maMauDtac")
    Optional<String> findMaMauByMaMauDtac(@Param("maMauDtac") String maMauDtac);

    @Query("SELECT z FROM ZSSMauZNZ z WHERE z.maMauDtac = :maMauDtac")
    Optional<ZSSMauZNZ> findByMaMauDtac(@Param("maMauDtac") String maMauDtac);

    @Query(value = """
                SELECT * FROM zss_mau_zns 
                WHERE zoaId = :zoaId 
                ORDER BY TO_DATE(ngay_nhap, 'DD/MM/YYYY HH24:MI') DESC 
                FETCH FIRST 1 ROWS ONLY
            """, nativeQuery = true)
    Optional<ZSSMauZNZ> findLatestByZoaId(@Param("zoaId") String zoaId);

    @Query("SELECT m.maMau FROM ZSSMauZNZ m WHERE m.maMauDtac = :maMauDtac AND m.zoaId = :oaId ")
    Optional<String> findMaMauByMaMauDtacAndZoaId(@Param("maMauDtac") String maMauDtac,
                                                  @Param("oaId") String oaId);
}
