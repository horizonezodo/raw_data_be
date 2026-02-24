package com.ngv.zns_service.repository;

import com.ngv.zns_service.dto.response.DCDviDto;
import com.ngv.zns_service.model.entity.DCDvi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DCDviRepository extends JpaRepository<DCDvi, String> {
    List<DCDvi> findAllByMaDviIn (List<String> maDvis);

    @Query("SELECT new com.ngv.zns_service.dto.response.DCDviDto(d.maDvi, d.tenDvi)" +
            "    FROM DCDvi d JOIN ZssTKhoanZoa tk ON d.maDvi = tk.maDvi")
    List<DCDviDto> listTenDvi();
    @Query("SELECT new com.ngv.zns_service.dto.response.DCDviDto(d.maDvi, d.tenDvi)" +
            "    FROM DCDvi d JOIN ZSSDviThueBao tk ON d.maDvi = tk.maDvi")
    List<DCDviDto> listTenDviDkyThueBao();

    @Query("SELECT new com.ngv.zns_service.dto.response.DCDviDto(d.maDvi, d.tenDvi) FROM DCDvi d")
    List<DCDviDto> getAllDCDviDto();
}
