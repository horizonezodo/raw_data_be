package ngvgroup.com.loan.core.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;


@Component
@Transactional
public class SequenceUtil {

    @PersistenceContext
    private EntityManager entityManager;

    public String getNextSequence(
            String prefix,
            String table,
            String column,
            String date,
            Integer numToGen,
            Integer paddedLength
    ) {
        return entityManager.unwrap(Session.class).doReturningWork(connection -> {

            String sql = "{ call EPAS_DEV_LOAN.GENERATE_NEXT_SEQUENCE(?, ?, ?, ?, ?, ?, ?) }";

            try (CallableStatement cs = connection.prepareCall(sql)) {

                cs.setString(1, table);
                cs.setString(2, column);
                cs.setString(3, prefix);
                cs.setBigDecimal(4, BigDecimal.valueOf(numToGen != null ? numToGen : 1));
                cs.setBigDecimal(5, BigDecimal.valueOf(paddedLength != null ? paddedLength : 5));
                cs.setString(6, date);

                cs.registerOutParameter(7, Types.VARCHAR);

                cs.execute();

                return cs.getString(7);
            }
        });
    }

}