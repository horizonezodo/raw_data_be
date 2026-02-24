package com.ngv.zns_service.repository;

import com.ngv.zns_service.model.entity.ZSSMauZNSTToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZZSMauZNSTToanRepository extends JpaRepository<ZSSMauZNSTToan, String> {

  void deleteByMaMau(String maMau);
}
