package ngvgroup.com.bpmn.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SetOperationList;

public class SQLValidatorUtils {

    public boolean isSafeSelectQuery(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);

            if (!(statement instanceof Select)) {
                return false;
            }

            Select select = (Select) statement;
            @SuppressWarnings("deprecation")
            Object selectBody = select.getSelectBody();

            return isSafeSelectBody(selectBody);

        } catch (JSQLParserException e) {
            System.out.println("Lỗi cú pháp SQL: " + e.getMessage());
            return false;
        }
    }

    private boolean isSafeSelectBody(Object selectBody) {
        if (selectBody instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) selectBody;

            if (plainSelect.getIntoTables() != null) {
                return false;
            }

            if (plainSelect.getForUpdateTable() != null) {
                return false;
            }

            return true;

        } else if (selectBody instanceof SetOperationList) {
            // UNION, INTERSECT, EXCEPT bị cấm
            return false;
        }

        return false;
    }
}
