package com.ngv.zns_service.repository;

import com.ngv.zns_service.model.entity.DCLoaiGd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DCLoaiGdRepository extends JpaRepository<DCLoaiGd, String> {
}
