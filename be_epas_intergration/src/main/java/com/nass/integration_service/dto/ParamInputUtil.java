package com.nass.integration_service.dto;

import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import java.sql.Types;
import java.util.List;
import com.ngvgroup.bpm.core.dto.StoredProcedureParameter;

public class ParamInputUtil {
    public static StoredProcedureParameter buildParamInputsUDTT(List<ParamInput> paramInputs) throws Exception {
        SQLServerDataTable table = new SQLServerDataTable();
        table.addColumnMetadata("PARAM_NAME", Types.VARCHAR);
        table.addColumnMetadata("PARAM_TYPE", Types.VARCHAR);
        table.addColumnMetadata("PARAM_VALUE", Types.NVARCHAR);
        if (paramInputs != null) {
            for (ParamInput p : paramInputs) {
                table.addRow(p.getParamName(), p.getParamType(), p.getParamValue());
            }
        }
        StoredProcedureParameter udttParam = new StoredProcedureParameter();
        udttParam.setName("PARAMS"); // Tên tham số trong procedure
        udttParam.setValue(table);
        udttParam.setSqlType(Types.STRUCT);
        udttParam.setOutput(false);
        // Nếu cần typeName, có thể mở rộng StoredProcedureParameter hoặc truyền qua name
        return udttParam;
    }

    public static SQLServerDataTable toSQLServerDataTable(List<ParamInput> paramInputs) throws Exception {
        SQLServerDataTable table = new SQLServerDataTable();
        table.addColumnMetadata("PARAM_NAME", Types.VARCHAR);
        table.addColumnMetadata("PARAM_TYPE", Types.VARCHAR);
        table.addColumnMetadata("PARAM_VALUE", Types.NVARCHAR);
        if (paramInputs != null) {
            for (ParamInput p : paramInputs) {
                table.addRow(p.getParamName(), p.getParamType(), p.getParamValue());
            }
        }
        return table;
    }
} 