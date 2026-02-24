package ngvgroup.com.crm.features.customer.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;
import ngvgroup.com.crm.features.customer.common.GenerateNextSequence;
import ngvgroup.com.crm.features.customer.dto.profile.*;
import ngvgroup.com.crm.features.customer.mapper.CustomerMapper;
import ngvgroup.com.crm.features.customer.mapper.CustomerTxnInfMapper;
import ngvgroup.com.crm.features.customer.model.history.*;
import ngvgroup.com.crm.features.customer.model.inf.*;
import ngvgroup.com.crm.features.customer.model.txn.*;
import ngvgroup.com.crm.features.customer.projection.CustomerProfileProjection;
import ngvgroup.com.crm.features.customer.repository.history.*;
import ngvgroup.com.crm.features.customer.repository.inf.*;
import ngvgroup.com.crm.features.customer.repository.txn.*;
import ngvgroup.com.crm.features.customer.service.CustomerTransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.crm.core.constant.CrmErrorCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerTransactionServiceImpl implements CustomerTransactionService {

    // --- Mappers & Utils ---
    private final CustomerMapper customerMapper;
    private final CustomerTxnInfMapper migrationMapper;
    @Getter
    private final GenerateNextSequence sequence;

    // --- Transaction Repositories (TXN) ---
    private final CrmTxnCustRepository txnCustRepo;
    private final CrmTxnCustIndvRepository txnIndvRepo;
    private final CrmTxnCustCorpRepository txnCorpRepo;
    private final CrmTxnCustAddrRepository txnAddrRepo;
    private final CrmTxnCustDocRepository txnDocRepo;
    private final CrmTxnCustSegRepository txnSegRepo;
    private final CrmTxnCustRelnRepository txnRelnRepo;

    // --- Info Repositories (INF) ---
    private final CrmInfCustRepository infCustRepo;
    private final CrmInfCustIndvRepository infIndvRepo;
    private final CrmInfCustCorpRepository infCorpRepo;
    private final CrmInfCustAddrRepository infAddrRepo;
    private final CrmInfCustDocRepository infDocRepo;
    private final CrmInfCustSegRepository infSegRepo;
    private final CrmInfCustRelnRepository infRelnRepo;

    // --- History Repositories (HIST) ---
    private final CrmInfCustARepository histCustRepo;
    private final CrmInfCustIndvARepository histIndvRepo;
    private final CrmInfCustCorpARepository histCorpRepo;
    private final CrmInfCustAddrARepository histAddrRepo;
    private final CrmInfCustDocARepository histDocRepo;
    private final CrmInfCustSegARepository histSegRepo;
    private final CrmInfCustRelnARepository histRelnRepo;

    // ==========================================
    // PHASE 1: TRANSACTION (CRUD)
    // ==========================================

    @Override
    @Transactional
    public void createCustomer(CustomerProfileDTO profile) {
        // 1. Save Main
        profile.getBasicInfo().setTxnDate(new Date());
        CrmTxnCust txnCust = customerMapper.toCrmTxnCust(profile.getBasicInfo(), profile.getExtendedInfo());
        txnCustRepo.save(txnCust);

        // 2. Save Children
        saveOrUpdateChildEntities(profile, txnCust.getCustomerCode(), txnCust.getProcessInstanceCode(), false);
    }

    @Override
    @Transactional
    public void updateCustomer(CustomerProfileDTO profile) {
        profile.getBasicInfo().setTxnDate(new Date());
        // 1. Get Existing
        CrmTxnCust existingTxn = txnCustRepo.findByCustomerCode(profile.getBasicInfo().getCustomerCode(),
                List.of(VariableConstants.CANCEL, VariableConstants.COMPLETE))
                .orElseThrow(() -> new BusinessException(CrmErrorCode.PROCESS_NOT_FOUND,
                        "Không tìm thấy hồ sơ: " + profile.getBasicInfo().getCustomerCode()));

        // 2. Update Main
        customerMapper.updateCust(existingTxn, profile.getBasicInfo(), profile.getExtendedInfo());
        existingTxn.setTxnDate(new Date());
        txnCustRepo.save(existingTxn);

        // 3. Update Children
        saveOrUpdateChildEntities(profile, existingTxn.getCustomerCode(), existingTxn.getProcessInstanceCode(), true);
    }

    @Override
    @Transactional
    public void updateCustomer(String processInstanceCode, CustomerProfileDTO profile) {
        profile.getBasicInfo().setTxnDate(new Date());
        // 1. Get Existing
        CrmTxnCust existingTxn = txnCustRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(CrmErrorCode.PROCESS_NOT_FOUND,
                        "Không tìm thấy hồ sơ: " + processInstanceCode));

        // 2. Update Main
        customerMapper.updateCust(existingTxn, profile.getBasicInfo(), profile.getExtendedInfo());
        existingTxn.setTxnDate(new Date());
        txnCustRepo.save(existingTxn);

        // 3. Update Children
        saveOrUpdateChildEntities(profile, existingTxn.getCustomerCode(), processInstanceCode, true);
    }

    @Override
    public CustomerProfileDTO getDetail(String processInstanceCode) {
        // Sử dụng Projection để lấy thông tin join cơ bản
        CustomerProfileProjection p = txnCustRepo.findProfileByProcessInstanceCode(processInstanceCode)
                .orElse(null);
        if (p == null)
            return null;

        return buildDtoFromProjection(p, processInstanceCode);
    }

    @Override
    @Transactional
    public void cancelRequest(String processInstanceCode) {
        CrmTxnCust txn = txnCustRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(CrmErrorCode.PROCESS_NOT_FOUND, "Process not found"));
        txn.setBusinessStatus(VariableConstants.CANCEL);
        txnCustRepo.save(txn);
    }

    // ==========================================
    // PHASE 2: MIGRATION (END PROCESS)
    // ==========================================

    @Override
    @Transactional
    public void updateEndProcess(String processInstanceCode) {
        if (processInstanceCode == null)
            return;

        // 1. Complete Transaction
        CrmTxnCust txnCust = txnCustRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(CrmErrorCode.PROCESS_NOT_FOUND, "TXN Customer not found"));

        txnCust.setBusinessStatus(VariableConstants.COMPLETE);
        txnCustRepo.save(txnCust);

        LocalDateTime now = LocalDateTime.now();
        String customerCode = txnCust.getCustomerCode();

        // 2. Migrate Main Customer
        migrateMainCustomer(txnCust, now);

        // 3. Migrate Details based on Type
        if (CrmVariableConstants.CUSTOMER_TYPE_INDIVIDUAL.equalsIgnoreCase(txnCust.getCustomerType())) {
            txnIndvRepo.findByProcessInstanceCode(processInstanceCode)
                    .ifPresent(indv -> migrateIndividual(indv, customerCode, now));

            txnDocRepo.findByProcessInstanceCode(processInstanceCode)
                    .ifPresent(doc -> migrateDocument(doc, customerCode, now));
        } else {
            txnCorpRepo.findByProcessInstanceCode(processInstanceCode)
                    .ifPresent(corp -> migrateCorporate(corp, customerCode, now));
        }

        // 4. Migrate Common Lists
        migrateAddresses(processInstanceCode, customerCode, now);

        txnSegRepo.findByProcessInstanceCode(processInstanceCode)
                .ifPresent(seg -> migrateSegment(seg, customerCode, now));

        migrateRelations(processInstanceCode, customerCode, now);
    }

    // ==========================================
    // PRIVATE HELPERS: TRANSACTION SAVING
    // ==========================================
    private void saveOrUpdateChildEntities(CustomerProfileDTO profile, String custCode, String processCode,
                                           boolean isUpdate) {
        String orgCode = profile.getBasicInfo().getOrgCode();
        Date txnDate = profile.getBasicInfo().getTxnDate();
        String type = profile.getBasicInfo().getCustomerType();

        // 1. Identity & Documents
        if (CrmVariableConstants.CUSTOMER_TYPE_INDIVIDUAL.equalsIgnoreCase(type)) {
            saveIndividualInfo(profile, custCode, processCode, orgCode, txnDate, isUpdate);
        } else {
            saveCorporateInfo(profile, custCode, processCode, orgCode, txnDate, isUpdate);
        }

        // 2. Segments
        saveSegmentInfo(profile, custCode, processCode, orgCode, txnDate, isUpdate);

        // 3. Addresses
        saveAddresses(profile, custCode, processCode, orgCode, txnDate, isUpdate);

        // 4. Relations
        saveRelations(profile, custCode, processCode, orgCode, txnDate, isUpdate);
    }

// --- Sub-methods ---

    private void saveIndividualInfo(CustomerProfileDTO profile, String custCode, String processCode,
                                    String orgCode, Date txnDate, boolean isUpdate) {
        // A. Save Individual Table
        CrmTxnCustIndv indv = isUpdate
                ? txnIndvRepo.findByProcessInstanceCode(processCode).orElse(new CrmTxnCustIndv())
                : new CrmTxnCustIndv();

        if (!isUpdate) {
            indv = customerMapper.toIndv(profile.getIdentityInfoPersonal(), profile.getExtendedInfo());
        } else {
            customerMapper.updateIndv(indv, profile.getIdentityInfoPersonal(), profile.getExtendedInfo());
        }

        indv.setCustomerCode(custCode);
        indv.setProcessInstanceCode(processCode);
        indv.setOrgCode(orgCode);
        indv.setTxnDate(txnDate);
        txnIndvRepo.save(indv);

        // B. Save Document Table (Specific to Individuals)
        CrmTxnCustDoc doc = isUpdate
                ? txnDocRepo.findByProcessInstanceCode(processCode).orElse(new CrmTxnCustDoc())
                : new CrmTxnCustDoc();

        if (!isUpdate) {
            doc = customerMapper.toDoc(profile.getIdentityInfoPersonal());
        } else {
            customerMapper.updateDoc(doc, profile.getIdentityInfoPersonal());
        }

        doc.setCustomerCode(custCode);
        doc.setProcessInstanceCode(processCode);
        doc.setOrgCode(orgCode);
        doc.setTxnDate(txnDate);
        txnDocRepo.save(doc);
    }

    private void saveCorporateInfo(CustomerProfileDTO profile, String custCode, String processCode,
                                   String orgCode, Date txnDate, boolean isUpdate) {
        CrmTxnCustCorp corp = isUpdate
                ? txnCorpRepo.findByProcessInstanceCode(processCode).orElse(new CrmTxnCustCorp())
                : new CrmTxnCustCorp();

        if (!isUpdate) {
            corp = customerMapper.toCorp(profile.getIdentityInfoCompany(), profile.getBasicInfo());
        } else {
            customerMapper.updateCorp(corp, profile.getIdentityInfoCompany(), profile.getBasicInfo());
        }

        corp.setCustomerCode(custCode);
        corp.setProcessInstanceCode(processCode);
        corp.setOrgCode(orgCode);
        corp.setTxnDate(txnDate);
        txnCorpRepo.save(corp);
    }

    private void saveSegmentInfo(CustomerProfileDTO profile, String custCode, String processCode,
                                 String orgCode, Date txnDate, boolean isUpdate) {
        if (profile.getExtendedInfo() == null) return;

        CrmTxnCustSeg seg = isUpdate
                ? txnSegRepo.findByProcessInstanceCode(processCode).orElse(new CrmTxnCustSeg())
                : new CrmTxnCustSeg();

        if (!isUpdate) {
            seg = customerMapper.toSeg(profile.getExtendedInfo());
        } else {
            customerMapper.updateSeg(seg, profile.getExtendedInfo());
        }

        seg.setCustomerCode(custCode);
        seg.setProcessInstanceCode(processCode);
        seg.setOrgCode(orgCode);
        seg.setTxnDate(txnDate);
        txnSegRepo.save(seg);
    }

    private void saveAddresses(CustomerProfileDTO profile, String custCode, String processCode,
                               String orgCode, Date txnDate, boolean isUpdate) {
        // Clear old data if update
        if (isUpdate) {
            List<CrmTxnCustAddr> oldAddrs = txnAddrRepo.findAllByProcessInstanceCode(processCode);
            if (!CollectionUtils.isEmpty(oldAddrs)) {
                txnAddrRepo.deleteAll(oldAddrs);
            }
        }

        // Insert new data
        if (!CollectionUtils.isEmpty(profile.getAddressInfo())) {
            List<CrmTxnCustAddr> newAddrs = new ArrayList<>();
            for (CustomerAddressDTO dto : profile.getAddressInfo()) {
                CrmTxnCustAddr addr = customerMapper.toAddr(dto);
                addr.setCustomerCode(custCode);
                addr.setProcessInstanceCode(processCode);
                addr.setOrgCode(orgCode);
                addr.setTxnDate(txnDate);
                newAddrs.add(addr);
            }
            txnAddrRepo.saveAll(newAddrs);
        }
    }

    private void saveRelations(CustomerProfileDTO profile, String custCode, String processCode,
                               String orgCode, Date txnDate, boolean isUpdate) {
        // Clear old data if update
        if (isUpdate) {
            List<CrmTxnCustReln> oldRelns = txnRelnRepo.findAllByProcessInstanceCode(processCode);
            if (!CollectionUtils.isEmpty(oldRelns)) {
                txnRelnRepo.deleteAll(oldRelns);
            }
        }

        // Insert new data
        if (!CollectionUtils.isEmpty(profile.getRelations())) {
            List<CrmTxnCustReln> newRelns = new ArrayList<>();
            for (CustomerRelationDTO dto : profile.getRelations()) {
                CrmTxnCustReln reln = new CrmTxnCustReln();
                reln.setCustomerCode(custCode);
                reln.setProcessInstanceCode(processCode);
                reln.setOrgCode(orgCode);
                reln.setTxnDate(txnDate);

                // Manual mapping from original code
                reln.setRelatedCustomerCode(dto.getRelatedCustomerCode());
                reln.setRelationGroupCode(dto.getRelationGroupCode());
                reln.setRelationCode(dto.getRelationCode());
                reln.setReciprocalRelationCode(dto.getReciprocalRelationCode());
                reln.setRelationStatus(dto.getRelationStatus());

                newRelns.add(reln);
            }
            txnRelnRepo.saveAll(newRelns);
        }
    }
    // ==========================================
    // PRIVATE HELPERS: MIGRATION
    // ==========================================

    private void migrateMainCustomer(CrmTxnCust txn, LocalDateTime now) {
        CrmInfCust inf = migrationMapper.txnToInf(txn);
        infCustRepo.findByCustomerCode(txn.getCustomerCode()).ifPresent(e -> inf.setId(e.getId()));
        CrmInfCust saved = infCustRepo.save(inf);

        CrmInfCustA hist = migrationMapper.infToInfH(saved);
        hist.setDataTime(now);
        histCustRepo.save(hist);
    }

    private void migrateIndividual(CrmTxnCustIndv txn, String custCode, LocalDateTime now) {
        CrmInfCustIndv inf = migrationMapper.txnToInfIndv(txn);
        infIndvRepo.findByCustomerCode(custCode).ifPresent(e -> inf.setId(e.getId()));
        CrmInfCustIndv saved = infIndvRepo.save(inf);

        CrmInfCustIndvA hist = migrationMapper.infToInfIndvH(saved);
        hist.setDataTime(now);
        histIndvRepo.save(hist);
    }

    private void migrateCorporate(CrmTxnCustCorp txn, String custCode, LocalDateTime now) {
        CrmInfCustCorp inf = migrationMapper.txnToInfCorp(txn);
        infCorpRepo.findByCustomerCode(custCode).ifPresent(e -> inf.setId(e.getId()));
        CrmInfCustCorp saved = infCorpRepo.save(inf);

        CrmInfCustCorpA hist = migrationMapper.infToInfCorpH(saved);
        hist.setDataTime(now);
        histCorpRepo.save(hist);
    }

    private void migrateDocument(CrmTxnCustDoc txn, String custCode, LocalDateTime now) {
        CrmInfCustDoc inf = migrationMapper.txnToInfDoc(txn);
        infDocRepo.findByCustomerCode(custCode).ifPresent(e -> inf.setId(e.getId()));
        CrmInfCustDoc saved = infDocRepo.save(inf);

        CrmInfCustDocA hist = migrationMapper.infToInfDocH(saved);
        hist.setDataTime(now);
        histDocRepo.save(hist);
    }

    private void migrateSegment(CrmTxnCustSeg txn, String custCode, LocalDateTime now) {
        CrmInfCustSeg inf = migrationMapper.txnToInfSeg(txn);
        infSegRepo.findByCustomerCode(custCode).ifPresent(e -> inf.setId(e.getId()));
        CrmInfCustSeg saved = infSegRepo.save(inf);

        CrmInfCustSegA hist = migrationMapper.infToInfSegH(saved);
        hist.setDataTime(now);
        histSegRepo.save(hist);
    }

    private void migrateAddresses(String processCode, String custCode, LocalDateTime now) {
        List<CrmTxnCustAddr> txnList = txnAddrRepo.findAllByProcessInstanceCode(processCode);

        // Strategy: Xóa hết Inf cũ, insert mới từ Txn (để đồng bộ hoàn toàn)
        List<CrmInfCustAddr> oldInfs = infAddrRepo.findAllByCustomerCode(custCode);
        if (!oldInfs.isEmpty())
            infAddrRepo.deleteAll(oldInfs);

        if (CollectionUtils.isEmpty(txnList))
            return;

        List<CrmInfCustAddr> newInfs = txnList.stream().map(txn -> {
            CrmInfCustAddr inf = migrationMapper.txnToInfAddr(txn);
            inf.setId(null); // Force insert
            return inf;
        }).toList();
        List<CrmInfCustAddr> savedList = infAddrRepo.saveAll(newInfs);

        // History
        List<CrmInfCustAddrA> hists = savedList.stream().map(inf -> {
            CrmInfCustAddrA h = migrationMapper.infToInfAddrH(inf);
            h.setId(null);
            h.setDataTime(now);
            return h;
        }).toList();
        histAddrRepo.saveAll(hists);
    }

    private void migrateRelations(String processCode, String custCode, LocalDateTime now) {
        List<CrmTxnCustReln> txnList = txnRelnRepo.findAllByProcessInstanceCode(processCode);

        List<CrmInfCustReln> oldInfs = infRelnRepo.findAllByCustomerCode(custCode);
        if (!oldInfs.isEmpty())
            infRelnRepo.deleteAll(oldInfs);

        if (CollectionUtils.isEmpty(txnList))
            return;

        List<CrmInfCustReln> newInfs = txnList.stream().map(txn -> {
            CrmInfCustReln inf = migrationMapper.txnToInfReln(txn);
            inf.setId(null);
            return inf;
        }).toList();
        List<CrmInfCustReln> savedList = infRelnRepo.saveAll(newInfs);

        List<CrmInfCustRelnA> hists = savedList.stream().map(inf -> {
            CrmInfCustRelnA h = migrationMapper.infToInfRelnH(inf);
            h.setId(null);
            h.setDataTime(now);
            return h;
        }).toList();
        histRelnRepo.saveAll(hists);
    }

    // ==========================================
    // PRIVATE HELPERS: DTO MAPPING
    // ==========================================

    private CustomerProfileDTO buildDtoFromProjection(CustomerProfileProjection p, String processInstanceCode) {
        CustomerGeneralInfoDTO gen = CustomerGeneralInfoDTO.builder()
                .processInstanceCode(p.getProcessInstanceCode())
                .customerCode(p.getCustomerCode())
                .customerName(p.getCustomerName())
                .customerType(p.getCustomerType())
                .orgCode(p.getOrgCode())
                .areaCode(p.getAreaCode())
                .mobileNumber(p.getMobileNumber())
                .phoneNumber(p.getPhoneNumber())
                .email(p.getEmail())
                .taxCode(p.getTaxCode())
                .fax(p.getFax())
                .economicTypeCode(p.getEconomicTypeCode())
                .industryCode(p.getIndustryCode())
                .isInsurance(p.getIsInsurance())
                .txnDate(p.getTxnDate())
                .build();

        CustomerExtensionInfoDTO ext = CustomerExtensionInfoDTO.builder()
                .poorHouseholdBookNo(p.getPoorHouseholdBookNo())
                .isPoorHousehold(p.getIsPoorHousehold())
                .segmentType(p.getSegmentType())
                .segmentCode(p.getSegmentCode())
                .segmentRankCode(p.getSegmentRankCode())
                .profession(p.getProfession())
                .jobTitle(p.getJobTitle())
                .workTimeValue(p.getWorkTimeValue() != null ? Integer.valueOf(p.getWorkTimeValue()) : null)
                .workTimeUnit(p.getWorkTimeUnit())
                .contractType(p.getContractType())
                .workplace(p.getWorkplace())
                .workAddress(p.getWorkAddress())
                .description(p.getDescription())
                .build();

        CustomerIndividualInfoDTO indv = null;
        CustomerCorporateInfoDTO corp = null;

        if (CrmVariableConstants.CUSTOMER_TYPE_INDIVIDUAL.equalsIgnoreCase(p.getCustomerType())) {
            indv = CustomerIndividualInfoDTO.builder()
                    .genderCode(p.getGenderCode())
                    .dateOfBirth(p.getDateOfBirth())
                    .placeOfBirth(p.getPlaceOfBirth())
                    .ethnicityCode(p.getEthnicityCode())
                    .maritalStatus(p.getMaritalStatus())
                    .professionTypeCode(p.getProfessionTypeCode())
                    .eduLevelCode(p.getEduLevelCode())
                    .eduBackgroundCode(p.getEduBackgroundCode())
                    .identificationType(p.getIdentificationType())
                    .identificationId(p.getIdentificationId())
                    .identificationIdOld(p.getIdentificationIdOld())
                    .issueDate(p.getIssueDate())
                    .expiryDate(p.getExpiryDate())
                    .issuePlace(p.getIssuePlace())
                    .build();
        } else {
            corp = CustomerCorporateInfoDTO.builder()
                    .corpShortName(p.getCorpShortName())
                    .businessLicenseNo(p.getBusinessLicenseNo())
                    .businessLicenseDate(p.getBusinessLicenseDate())
                    .issuedBy(p.getIssuedBy())
                    .establishedDate(p.getEstablishedDate())
                    .website(p.getWebsite())
                    .legalRepName(p.getLegalRepName())
                    .legalRepTitle(p.getLegalRepTitle())
                    .legalRepIdNo(p.getLegalRepIdNo())
                    .industryDetail(p.getIndustryDetail())
                    .build();
        }

        List<CrmTxnCustAddr> addrs = txnAddrRepo.findAllByProcessInstanceCode(processInstanceCode);
        List<CustomerAddressDTO> addrDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(addrs)) {
            addrDtos = addrs.stream().map(a -> CustomerAddressDTO.builder()
                    .provinceCode(a.getProvinceCode())
                    .wardCode(a.getWardCode())
                    .address(a.getAddress())
                    .isPrimary(a.getIsPrimary())
                    .countryCode(a.getCountryCode())
                    .build()).toList();
        }

        List<CustomerRelationDTO> relnDtos = txnRelnRepo.findRelationsByCustomerCode(p.getCustomerCode(),
                processInstanceCode);

        return CustomerProfileDTO.builder()
                .basicInfo(gen)
                .extendedInfo(ext)
                .identityInfoPersonal(indv)
                .identityInfoCompany(corp)
                .addressInfo(addrDtos)
                .relations(relnDtos)
                .build();
    }
}