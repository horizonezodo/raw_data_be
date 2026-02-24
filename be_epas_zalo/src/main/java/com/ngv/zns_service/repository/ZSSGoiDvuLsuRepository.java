package com.ngv.zns_service.repository;

import com.ngv.zns_service.model.entity.ZSSGoiDvuLsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZSSGoiDvuLsuRepository extends JpaRepository<ZSSGoiDvuLsu, String> {
}
