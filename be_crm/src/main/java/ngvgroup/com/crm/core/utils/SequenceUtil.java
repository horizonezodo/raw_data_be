package ngvgroup.com.crm.core.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
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
    public String getNextSequence(String prefix, String table, String column, String date, 
                                  Integer numToGen, Integer paddedLength, String separator) {

        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery(SP_NAME);

        // 1. Đăng ký tham số (Phải khớp kiểu dữ liệu với Procedure)
        storedProcedure.registerStoredProcedureParameter("p_table_name", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_column_name", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_prefix", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_num_to_gen", Integer.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_padded_length", Integer.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_date_text", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_separator", String.class, ParameterMode.IN); // Mới thêm

        storedProcedure.registerStoredProcedureParameter("p_sequence_value", String.class, ParameterMode.OUT);

        // 2. Set giá trị
        storedProcedure.setParameter("p_table_name", table);
        storedProcedure.setParameter("p_column_name", column);
        storedProcedure.setParameter("p_prefix", prefix);
        storedProcedure.setParameter("p_num_to_gen", numToGen != null ? numToGen : 1);
        storedProcedure.setParameter("p_padded_length", paddedLength != null ? paddedLength : 5);
        storedProcedure.setParameter("p_date_text", date);
        storedProcedure.setParameter("p_separator", separator != null ? separator : ""); // Mới thêm

        // 3. Thực thi
        storedProcedure.execute();

        return (String) storedProcedure.getOutputParameterValue("p_sequence_value");
    }
}