package com.naas.admin_service.features.setting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naas.admin_service.features.setting.model.RemotesUrl;

import java.util.Optional;

@Repository
public interface RemoteUrlRepository extends JpaRepository<RemotesUrl, Long> {

    Optional<RemotesUrl> findByRemoteName(String remoteName);
}
