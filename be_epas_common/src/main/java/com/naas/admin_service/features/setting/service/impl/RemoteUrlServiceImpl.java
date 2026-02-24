package com.naas.admin_service.features.setting.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.setting.dto.RemotesUrlDto;
import com.naas.admin_service.features.setting.mapper.RemoteUrlMapper;
import com.naas.admin_service.features.setting.model.RemotesUrl;
import com.naas.admin_service.features.setting.repository.RemoteUrlRepository;
import com.naas.admin_service.features.setting.service.RemoteUrlService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemoteUrlServiceImpl implements RemoteUrlService {
    private final RemoteUrlRepository remoteUrlRepository;
    private final RemoteUrlMapper remoteUrlMapper;


    @Override
    public Map<String, String> getListRemoteUrl() {
        return remoteUrlRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        RemotesUrl::getRemoteName,
                        RemotesUrl::getRemoteUrl
                ));
    }

    @Override
    public List<RemotesUrl> getListRemoteModule() {
        return remoteUrlRepository.findAll();
    }

    @Override
    @Transactional
    public void createRemoteUrl(RemotesUrlDto remotesUrlDto) {
        RemotesUrl remotesUrl = remoteUrlMapper.toEntity(remotesUrlDto);
        this.checkRemoteNameExist(remotesUrl.getRemoteName());
        this.checkRemoteUrlExist(remotesUrl.getRemoteUrl());
        this.checkRemotePathExist(remotesUrl.getRemotePath());
        remoteUrlRepository.save(remotesUrl);
    }

    @Override
    @Transactional
    public void updateRemoteUrl(String remoteName, RemotesUrlDto remotesUrlDto) {
        RemotesUrl remotesUrl = remoteUrlMapper.toEntity(remotesUrlDto);
        this.checkRemoteNameNotFound(remoteName);
        this.checkRemoteNameExist(remotesUrl.getRemoteName());
        this.checkRemoteUrlExist(remotesUrl.getRemoteUrl());
        this.checkRemotePathExist(remotesUrl.getRemotePath());
        Optional<RemotesUrl> remotesUrlOptional = remoteUrlRepository.findByRemoteName(remoteName);
        if (remotesUrlOptional.isPresent()) {
            RemotesUrl remote = remotesUrlOptional.get();
            remote.setRemoteName(remotesUrl.getRemoteName());
            remote.setRemotePath(remotesUrl.getRemotePath());
            remote.setRemoteUrl(remotesUrl.getRemoteUrl());
            remoteUrlRepository.save(remote);
        }

    }

    @Override
    @Transactional
    public void deleteRemoteUrl(String remoteName) {
        this.checkRemoteNameNotFound(remoteName);
        Optional<RemotesUrl> remotesUrlOptional = remoteUrlRepository.findByRemoteName(remoteName);
        if (remotesUrlOptional.isPresent()) {
            RemotesUrl remote = remotesUrlOptional.get();
            remoteUrlRepository.deleteById(remote.getId());
        }

    }

    private void checkRemoteNameExist(String remoteName) {
        List<RemotesUrl> remotesUrlList = remoteUrlRepository.findAll();
        List<RemotesUrl> filteredList = remotesUrlList.stream()
                .filter(remotesUrl -> !remoteName.equals(remotesUrl.getRemoteName()))
                .toList();
        boolean remoteNameExist = filteredList.stream()
                .anyMatch(remote -> remote.getRemoteName().equals(remoteName));
        if (remoteNameExist) {
            throw new BusinessException(CommonErrorCode.EXISTS,remoteName);
        }
    }

    private void checkRemoteNameNotFound(String remoteName) {
        List<RemotesUrl> remotesUrlList = remoteUrlRepository.findAll();
        boolean remoteNameExist = remotesUrlList.stream()
                .anyMatch(remote -> remote.getRemoteName().equals(remoteName));
        if (!remoteNameExist) {
            throw new BusinessException(ErrorCode.NOT_FOUND, remoteName);
        }
    }

    private void checkRemoteUrlExist(String remoteUrl) {
        List<RemotesUrl> remotesUrlList = remoteUrlRepository.findAll();
        List<RemotesUrl> filteredList = remotesUrlList.stream()
                .filter(remotesUrl -> !remoteUrl.equals(remotesUrl.getRemoteUrl()))
                .toList();
        boolean remoteUrlExist = filteredList.stream()
                .anyMatch(remote -> remote.getRemoteUrl().equals(remoteUrl));
        if (remoteUrlExist) {
            throw new BusinessException(CommonErrorCode.EXISTS,remoteUrl);
        }
    }

    private void checkRemotePathExist(String remotePath) {
        List<RemotesUrl> remotesUrlList = remoteUrlRepository.findAll();
        List<RemotesUrl> filteredList = remotesUrlList.stream()
                .filter(remote-> !remotePath.equals(remote.getRemotePath()))
                .toList();
        boolean remoteUrlExist = filteredList.stream()
                .anyMatch(remote -> remote.getRemotePath().equals(remotePath));
        if (remoteUrlExist) {
            throw new BusinessException(CommonErrorCode.EXISTS,remotePath);
        }
    }


}

