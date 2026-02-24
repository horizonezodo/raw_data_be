package com.ngv.zns_service.repository;

import com.ngv.zns_service.model.entity.ImageStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageStorageRepository extends JpaRepository<ImageStorage, String> {
}
