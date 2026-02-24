package ngvgroup.com.rpt.core.utils;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class SequenceUtil {
    @PersistenceContext
    private EntityManager entityManager;

    public String getNextSequence(String prefix, String table, String column, String date, Integer numToGen, Integer paddedLength) {

        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("GENERATE_NEXT_SEQUENCE");

        storedProcedure.registerStoredProcedureParameter("P_TABLE_NAME", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("P_COLUMN_NAME", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("P_PREFIX", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("P_NUM_TO_GEN", Integer.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("P_PADDED_LENGTH", Integer.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("P_DATE_TEXT", String.class, ParameterMode.IN);

        storedProcedure.registerStoredProcedureParameter("P_SEQUENCE_VALUE", String.class, ParameterMode.OUT);

        storedProcedure.setParameter("P_TABLE_NAME", table);
        storedProcedure.setParameter("P_COLUMN_NAME", column);
        storedProcedure.setParameter("P_PREFIX", prefix);
        storedProcedure.setParameter("P_NUM_TO_GEN", numToGen);
        storedProcedure.setParameter("P_PADDED_LENGTH", paddedLength);
        storedProcedure.setParameter("P_DATE_TEXT", date);

        storedProcedure.execute();

        return (String) storedProcedure.getOutputParameterValue("P_SEQUENCE_VALUE");
    }

    public String getNextSequence(String prefix, String table, String column, String date){
        return getNextSequence(prefix, table, column, date, 1, 5);
    }

}
