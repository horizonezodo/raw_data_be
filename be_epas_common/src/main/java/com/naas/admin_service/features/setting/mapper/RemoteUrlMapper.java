package com.naas.admin_service.features.setting.mapper;

import org.springframework.stereotype.Component;

import com.naas.admin_service.features.setting.dto.RemotesUrlDto;
import com.naas.admin_service.features.setting.model.RemotesUrl;

@Component
public class RemoteUrlMapper {
    public RemotesUrl toEntity(RemotesUrlDto remotesUrlDto) {
        RemotesUrl remotesUrl = new RemotesUrl();
        remotesUrl.setId(remotesUrlDto.getId());
        remotesUrl.setRemoteName(remotesUrlDto.getRemoteName());
        remotesUrl.setRemoteUrl(remotesUrlDto.getRemoteUrl());
        remotesUrl.setRemotePath(remotesUrlDto.getRemotePath());
        return remotesUrl;
    }
}
