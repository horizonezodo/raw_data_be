package com.naas.admin_service.core.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StringArrayDeserializer extends JsonDeserializer<String[]> {
    @Override
    public String[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        return value == null ? new String[0] : value.split(",");
    }
}
