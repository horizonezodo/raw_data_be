package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.ibm.core.constant.ActiveStatus;
import ngvgroup.com.ibm.core.constant.IbmErrorCode;
import ngvgroup.com.ibm.feature.common.service.CtgComCommonService;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto.IbmCfgDepIntRateDTO;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto.IbmCfgDepIntRateDtlDtTO;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto.IbmCfgDepIntRateSearch;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.mapper.IbmCfgDepIntRateDtlMapper;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.mapper.IbmCfgDepIntRateMapper;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.model.IbmCfgDepIntRate;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.model.IbmCfgDepIntRateDtl;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.repository.IbmCfgDepIntRateDtlRepository;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.repository.IbmCfgDepIntRateRepository;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.service.IbmCfgDepIntRateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static ngvgroup.com.ibm.core.constant.CommonTypeCode.INTEREST_RATE_TYPE;

@Service
public class IbmCfgDepIntRateServiceImpl extends BaseServiceImpl<IbmCfgDepIntRate, IbmCfgDepIntRateDTO>
        implements IbmCfgDepIntRateService {
    private final IbmCfgDepIntRateRepository ibmCfgDepIntRateRepository;
    private final IbmCfgDepIntRateDtlRepository ibmCfgDepIntRateDtlRepository;
    private final IbmCfgDepIntRateMapper ibmCfgDepIntRateMapper;
    private final IbmCfgDepIntRateDtlMapper ibmCfgDepIntRateDtlMapper;
    private final CtgComCommonService ctgComCommonService;

    protected IbmCfgDepIntRateServiceImpl(IbmCfgDepIntRateRepository repository, IbmCfgDepIntRateMapper mapper,
                                          IbmCfgDepIntRateDtlRepository dtlRepository, IbmCfgDepIntRateDtlMapper dtlMapper,
                                          CtgComCommonService comCommonService) {
        super(repository, mapper);
        ibmCfgDepIntRateRepository = repository;
        ibmCfgDepIntRateDtlRepository = dtlRepository;
        ibmCfgDepIntRateMapper = mapper;
        ibmCfgDepIntRateDtlMapper = dtlMapper;
        ctgComCommonService = comCommonService;
    }

    @Override
    protected void validateBeforeCreate(IbmCfgDepIntRateDTO dto) {
        String interestRateCode = dto.getInterestRateCode();
        boolean isExist = ibmCfgDepIntRateRepository.existsByInterestRateCode(interestRateCode);
        if (isExist) {
            throw new BusinessException(IbmErrorCode.DUPLICATE_INTEREST_RATE_CODE, interestRateCode);
        }
    }


    @Override
    protected void validateBeforeUpdate(IbmCfgDepIntRateDTO dto, IbmCfgDepIntRate entity) {
        if (dto.getInterestRateCode().equals(entity.getInterestRateCode())) {
            return;
        }
        validateBeforeCreate(dto);
    }
    @Override
    protected void beforeSaveCreate(IbmCfgDepIntRate entity, IbmCfgDepIntRateDTO dto) {
        String interestRateCode = entity.getInterestRateCode();
        List<IbmCfgDepIntRateDtlDtTO> ibmCfgDepIntRateDtlDTOs = dto.getIbmCfgDepIntRateDtls();
        if (ibmCfgDepIntRateDtlDTOs.isEmpty()) {
            return;
        }

        List<IbmCfgDepIntRateDtl> ibmCfgDepIntRateDtls = ibmCfgDepIntRateDtlDTOs.stream()
                .map(item -> {
                    IbmCfgDepIntRateDtl ibmCfgDepIntRateDtl = ibmCfgDepIntRateDtlMapper.toEntity(item);
                    ibmCfgDepIntRateDtl.setOrgCode(entity.getOrgCode());
                    ibmCfgDepIntRateDtl.setInterestRateCode(interestRateCode);
                    ibmCfgDepIntRateDtl.setIsActive(entity.getIsActive());
                    return ibmCfgDepIntRateDtl;
                }).toList();
        ibmCfgDepIntRateDtlRepository.saveAll(ibmCfgDepIntRateDtls);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        IbmCfgDepIntRate entity = ibmCfgDepIntRateRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));

        ibmCfgDepIntRateRepository.delete(entity);
        ibmCfgDepIntRateDtlRepository.deleteByInterestRateCode(entity.getInterestRateCode());
    }

    @Override
    @Transactional
    public IbmCfgDepIntRateDTO update(Long id, IbmCfgDepIntRateDTO dto) {
        IbmCfgDepIntRate entity = ibmCfgDepIntRateRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));
        validateBeforeUpdate(dto, entity);
        ibmCfgDepIntRateMapper.updateEntityFromDto(dto, entity);

        updateIbmCfgDepIntRateDtl(dto.getIbmCfgDepIntRateDtls(), entity.getInterestRateCode(), entity.getOrgCode(), dto.getIsActive());
        return ibmCfgDepIntRateMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public IbmCfgDepIntRateDTO findById(Long id) {
        IbmCfgDepIntRate entity = ibmCfgDepIntRateRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));

        IbmCfgDepIntRateDTO dto = ibmCfgDepIntRateMapper.toDto(entity);
        List<IbmCfgDepIntRateDtlDtTO> dtls = ibmCfgDepIntRateDtlRepository
                .findByInterestRateCode(entity.getInterestRateCode())
                .stream()
                .map(ibmCfgDepIntRateDtlMapper::toDto)
                .toList();

        dto.setIbmCfgDepIntRateDtls(dtls);

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IbmCfgDepIntRateSearch> searchCfgDepIntRate() {
        Map<String, String> commonMap = ctgComCommonService.findByCommonTypeCodes(List.of(INTEREST_RATE_TYPE));

        return ibmCfgDepIntRateRepository.findAllByOrderByModifiedDateDesc()
                .stream()
                .map(item -> {
                    String interestRateType = commonMap.get(item.getInterestRateType());
                    if (Objects.nonNull(interestRateType)) {
                        item.setInterestRateType(interestRateType);
                    }
                    return ibmCfgDepIntRateMapper.toSearch(item);
                })
                .toList();
    }

    private void updateIbmCfgDepIntRateDtl(List<IbmCfgDepIntRateDtlDtTO> ibmCfgDepIntRateDtlDtTOs,
                                           String interestRateCode, String orgCode, Integer isActiveDepIntRate) {

        if (Objects.isNull(ibmCfgDepIntRateDtlDtTOs)) {
            return;
        }

        Map<Long, IbmCfgDepIntRateDtl> ibmCfgDepIntRateDtlMap = null;
        List<IbmCfgDepIntRateDtl> ibmCfgDepIntRateDtlsUpdate;
        List<Long> depIntRateDtlIds = ibmCfgDepIntRateDtlDtTOs.stream()
                .map(IbmCfgDepIntRateDtlDtTO::getId)
                .filter(Objects::nonNull).toList();


        if (!depIntRateDtlIds.isEmpty()) {
            List<IbmCfgDepIntRateDtl> ibmCfgDepIntRateDtls = ibmCfgDepIntRateDtlRepository
                    .findByIdIn(depIntRateDtlIds);

            ibmCfgDepIntRateDtlMap = ibmCfgDepIntRateDtls.stream()
                    .collect(Collectors.toMap(IbmCfgDepIntRateDtl::getId, item -> item));

            ibmCfgDepIntRateDtlsUpdate = buildListIbmCfgDepIntRateDtl(ibmCfgDepIntRateDtlDtTOs,
                    ibmCfgDepIntRateDtlMap, interestRateCode, orgCode, isActiveDepIntRate);

            ibmCfgDepIntRateDtlRepository.deleteByInterestRateCodeAndIdNotIn(interestRateCode, depIntRateDtlIds);
        } else {
            ibmCfgDepIntRateDtlsUpdate = buildListIbmCfgDepIntRateDtl(ibmCfgDepIntRateDtlDtTOs,
                    ibmCfgDepIntRateDtlMap, interestRateCode, orgCode, isActiveDepIntRate);
            ibmCfgDepIntRateDtlRepository.deleteByInterestRateCode(interestRateCode);

        }

        ibmCfgDepIntRateDtlRepository.saveAll(ibmCfgDepIntRateDtlsUpdate);
    }

    private List<IbmCfgDepIntRateDtl> buildListIbmCfgDepIntRateDtl(List<IbmCfgDepIntRateDtlDtTO> dtos, Map<Long, IbmCfgDepIntRateDtl> entityMap,
                                                                   String interestRateCode, String orgCode, Integer isActiveDepIntRate) {
        if (Objects.isNull(entityMap)) {
            return dtos.stream().map(item -> {
                IbmCfgDepIntRateDtl newItem = ibmCfgDepIntRateDtlMapper.toEntity(item);
                newItem.setInterestRateCode(interestRateCode);
                newItem.setOrgCode(orgCode);
                newItem.setIsActive(isActiveDepIntRate);
                return newItem;
            }).toList();
        }

        return dtos.stream().map(item -> {
            IbmCfgDepIntRateDtl updateItem = entityMap.get(item.getId());
             if (Objects.nonNull(updateItem)) {
                 ibmCfgDepIntRateDtlMapper.updateEntityFromDto(item, updateItem);
                 updateItem.setOrgCode(orgCode);
                 updateItem.setIsActive(isActiveDepIntRate);
                 return updateItem;
             }
            IbmCfgDepIntRateDtl newItem = ibmCfgDepIntRateDtlMapper.toEntity(item);
            newItem.setOrgCode(orgCode);
            newItem.setIsActive(isActiveDepIntRate);
            newItem.setInterestRateCode(interestRateCode);
            return newItem;
        }).toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<IbmCfgDepIntRateSearch> exportExcel() {
        Map<String, String> commonMap = ctgComCommonService.findByCommonTypeCodes(List.of(INTEREST_RATE_TYPE));

        return ibmCfgDepIntRateRepository.findAllByOrderByModifiedDateDesc()
                .stream()
                .map(item -> {
                    String interestRateType = commonMap.get(item.getInterestRateType());
                    if (Objects.nonNull(interestRateType)) {
                        item.setInterestRateType(interestRateType);
                    }
                    IbmCfgDepIntRateSearch intRateSearch = ibmCfgDepIntRateMapper.toSearch(item);
                    if (ActiveStatus.ACTIVE.getValue() == intRateSearch.getIsActive()) {
                        intRateSearch.setActive("Hiệu lực");
                    } else {
                        intRateSearch.setActive("Hết hiệu lực");
                    }
                    return intRateSearch;
                })
                .toList();
    }

}
