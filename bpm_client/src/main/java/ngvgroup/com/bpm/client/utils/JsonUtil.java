package ngvgroup.com.bpm.client.utils;

import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.client.exception.BpmClientErrorCode;

/**
 * Utility class for JSON parsing and serialization.
 * Provides methods to convert between JSON strings and Java objects.
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private JsonUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Parses JSON string to object using TypeReference.
     *
     * @param json          JSON string to parse
     * @param typeReference TypeReference for the target type
     * @param <T>           Target type
     * @return Parsed object
     * @throws BusinessException if JSON format is invalid
     */
    public static <T> T parseJson(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BpmClientErrorCode.INVALID_JSON_FORMAT, e.getMessage());
        }
    }

    /**
     * Parses JSON string to object of specified class.
     *
     * @param json  JSON string to parse
     * @param clazz Target class
     * @param <T>   Target type
     * @return Parsed object
     * @throws BusinessException if JSON format is invalid
     */
    public static <T> T parseJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BpmClientErrorCode.INVALID_JSON_FORMAT, e.getMessage());
        }
    }

    /**
     * Converts object to JSON string.
     *
     * @param object Object to serialize
     * @return JSON string representation
     * @throws BusinessException if serialization fails
     */
    public static String toJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BpmClientErrorCode.INVALID_JSON_FORMAT, e.getMessage());
        }
    }

    /**
     * Parses JSON from InputStream to object of specified class.
     *
     * @param data        InputStream containing JSON data
     * @param targetClass Target class
     * @param <T>         Target type
     * @return Parsed object
     * @throws BusinessException if JSON format is invalid or IO error occurs
     */
    public static <T> T parseJson(InputStream data, Class<T> targetClass) {
        try {
            return mapper.readValue(data, targetClass);
        } catch (java.io.IOException e) {
            throw new BusinessException(BpmClientErrorCode.INVALID_JSON_FORMAT, e.getMessage());
        }
    }

    /**
     * Parses JSON string to object with generic type parameter.
     * Use this for generic wrapper classes like StartRequest&lt;T&gt;,
     * SubmitTaskRequest&lt;T&gt;.
     *
     * @param json         JSON string to parse
     * @param wrapperClass The wrapper class (e.g., StartRequest.class)
     * @param genericType  The generic type parameter (e.g., Map.class,
     *                     CustomerDto.class)
     * @param <T>          Target type
     * @return Parsed object
     * @throws BusinessException if JSON format is invalid
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseJsonWithGeneric(String json, Class<?> wrapperClass, Class<?> genericType) {
        try {
            var type = mapper.getTypeFactory().constructParametricType(wrapperClass, genericType);
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BpmClientErrorCode.INVALID_JSON_FORMAT, e.getMessage());
        }
    }

    /**
     * Converts a value to the given type using the internal ObjectMapper.
     * Useful for converting Maps to DTOs.
     *
     * @param fromValue   Source value
     * @param toValueType Target type class
     * @param <T>         Target type
     * @return Converted value
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        try {
            return mapper.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(BpmClientErrorCode.INVALID_JSON_FORMAT, e.getMessage());
        }
    }
}
