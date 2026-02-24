package ngvgroup.com.fac.feature.common.component;

import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.common.dto.SequenceDto;
import ngvgroup.com.fac.feature.common.util.SequenceUtil;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntry;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntryDtl;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherRuleN0;
import ngvgroup.com.fac.feature.fac_cfg_voucher.service.FacCfgVoucherRuleN0Service;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenerateCodeService {

    private final SequenceUtil sequenceUtil;
    private final FacCfgVoucherRuleN0Service voucherRuleN0Service;

    public String generateCode(String orgCode, String prefix, String tableName, String columnName, int start, int length, String separator) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        SequenceDto dto = new SequenceDto();
        dto.setOrgCode(orgCode);
        dto.setPrefix(prefix);
        dto.setTable(tableName);
        dto.setColumn(columnName);
        dto.setDate(currentDate);
        dto.setNumToGen(start);
        dto.setPaddedLength(length);
        dto.setSeparator(separator);
        return sequenceUtil.getNextSequence(dto);
    }

    @Transactional
    public String generateVoucherCode(String orgCode, String voucherTypeCode) {

        FacCfgVoucherRuleN0 dto = voucherRuleN0Service.findByVoucherTypeCode(voucherTypeCode);

        List<String> pattern = normalizePattern(dto.getFormatOrder());
        Map<String, Object> dataMap = buildDataMap(pattern, dto);
        String separator = dto.getSeparator();
        int lengthSeq = dto.getLengthSeq();
        String prefix = generateCodeByRule(pattern, dataMap, separator);

        return generateCode(orgCode,
                prefix,
                FacVariableConstants.FAC_CFG_VOUCHER_SEQ,
                FacVariableConstants.CURRENT_SEQ,
                1,
                lengthSeq,
                separator);
    }

    private static String extractDelimiter(String pattern) {
        Matcher matcher = Pattern.compile("\\W").matcher(pattern);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new IllegalArgumentException("Cannot detect delimiter from pattern: " + pattern);
    }

    public List<String> normalizePattern(String rawPattern) {

        String delimiter = extractDelimiter(rawPattern);

        return Arrays.stream(rawPattern.split(delimiter))
                .map(token -> FacVariableConstants.COLUMN_TO_PROPERTY.getOrDefault(token, token))
                .toList();
    }

    public Map<String, Object> buildDataMap(List<String> pattern, Object source) {

        BeanWrapper wrapper = new BeanWrapperImpl(source);
        Map<String, Object> dataMap = new HashMap<>();

        for (String token : pattern) {

            // BeanWrapper hiểu đây là property

            Object value = wrapper.getPropertyValue(token);
            // internally gọi hàm get()

            dataMap.put(token, value);
        }

        return dataMap;
    }

    public String generateCodeByRule(List<String> pattern, Map<String, Object> dataMap, String separator) {

        return pattern.stream()
                .map(t -> String.valueOf(dataMap.get(t)))
                .collect(Collectors.joining(separator));
    }


    public String generateTxnAcctEntryCode(String orgCode,String processInstanceCode){
        return generateCode(orgCode,
                processInstanceCode,
                FacTxnAcctEntry.class.getAnnotation(Table.class).name(),
                "TXN_ACCT_ENTRY_CODE",
                1,
                3,
                ".");
    }

    public String generateTxnAcctEntryDtlCode(String orgCode,String txnAcctEntryCode){
        return generateCode(orgCode,
                txnAcctEntryCode,
                FacTxnAcctEntryDtl.class.getAnnotation(Table.class).name(),
                "TXN_ACCT_ENTRY_DTL_CODE",
                1,
                4,
                ".");
    }
}
