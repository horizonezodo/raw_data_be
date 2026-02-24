package com.naas.admin_service.features.demomulti;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductJdbcRepository repo;

    public ProductService(ProductJdbcRepository repo) {
        this.repo = repo;
    }

    public List<ProductDto> list() {
        return repo.findAll();
    }
}
