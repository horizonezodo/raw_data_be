package com.naas.admin_service.features.common.service;

import com.naas.admin_service.features.common.dto.PublicImageResponse;

public interface PublicService {
    PublicImageResponse getImage(String imagePath);
}
