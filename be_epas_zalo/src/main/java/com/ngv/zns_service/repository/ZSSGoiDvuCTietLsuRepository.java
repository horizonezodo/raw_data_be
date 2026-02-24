package com.ngv.zns_service.repository;

import com.ngv.zns_service.model.entity.ZSSGoiDvuCTietLsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZSSGoiDvuCTietLsuRepository extends JpaRepository<ZSSGoiDvuCTietLsu, String> {
}
