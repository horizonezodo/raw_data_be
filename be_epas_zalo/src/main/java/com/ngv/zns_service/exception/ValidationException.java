package com.ngv.zns_service.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ValidationException extends Exception{
    private final Map<String, String> errors;
    public ValidationException(String key, String value) {
        this.errors = new HashMap<>();
        this.errors.put(key, value);
    }
    public ValidationException(Map<String, String> errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        return errors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
    }

}
