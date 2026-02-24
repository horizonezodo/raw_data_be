package com.ngv.aia_service.intergation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {

    Optional<Option> findByPartnerCodeAndParamCode(String partnerCode, String settingName);
}
