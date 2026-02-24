package com.naas.admin_service.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public final class JwtLite {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private JwtLite() {}

    public static String getSub(String accessToken) {
        JsonNode payload = payload(accessToken);
        return payload.hasNonNull("sub") ? payload.get("sub").asText() : null;
    }

    public static Instant getExp(String accessToken) {
        JsonNode payload = payload(accessToken);
        if (payload.hasNonNull("exp")) {
            return Instant.ofEpochSecond(payload.get("exp").asLong());
        }
        return null;
    }

    private static JsonNode payload(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) throw new IllegalArgumentException("Invalid JWT");
            byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
            return MAPPER.readTree(new String(decoded, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse JWT payload", e);
        }
    }
}
