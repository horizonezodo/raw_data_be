package ngvgroup.com.rpt.core.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SetOperationList;

@Slf4j
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
            log.error("Lỗi cú pháp SQL: {}", e.getMessage());
            return false;
        }
    }

    private boolean isSafeSelectBody(Object selectBody) {
        if (selectBody instanceof PlainSelect plainSelect) {

            if (plainSelect.getIntoTables() != null) {
                return false;
            }

            return plainSelect.getForUpdateTable() == null;

        } else if (selectBody instanceof SetOperationList) {
            // UNION, INTERSECT, EXCEPT bị cấm
            return false;
        }

        return false;
    }
}
