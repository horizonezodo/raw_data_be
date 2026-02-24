package com.naas.admin_service.features.permission.service.impl;

import com.naas.admin_service.features.permission.repository.ComCfgPermissionMapRepository;
import com.naas.admin_service.features.permission.service.ComCfgPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ComCfgPermissionServiceImpl implements ComCfgPermissionService {

    private final ComCfgPermissionMapRepository comCfgPermissionMapRepository;

    @Override
    public Set<String> findPermissionCodesByUserId(String userId) {
        List<String> rows = comCfgPermissionMapRepository.findDistinctPermissionCodesByUserId(userId);
        return normalize(rows);
    }

    @Override
    public Set<String> findPermissionCodesByGroupName(String groupName) {
        List<String> rows = comCfgPermissionMapRepository.findDistinctPermissionCodesByGroupName(groupName);
        return normalize(rows);
    }

    private Set<String> normalize(List<String> rows) {
        if (rows == null || rows.isEmpty()) return Set.of();
        Set<String> out = new HashSet<>();
        for (String r : rows) {
            if (r == null) continue;
            String v = r.trim();
            if (!v.isEmpty()) out.add(v);
        }
        return out;
    }
}
