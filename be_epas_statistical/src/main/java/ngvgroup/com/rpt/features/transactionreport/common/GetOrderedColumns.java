package ngvgroup.com.rpt.features.transactionreport.common;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class GetOrderedColumns {
    private final JdbcTemplate jdbcTemplate;

    @NotNull
    public List<String> getOrderedColumns(String tableName) {
        String owner = jdbcTemplate.queryForObject("SELECT SYS_CONTEXT('USERENV','CURRENT_SCHEMA') FROM dual", String.class);
        String[] parts = tableName.split("\\.");
        String tOwner = parts.length == 2 ? parts[0] : owner;
        String tName = parts.length == 2 ? parts[1] : parts[0];

        String sql = """
                SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS
                WHERE OWNER = ? AND TABLE_NAME = ?
                ORDER BY COLUMN_ID
                """;
        return jdbcTemplate.query(sql, ps -> {
            assert tOwner != null;
            ps.setString(1, tOwner.toUpperCase());
            ps.setString(2, tName.toUpperCase());
        }, (rs, i) -> rs.getString(1));
    }
}
