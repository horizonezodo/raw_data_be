package com.naas.admin_service.features.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naas.admin_service.features.category.model.HrmInfEmployee;

@Repository
public interface HrmInfEmployeeRepository extends JpaRepository<HrmInfEmployee,Long> {
}
