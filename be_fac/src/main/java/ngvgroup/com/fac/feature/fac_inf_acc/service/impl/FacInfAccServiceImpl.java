package ngvgroup.com.fac.feature.fac_inf_acc.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;

import jakarta.transaction.Transactional;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.feature.common.model.ComInfSequence;
import ngvgroup.com.fac.feature.common.repository.ComInfSequenceRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccDetailDto;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccDto;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccDtoRes;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccRequest;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.GenerateAccountNoRequest;
import ngvgroup.com.fac.feature.fac_inf_acc.mapper.FacInfAccMapper;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccMapValue;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccStructureDtl;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAcc;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccA;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBal;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBalA;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacCfgAccMapValueRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacCfgAccPurposeRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacCfgAccStructureDtlRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccARepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccBalARepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccBalRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.service.FacInfAccService;

@Service
public class FacInfAccServiceImpl extends BaseServiceImpl<FacInfAcc, FacInfAccDetailDto> implements FacInfAccService {
    private final FacInfAccRepository facInfAccRepository;
    private final FacInfAccARepository accARepo;
    private final FacInfAccBalRepository balRepo;
    private final FacInfAccBalARepository balARepo;

    private final FacCfgAccStructureDtlRepository structureDtlRepo;
    private final FacCfgAccMapValueRepository mapValueRepo;
    private final FacCfgAccPurposeRepository accPurposeRepo;
    private final ComInfSequenceRepository comSeqRepo;

    private static final String ACC_STRUCTURE_CODE = "ACC_INTERNAL";
    private static final String ACC_TYPE_INTERNAL = "INTERNAL";
    private static final String TABLE_NAME_VAL = "FAC_INF_ACC";
    private static final String FIELD_NAME_VAL = "ACC_NO";
    private static final String CONSTANT_PERIOD = "NO DATE";


    protected FacInfAccServiceImpl(
            FacInfAccRepository facInfAccRepository,
            FacInfAccMapper facInfAccMapper,
            FacCfgAccStructureDtlRepository structureDtlRepo,
            FacInfAccARepository accARepo,
            FacInfAccBalRepository balRepo,
            FacInfAccBalARepository balARepo,
            FacCfgAccMapValueRepository mapValueRepo,
            FacCfgAccPurposeRepository accPurposeRepo,
            ComInfSequenceRepository comSeqRepo
    ) {
        super(facInfAccRepository, facInfAccMapper);
        this.facInfAccRepository = facInfAccRepository;
        this.structureDtlRepo = structureDtlRepo;
        this.accARepo = accARepo;
        this.balRepo = balRepo;
        this.balARepo = balARepo;
        this.mapValueRepo = mapValueRepo;
        this.accPurposeRepo = accPurposeRepo;
        this.comSeqRepo = comSeqRepo;
    }

    @Override
    public Page<FacInfAccDto> search(List<String> accScope, Pageable pageable) {
        return facInfAccRepository.search(accScope, pageable);
    }

    @Override
    @Transactional
    public void createFacInfAcc(FacInfAccRequest req) {
        GenerateAccountNoRequest genReq = GenerateAccountNoRequest.builder()
                .orgCode(req.getOrgCode())
                .domainCode(req.getDomainCode())
                .accScope(req.getAccScope())
                .currencyCode(req.getCurrencyCode())
                .build();
        req.setAccNo(generateAccountNo(genReq, true));
        validateDomainRequired(req);
        validateAccPurpose(req.getAccPurposeCode());
        FacInfAcc acc = buildFacInfAcc(req);
        syncAccBalanceFields(acc, acc.getBal());
        acc.setIntAcrPeriodAmt(BigDecimal.ZERO);
        acc.setIntAcrAmt(BigDecimal.ZERO);
        acc.setIntAcrYprevAmt(BigDecimal.ZERO);

        facInfAccRepository.save(acc);
        saveAuditAndSyncBalance(acc);
    }

    @Override
    @Transactional
    public void updateFacInfAcc(FacInfAccRequest req) {
        FacInfAcc acc = facInfAccRepository.findByAccNo(req.getAccNo()).orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND, req.getAccNo()));
        validateAccPurpose(req.getAccPurposeCode());

        acc.setObjectTypeCode(req.getObjectTypeCode());
        acc.setAccStatus(req.getAccStatus());
        acc.setAccName(req.getAccName());
        acc.setOpenDate(req.getOpenDate());
        acc.setAccPurposeCode(req.getAccPurposeCode());
        acc.setAccClassCode(req.getAccClassCode());
        acc.setAccNature(req.getAccNature());
        acc.setIsPrimaryAccount(req.getIsPrimaryAccount() != null && req.getIsPrimaryAccount() == 1 ? 1 : 0);
        BigDecimal newBal = req.getBal() == null ? BigDecimal.ZERO : req.getBal();
        acc.setBal(newBal);
        syncAccBalanceFields(acc, newBal);

        facInfAccRepository.save(acc);
        saveAuditAndSyncBalance(acc);
    }

    @Override
    @Transactional
    public String generateAccountNo(GenerateAccountNoRequest req) {
        return generateAccountNo(req, false);
    }

    private String generateAccountNo(GenerateAccountNoRequest req, boolean isUpdate) {
        List<FacCfgAccStructureDtl> segments =
                structureDtlRepo.findByAccStructureCodeOrderBySortNumber(ACC_STRUCTURE_CODE);
        Map<Long, String> segmentValues = new HashMap<>();
        StringBuilder prefixBuilder = new StringBuilder();
        FacCfgAccStructureDtl seqSegment = null;
        boolean isSeqFound = false;
        for (FacCfgAccStructureDtl seg : segments) {
            if ("SEQ".equals(seg.getSegmentType())) {
                seqSegment = seg;
                isSeqFound = true;
                continue;
            }

            String rawValue = resolveSegmentValue(seg, req);
            String paddedValue = pad(rawValue, seg.getSegmentLength());

            segmentValues.put(seg.getId(), paddedValue);
            if (!isSeqFound) {
                prefixBuilder.append(paddedValue);
            }
        }

        if (seqSegment == null) {
            throw new BusinessException(FacErrorCode.INVALID_SEGMENT_TYPE);
        }

        String calculatedPrefix = prefixBuilder.toString();
        String seqValue = generateSeq(seqSegment, req.getOrgCode(), calculatedPrefix, isUpdate);
        segmentValues.put(seqSegment.getId(), seqValue);

        List<String> finalParts = new ArrayList<>();
        for (FacCfgAccStructureDtl seg : segments) {
            finalParts.add(segmentValues.get(seg.getId()));
        }
        return String.join("", finalParts);
    }


    @Override
    public void deleteFacInfAcc(String accNo) {
        FacInfAcc acc = facInfAccRepository.findByAccNo(accNo).orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND, accNo));
        acc.setIsDelete(1);
        facInfAccRepository.save(acc);
    }

    @Override
    public List<FacInfAccDto> exportExcel() {
        return facInfAccRepository.exportExcel();
    }

    private String resolveSegmentValue(FacCfgAccStructureDtl seg, GenerateAccountNoRequest req) {
        return switch (seg.getSegmentType()) {
            case "STATIC" -> seg.getSegmentValue();
            case "MAP" -> resolveMapSegment(seg, req);
            default -> throw new BusinessException(FacErrorCode.INVALID_SEGMENT_TYPE);
        };
    }

    private void saveAuditAndSyncBalance(FacInfAcc acc) {
        accARepo.save(FacInfAccA.from(acc));

        FacInfAccBal bal = balRepo.findByAccNo(acc.getAccNo()).orElse(new FacInfAccBal());

        bal.setOrgCode(acc.getOrgCode());
        bal.setObjectTypeCode(acc.getObjectTypeCode());
        bal.setObjectCode(acc.getAccNo());
        bal.setAccNo(acc.getAccNo());
        bal.setBal(acc.getBal());
        bal.setBalAvailable(acc.getBalAvailable());
        bal.setBalActual(acc.getBalActual());
        bal.setIsDelete(0);

        if (bal.getId() == null) {
            bal.setTotalDrAmt(BigDecimal.ZERO);
            bal.setTotalCrAmt(BigDecimal.ZERO);
        }

        balRepo.save(bal);
        balARepo.save(FacInfAccBalA.fromBal(bal));
    }

    private void validateDomainRequired(FacInfAccRequest req) {
        structureDtlRepo
                .findByAccStructureCodeAndSegmentCode(ACC_STRUCTURE_CODE, "DOMAIN")
                .filter(d -> d.getIsRequired() == 1)
                .ifPresent(d -> {
                    if (!StringUtils.hasText(req.getDomainCode())) {
                        throw new BusinessException(FacErrorCode.DOMAIN_CODE_REQUIRED);
                    }
                });
    }

    private void validateAccPurpose(String code) {
        if (!accPurposeRepo.existsByAccPurposeCode(code)) {
            throw new BusinessException(FacErrorCode.ACC_PURPOSE_NOT_FOUND);
        }
    }

    private String resolveMapSegment(FacCfgAccStructureDtl seg, GenerateAccountNoRequest req) {

        return switch (seg.getSegmentCode()) {

            case "ORG" -> mapValueByOrgCode(req.getOrgCode());

            case "DOMAIN" -> {
                if (req.getDomainCode() == null)
                    yield zeros(seg.getSegmentLength());
                yield mapValue("DOMAIN_INTERNAL", req.getDomainCode());
            }

            case "ACC_SCOPE" -> mapValue("ACC_SCOPE", req.getAccScope());

            case "CCY" -> mapValue("CCY", req.getCurrencyCode());

            default -> throw new BusinessException(FacErrorCode.INVALID_MAP_SEGMENT);
        };
    }

    public String generateSeq(FacCfgAccStructureDtl seg, String orgCode, String calculatedPrefix, boolean isUpdate) {
        if (seg.getIsSeq() == null) {
            return "";
        }
        int length = seg.getSegmentLength();
        ComInfSequence seqEntry = comSeqRepo.findFirstByOrgCodeAndTableNameAndFieldNameAndPrefixAndPeriodValue(
                orgCode, TABLE_NAME_VAL, FIELD_NAME_VAL, calculatedPrefix, CONSTANT_PERIOD).orElse(null);

        long nextVal = 1L;

        if (seg.getIsSeq() == 1) {
            if (seqEntry != null) {
                nextVal = seqEntry.getCurrentSeq() + 1;
            }
        } else if (seg.getIsSeq() == 0) {
            if (seqEntry != null && seqEntry.getCurrentSeq() == 1) {
                throw new BusinessException(FacErrorCode.VALID_ACC_NOO);
            }
            nextVal = 1L;
        }

        if (seqEntry == null) {
            seqEntry = new ComInfSequence();
            seqEntry.setOrgCode(orgCode);
            seqEntry.setTableName(TABLE_NAME_VAL);
            seqEntry.setFieldName(FIELD_NAME_VAL);
            seqEntry.setPrefix(calculatedPrefix);
            seqEntry.setPeriodValue(CONSTANT_PERIOD);
        }
        seqEntry.setCurrentSeq(nextVal);
          if (isUpdate) {
            try {
                comSeqRepo.save(seqEntry);
            } catch (DataIntegrityViolationException e) {
                throw new BusinessException(FacErrorCode.VALID_ACC_NOO);
            }
        }
        return padLeft(String.valueOf(nextVal), length);
    }

    private String padLeft(String input, int length) {
        if (input.length() >= length) return input;
        return "0".repeat(length - input.length()) + input;
    }

    private String mapValue(String mapCode, String businessCode) {
        return mapValueRepo
                .findByMapCodeAndBusinessCode(mapCode, businessCode)
                .map(FacCfgAccMapValue::getMapValue)
                .orElseThrow(() -> new BusinessException(FacErrorCode.MAP_VALUE_NOT_FOUND, mapCode));
    }

    private String mapValueByOrgCode(String orgCode) {
        return mapValueRepo.findByMapValueAndOrgCode("ORG_CODE", orgCode)
                .map(FacCfgAccMapValue::getMapValue)
                .orElseThrow(() -> new BusinessException(FacErrorCode.MAP_VALUE_NOT_FOUND, "ORG_CODE"));
    }

    private FacInfAcc buildFacInfAcc(FacInfAccRequest req) {
        FacInfAcc e = new FacInfAcc();
        e.setOrgCode(req.getOrgCode());
        e.setAccNo(req.getAccNo());
        e.setAccType(ACC_TYPE_INTERNAL);
        e.setAccScope(req.getAccScope());
        e.setCurrencyCode(req.getCurrencyCode());
        e.setDomainCode(req.getDomainCode());
        e.setObjectTypeCode(req.getObjectTypeCode());
        e.setAccStatus(req.getAccStatus());
        e.setAccName(req.getAccName());
        e.setOpenDate(req.getOpenDate());
        e.setAccPurposeCode(req.getAccPurposeCode());
        e.setAccClassCode(req.getAccClassCode());
        e.setAccNature(req.getAccNature());
        e.setIsPrimaryAccount(req.getIsPrimaryAccount() == null ? 0 : req.getIsPrimaryAccount());
        e.setBal(req.getBal() == null ? BigDecimal.ZERO : req.getBal());
        return e;
    }

    private void syncAccBalanceFields(FacInfAcc acc, BigDecimal amount) {
        acc.setBalAvailable(amount);
        acc.setBalActual(amount);
    }

    private String pad(String value, int len) {
        if (StringUtils.hasText(value)) {
            return value;
        }

        if (value == null) {
            value = "";
        }
        return "0".repeat(Math.max(0, len - value.length())) + value;
    }

    private String zeros(int len) {
        return "0".repeat(len);
    }

    @Override
    public List<String> getAccNo(String accClassCode, String orgCode) {
        return facInfAccRepository.getAccNo(accClassCode, orgCode);
    }

    @Override
    public FacInfAccDtoRes getAllByAccNo(String accNo) {
        return facInfAccRepository.findAllByAccNo(accNo);
    }

    @Override
    public List<String> getAccClassCodes() {
        return facInfAccRepository.findAccClassCodesForEntry();
    }

    @Override
    public List<FacInfAccDto> getAccountsByClass(String accClassCode, String orgCode) {
        return facInfAccRepository.findAccountsByClassAndOrg(accClassCode, orgCode);
    }

    public List<FacInfAccDto> getByOrgCode(String orgCode) {
        return facInfAccRepository.getByOrgCode(orgCode);
    }

}
