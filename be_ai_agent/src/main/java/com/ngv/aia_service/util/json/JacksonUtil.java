package com.ngv.aia_service.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.aia_service.util.log.DebugLogger;

public class JacksonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final ObjectWriter prettyWriter = objectMapper.writerWithDefaultPrettyPrinter();
    
    public static ObjectNode initObjectNode() {
        return objectMapper.createObjectNode();
    }
    
    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON string", e);
        }
    }
    
    public static String toBeautifulJsonString(Object obj) {
        try {
            return prettyWriter.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to pretty JSON string", e);
        }
    }
    
    public static ArrayNode toJsonArray(String json) {
        try {
            return (ArrayNode) objectMapper.readTree(json);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static ObjectNode toJsonObject(String json) {
        try {
            return (ObjectNode) objectMapper.readTree(json);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static boolean isJsonNull(JsonNode element) {
        return element == null || element.isNull() || "null".equals(element.asText());
    }
    
    public static <T> T toJsonObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static ArrayNode toJsonArray(int[][] arr) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (int[] subArray : arr) {
            arrayNode.add(toJsonArray(subArray));
        }
        return arrayNode;
    }
    
    public static ArrayNode toJsonArray(long[][] arr) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (long[] subArray : arr) {
            arrayNode.add(toJsonArray(subArray));
        }
        return arrayNode;
    }
    
    public static ArrayNode toJsonArray(double[][] arr) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (double[] subArray : arr) {
            arrayNode.add(toJsonArray(subArray));
        }
        return arrayNode;
    }
    
    public static ArrayNode toJsonArray(int[] arr) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (int value : arr) {
            arrayNode.add(value);
        }
        return arrayNode;
    }
    
    public static ArrayNode toJsonArray(long[] arr) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (long value : arr) {
            arrayNode.add(value);
        }
        return arrayNode;
    }
    
    public static ArrayNode toJsonArray(double[] arr) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (double value : arr) {
            arrayNode.add(value);
        }
        return arrayNode;
    }
    
    public static int[] toIntArray(ArrayNode arr) {
        int[] array = new int[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            array[i] = arr.get(i).asInt();
        }
        return array;
    }
    
    public static long[] toLongArray(ArrayNode arr) {
        long[] array = new long[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            array[i] = arr.get(i).asLong();
        }
        return array;
    }
    
    public static double[] toDoubleArray(ArrayNode arr) {
        double[] array = new double[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            array[i] = arr.get(i).asDouble();
        }
        return array;
    }
    
    public static int[][] toTwoDimensionIntArray(ArrayNode arr) {
        int[][] result = new int[arr.size()][];
        for (int i = 0; i < arr.size(); i++) {
            ArrayNode subArray = (ArrayNode) arr.get(i);
            result[i] = toIntArray(subArray);
        }
        return result;
    }
}
