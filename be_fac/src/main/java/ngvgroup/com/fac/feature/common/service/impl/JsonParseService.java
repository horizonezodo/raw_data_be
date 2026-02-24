package ngvgroup.com.fac.feature.common.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class JsonParseService {
    private final ObjectMapper objectMapper;

    public JsonParseService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T parseJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Lỗi format JSON: " + e.getMessage());
        }
    }
}
