package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.response.DvuGroupedDTO;
import com.ngv.zns_service.dto.response.dvu.*;
import com.ngv.zns_service.dto.response.temp.TempMaMauDto;
import com.ngv.zns_service.dto.response.temp.TempTenDviDto;
import com.ngv.zns_service.model.entity.ZSSDvu;
import com.ngv.zns_service.repository.projection.ZSSDvuProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZSSDvuRepository extends JpaRepository<ZSSDvu, String> {

    @Query(value = "SELECT \n" +
            "    zd.MA_DVU ,\n" +
            "    zd.TEN_DVU ,\n" +
            "    loaiDv.TEN_DMUC_CTIET AS TEN_LOAI_DV,\n" +
            "    loaiDv.MA_DMUC_CTIET AS MA_LOAI_DV,\n" +
            "    trangThai.TEN_DMUC_CTIET AS TRANG_THAI,\n" +
            "    zd.NGAY_NHAP \n" +
            "FROM ZSS_DVU zd\n" +
            "LEFT JOIN ZSS_DMUC_CTIET loaiDv \n" +
            "    ON loaiDv.MA_DMUC_CTIET = zd.MA_LOAI_DVU \n" +
            "    AND loaiDv.MA_DMUC = 'LOAI_DVU'\n" +
            "LEFT JOIN ZSS_DMUC_CTIET trangThai \n" +
            "    ON trangThai.MA_DMUC_CTIET = zd.TTHAI_NVU \n" +
            "    AND trangThai.MA_DMUC = 'TTHAI_DVU'\n" +
            "WHERE (:loaiDv IS NULL OR zd.MA_LOAI_DVU = :loaiDv) AND (:trangThaiDv IS NULL OR zd.TTHAI_NVU = :trangThaiDv)", nativeQuery = true)
    Page<ZSSDvuProjection> getList(@Param("loaiDv") String loaiDv, @Param("trangThaiDv") String trangThaiDv, Pageable pageable);

    @Query("SELECT new com.ngv.zns_service.dto.response.dvu.ZSSDvuDto(" +
                  "dv.maDvu, dv.nguoiNhap, dv.ngayNhap, dv.nguoiSua, dv.ngaySua,dv.tenDvu,dv.maLoaiDvu," +
            "dv.soLguiTbai,dv.hthucGui,dv.tgianGui,dv.gioBdauGui,dv.gioKthucGui,dv.suKien,dv.dvuLquan,dv.mota," +
            "dv.tthaiNvu,gdvct.maGoiDvu) " +
                  "FROM ZSSDvu dv " +
                  "LEFT JOIN ZSSGoiDvuCTiet gdvct ON dv.maDvu = gdvct.maDvu " +
                  "WHERE gdvct.maGoiDvu IN :gvdct ")
    List<ZSSDvuDto> getAll(@Param("gvdct") List<String>  gvdct);

    @Modifying
    @Transactional
    @Query("DELETE FROM ZSSDvu g WHERE g.maDvu IN :ids")
    void deleteByMaDvuList(@Param("ids") List<String> ids);

    @Query("SELECT new com.ngv.zns_service.dto.response.dvu.CtDvTtchungDto(" +
            "zd.maLoaiDvu, zdc1.tenDmucCtiet , zd.tthaiNvu , zd.maDvu, zd.tenDvu, zd.dvuLquan," +
            "zd.mota, zd.hthucGui, zd.tgianGui, zd.gioBdauGui, zd.gioKthucGui, zd.soLguiTbai) " +
            "FROM ZSSDvu zd " +
            "LEFT JOIN ZSSDmucCtiet zdc1 ON zdc1.maDmucCtiet = zd.maLoaiDvu " +
            "AND zdc1.maDmucCtiet = zd.tthaiNvu " +
            "WHERE zd.maDvu = :mdv")
    CtDvTtchungDto getDetail(@Param("mdv") String mdv);

    /*
        - Hình thức gửi : HINH_THUC_GUI. Default : TUDONG
        - TGian gửi : TGIAN_GUI. Default : GUINGAY
     */
    @Query("SELECT new com.ngv.zns_service.dto.response.dvu.ZSSLoaiDvuDto(" +
            "maDmucCtiet, tenDmucCtiet) " +
            "FROM ZSSDmucCtiet " +
            "where maDmuc = :mdm " +
            "AND (:mdmct IS NULL OR maDmucCtiet = :mdmct)")
    List<ZSSLoaiDvuDto> listCommon(@Param("mdm") String mdm, @Param("mdmct") String mdmct);

    // Mapping mẫu biểu
    @Query("SELECT new com.ngv.zns_service.dto.response.dvu.CtDvMapTempDto(" +
            "zdmz.maDvi, dd.tenDvi, zdmz.zoaId, zdmz.maMau, zmz.tenMau) " +
            "FROM ZSSDvuMauZNS zdmz " +
            "INNER JOIN ZSSMauZNZ zmz ON zdmz.maMau = zmz.maMau " +
            "INNER JOIN ZssTKhoanZoa ztz ON zmz.zoaId = ztz.maZoa " +
            "INNER JOIN DCDvi dd ON ztz.maDvi = dd.maDvi " +
            "WHERE zdmz.maDvu = :mdv")
    List<CtDvMapTempDto> listMappingTemp(@Param("mdv") String mdv);

    // Tên đơn vị
    @Query("SELECT DISTINCT new com.ngv.zns_service.dto.response.temp.TempTenDviDto(" +
            "dd.maDvi, dd.tenDvi)" +
            "FROM DCDvi dd " +
            "INNER JOIN ZSSDviThueBao tb ON dd.maDvi = tb.maDvi")
    List<TempTenDviDto> listTen();

    // zoaId
    @Query("SELECT tk.maZoa " +
            "FROM ZssTKhoanZoa tk " +
            "WHERE tk.maDvi = :maDvi")
    List<String> listZoaId(@Param("maDvi") String maDvi);

    //Tên mẫu
    @Query("SELECT DISTINCT new com.ngv.zns_service.dto.response.temp.TempMaMauDto(" +
            "mznz.maMau, mznz.tenMau) " +
            "FROM ZSSMauZNZ mznz " +
            "WHERE mznz.zoaId = :zoaId")
    List<TempMaMauDto> listTenMau(@Param("zoaId") String zoaId);

    @Query("SELECT new com.ngv.zns_service.dto.response.DvuGroupedDTO(" +
            "dv.maDvu, ct.tenDmucCtiet, dv.tenDvu) " +
            "FROM ZSSDvu dv " +
            "JOIN ZSSNDung nd ON dv.maDvu = nd.maDvu " +
            "JOIN ZSSDmucCtiet ct ON ct.maDmucCtiet = dv.maLoaiDvu " )
    List<DvuGroupedDTO> getGroupedDvu();
}
