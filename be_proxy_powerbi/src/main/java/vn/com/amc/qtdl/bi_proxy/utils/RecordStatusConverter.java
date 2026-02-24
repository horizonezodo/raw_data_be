package vn.com.amc.qtdl.bi_proxy.utils;


import vn.com.amc.qtdl.bi_proxy.enums.RecordStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RecordStatusConverter implements AttributeConverter<RecordStatus, String> {

    @Override
    public String convertToDatabaseColumn(RecordStatus attribute) {
        return attribute != null ? attribute.getCode() : RecordStatus.APPROVAL.getCode();
    }

    @Override
    public RecordStatus convertToEntityAttribute(String dbData) {
        return RecordStatus.fromCode(dbData);
    }
}
