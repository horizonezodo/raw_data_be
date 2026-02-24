package vn.com.amc.qtdl.bi_proxy.utils;

import org.json.JSONObject;

import java.util.Base64;

public class JwtParser {

    private static JSONObject getPayload(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            return new JSONObject(payloadJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUserName(String token) {
        JSONObject payload = getPayload(token);
        return payload != null ? payload.optString("sub", null) : null;
    }

    public static Long getExpirationTime(String token) {
        JSONObject payload = getPayload(token);
        return payload != null ? payload.optLong("exp", -1) : null;
    }
}
