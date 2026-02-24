package com.ngv.zns_service.repository;

import com.ngv.zns_service.model.entity.ZSSMauZNSHanh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZSSMauZNSHanhRepository extends JpaRepository<ZSSMauZNSHanh, String> {

    void deleteByMaMau(String maMau);

}
