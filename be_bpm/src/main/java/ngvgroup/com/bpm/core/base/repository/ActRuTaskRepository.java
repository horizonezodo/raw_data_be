package ngvgroup.com.bpm.core.base.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class ActRuTaskRepository {
    private final JdbcTemplate jdbcTemplate;

    public Optional<ActRuTaskRow> findFirstTaskByProcInstId(String procInstId) {
        String sql = """
            SELECT ID_ as taskId, TASK_DEF_KEY_ as taskDefKey, NAME_ as taskName
            FROM ACT_RU_TASK
            WHERE PROC_INST_ID_ = ?
            ORDER BY CREATE_TIME_ ASC
        """;
        List<ActRuTaskRow> list = jdbcTemplate.query(sql, (rs, i) ->
                        new ActRuTaskRow(rs.getString("taskId"), rs.getString("taskDefKey"), rs.getString("taskName")),
                procInstId
        );
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}