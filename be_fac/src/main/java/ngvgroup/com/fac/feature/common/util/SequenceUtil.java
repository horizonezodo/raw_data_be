package ngvgroup.com.fac.feature.common.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import ngvgroup.com.fac.feature.common.dto.SequenceDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class SequenceUtil {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String SP_NAME = "GENERATE_NEXT_SEQUENCE";

    /**
     * Hàm gọi Stored Procedure chính (đã cập nhật thêm tham số separator)
     */
    public String getNextSequence(SequenceDto dto) {

        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery(SP_NAME);

        // 1. Đăng ký tham số (Phải khớp kiểu dữ liệu với Procedure)
        storedProcedure.registerStoredProcedureParameter("p_org_code", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_table_name", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_column_name", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_prefix", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_num_to_gen", Integer.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_padded_length", Integer.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_date_text", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_separator", String.class, ParameterMode.IN);

        storedProcedure.registerStoredProcedureParameter("p_sequence_value", String.class, ParameterMode.OUT);

        // 2. Set giá trị
        storedProcedure.setParameter("p_org_code", dto.getOrgCode());
        storedProcedure.setParameter("p_table_name", dto.getTable());
        storedProcedure.setParameter("p_column_name", dto.getColumn());
        storedProcedure.setParameter("p_prefix", dto.getPrefix());
        storedProcedure.setParameter("p_num_to_gen", dto.getNumToGen() != null ? dto.getNumToGen() : 1);
        storedProcedure.setParameter("p_padded_length", dto.getPaddedLength() != null ? dto.getPaddedLength() : 5);
        storedProcedure.setParameter("p_date_text", dto.getDate());
        storedProcedure.setParameter("p_separator", dto.getSeparator() != null ? dto.getSeparator() : "");

        // 3. Thực thi
        storedProcedure.execute();

        return (String) storedProcedure.getOutputParameterValue("p_sequence_value");
    }
}