package com.ngvgroup.bpm.core.web.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.persistence.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseController<
        E,
        D,
        S extends BaseService<E, D>
        > {

    protected final S service;

    @PostMapping
    public ResponseEntity<ResponseData<D>> create(@RequestBody D dto) {
        return ResponseData.okEntity(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<D>> update(@PathVariable("id") Long id,
                                                  @RequestBody D dto) {
        return ResponseData.okEntity(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<String>> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseData.okEntity("Deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<D>> getDetail(@PathVariable("id") Long id) {
        return ResponseData.okEntity(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<ResponseData<List<D>>> getAll() {
        return ResponseData.okEntity(service.findAll());
    }
}
