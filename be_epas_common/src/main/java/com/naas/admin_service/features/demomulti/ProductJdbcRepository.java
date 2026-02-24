package com.naas.admin_service.features.demomulti;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductDto> findAll() {
        String sql = """
        SELECT "ID" AS id, "NAME" AS name
        FROM "PRODUCT"
        ORDER BY "ID"
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ProductDto(
                        rs.getObject("id", Long.class),
                        rs.getString("name")
                )
        );
    }
}
