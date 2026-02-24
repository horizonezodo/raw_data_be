package com.naas.admin_service.features.setting.service;

import java.util.List;
import java.util.Map;

import com.naas.admin_service.features.setting.dto.RemotesUrlDto;
import com.naas.admin_service.features.setting.model.RemotesUrl;

public interface RemoteUrlService {
    Map<String, String> getListRemoteUrl();

    void createRemoteUrl(RemotesUrlDto remotesUrlDto) ;

    void updateRemoteUrl(String remoteName, RemotesUrlDto remotesUrlDto) ;
    void deleteRemoteUrl(String remoteName) ;
    List<RemotesUrl> getListRemoteModule();
}
