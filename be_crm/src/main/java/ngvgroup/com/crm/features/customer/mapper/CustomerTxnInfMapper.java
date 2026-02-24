package ngvgroup.com.crm.features.customer.mapper;

import org.mapstruct.*;

import ngvgroup.com.crm.features.customer.model.history.*;
import ngvgroup.com.crm.features.customer.model.inf.*;
import ngvgroup.com.crm.features.customer.model.txn.*;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CustomerTxnInfMapper {

    @Mapping(target = "id", ignore = true)
    CrmInfCust txnToInf(CrmTxnCust txn);

    @Mapping(target = "id", ignore = true)
    CrmInfCustIndv txnToInfIndv(CrmTxnCustIndv txn);

    @Mapping(target = "id", ignore = true)
    CrmInfCustCorp txnToInfCorp(CrmTxnCustCorp txn);

    @Mapping(target = "id", ignore = true)
    CrmInfCustDoc txnToInfDoc(CrmTxnCustDoc txn);

    @Mapping(target = "id", ignore = true)
    CrmInfCustAddr txnToInfAddr(CrmTxnCustAddr txn);

    @Mapping(target = "id", ignore = true)
    CrmInfCustSeg txnToInfSeg(CrmTxnCustSeg txn);

    @Mapping(target = "id", ignore = true)
    CrmInfCustReln txnToInfReln(CrmTxnCustReln txn);

    @Mapping(target = "id", ignore = true)
    CrmInfCustA infToInfH(CrmInfCust inf);

    @Mapping(target = "id", ignore = true)
    CrmInfCustIndvA infToInfIndvH(CrmInfCustIndv inf);

    @Mapping(target = "id", ignore = true)
    CrmInfCustCorpA infToInfCorpH(CrmInfCustCorp inf);

    @Mapping(target = "id", ignore = true)
    CrmInfCustDocA infToInfDocH(CrmInfCustDoc inf);

    @Mapping(target = "id", ignore = true)
    CrmInfCustAddrA infToInfAddrH(CrmInfCustAddr inf);

    @Mapping(target = "id", ignore = true)
    CrmInfCustSegA infToInfSegH(CrmInfCustSeg inf);

    @Mapping(target = "id", ignore = true)
    CrmInfCustRelnA infToInfRelnH(CrmInfCustReln inf);
}