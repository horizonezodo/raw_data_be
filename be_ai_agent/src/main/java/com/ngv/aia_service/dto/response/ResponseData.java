package com.ngv.aia_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.ResponseEntity;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {
    Integer status;
    String message;
    private T data;

    public ResponseData() {
        status = 200;
        message = "OK";
    }

    public ResponseData(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<ResponseData<T>> okEntity(T body) {
        return ResponseEntity.ok(ok(body));
    }

    public static <T> ResponseData<T> ok(T body) {
        ResponseData<T> response = new ResponseData<>();
        response.setData(body);
        return response;
    }

    public static ResponseEntity<ResponseData<Void>> noContentEntity() {
        ResponseData<Void> response = new ResponseData<>();
        response.setStatus(204);
        response.setMessage("No Content");
        return ResponseEntity.status(204).body(response);
    }

    public static ResponseEntity<ResponseData<Void>> createdEntity() {
        ResponseData<Void> response = new ResponseData<>();
        response.setStatus(201);
        response.setMessage("Created");
        return ResponseEntity.status(201).body(response);
    }

    public static ResponseEntity<ResponseData<Void>> okEntity() {
        ResponseData<Void> response = new ResponseData<>();
        response.setStatus(200);
        response.setMessage("OK");
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ResponseData<T>> notFoundEntity(String message) {
        ResponseData<T> response = new ResponseData<>();
        response.setStatus(404);
        response.setMessage(message);
        return ResponseEntity.status(404).body(response);
    }

    public static <T> ResponseEntity<ResponseData<T>> errorEntity(String message) {
        ResponseData<T> response = new ResponseData<>();
        response.setStatus(500);
        response.setMessage(message);
        return ResponseEntity.status(500).body(response);
    }

    public static <T> ResponseEntity<ResponseData<T>> badRequestEntity(String message) {
        ResponseData<T> response = new ResponseData<>();
        response.setStatus(400);
        response.setMessage(message);
        return ResponseEntity.status(400).body(response);
    }

}
