package ngvgroup.com.ibm.feature.dep_product.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.ibm.feature.dep_product.dto.IbmCfgDepProductDTO;
import ngvgroup.com.ibm.feature.dep_product.dto.IbmCfgDepProductDtlDTO;
import ngvgroup.com.ibm.feature.dep_product.mapper.IbmCfgDepProductDtlMapper;
import ngvgroup.com.ibm.feature.dep_product.mapper.IbmCfgDepProductMapper;
import ngvgroup.com.ibm.feature.dep_product.model.IbmCfgDepProduct;
import ngvgroup.com.ibm.feature.dep_product.model.IbmCfgDepProductDtl;
import ngvgroup.com.ibm.feature.dep_product.repository.IbmCfgDepProductDtlRepository;
import ngvgroup.com.ibm.feature.dep_product.repository.IbmCfgDepProductRepository;
import ngvgroup.com.ibm.feature.dep_product.service.IbmCfgDepProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IbmCfgDepProductServiceImpl extends BaseServiceImpl<IbmCfgDepProduct, IbmCfgDepProductDTO> implements IbmCfgDepProductService {

    private final IbmCfgDepProductRepository productRepository;
    private final IbmCfgDepProductDtlRepository productDtlRepository;
    private final IbmCfgDepProductDtlMapper productDtlMapper;

    public IbmCfgDepProductServiceImpl(IbmCfgDepProductRepository repository, IbmCfgDepProductMapper mapper,
                                       IbmCfgDepProductDtlMapper dtlMapper, IbmCfgDepProductDtlRepository dtlRepository) {
        super(repository, mapper);
        this.productRepository = repository;
        this.productDtlMapper = dtlMapper;
        this.productDtlRepository = dtlRepository;
    }

    @Override
    @Transactional
    public IbmCfgDepProductDTO create(IbmCfgDepProductDTO dto) {
        this.validateBeforeCreate(dto);
        IbmCfgDepProduct depProduct = mapper.toEntity(dto);
        applyRecordStatus(depProduct);
        repository.save(depProduct);
        List<IbmCfgDepProductDtl> depProductDtls = productDtlMapper.toListEntity(dto.getCfgDepProductDtlDTOs());
        depProductDtls.forEach(item -> {
            item.setIbmDepProductCode(depProduct.getIbmDepProductCode());
            item.setOrgCode(depProduct.getOrgCode());
            applyRecordStatus(item);
        });
        productDtlRepository.saveAll(depProductDtls);

        return mapper.toDto(depProduct);
    }

    @Override
    @Transactional
    public IbmCfgDepProductDTO update(Long id, IbmCfgDepProductDTO dto) {
        IbmCfgDepProduct entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found: " + id));
        String oldProductCode = entity.getIbmDepProductCode();

        this.validateBeforeUpdate(dto, entity);
        this.mapper.updateEntityFromDto(dto, entity);
        applyRecordStatus(entity);
        updateProductDtl(oldProductCode, entity.getIbmDepProductCode(), entity.getOrgCode(), dto.getCfgDepProductDtlDTOs());
        repository.save(entity);

        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        IbmCfgDepProduct entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found: " + id));

        repository.delete(entity);
        productDtlRepository.deleteByIbmDepProductCode(entity.getIbmDepProductCode());
    }

    @Override
    public Page<IbmCfgDepProductDTO> search(String keyword, List<String> ibmDepProductTypeCode, Pageable pageable) {
        return productRepository.search(keyword, ibmDepProductTypeCode, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public IbmCfgDepProductDTO getDetail(String ibmDepProductCode) {
        IbmCfgDepProductDTO productDto = productRepository.findByIbmDepProductCode(ibmDepProductCode)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Not found: " + ibmDepProductCode));

        List<IbmCfgDepProductDtlDTO> productDTOS = productDtlRepository.findAllDtlByIbmDepProductCode(productDto.getIbmDepProductCode());

        productDto.setCfgDepProductDtlDTOs(productDTOS);
        return productDto;
    }

    @Override
    public void validateBeforeCreate(IbmCfgDepProductDTO dto) {
        productRepository.findByIbmDepProductCode(dto.getIbmDepProductCode())
            .ifPresent(existing -> {
                throw new RuntimeException("IBM_DEP_PRODUCT_CODE already exists: " + dto.getIbmDepProductCode());
            });
    }

    @Override
    public void validateBeforeUpdate(IbmCfgDepProductDTO dto, IbmCfgDepProduct e) {
        if (dto.getIbmDepProductCode().equals(e.getIbmDepProductCode())) {
            return;
        }
        validateBeforeCreate(dto);
    }

    private void updateProductDtl(String oldProductCode, String newProductCode, String orgCode, List<IbmCfgDepProductDtlDTO> cfgDepProductDtlDTOs) {
        List<IbmCfgDepProductDtl> productDtls;

        if (Objects.isNull(cfgDepProductDtlDTOs) || cfgDepProductDtlDTOs.isEmpty()) {
            productDtlRepository.deleteByIbmDepProductCode(oldProductCode);
        } else {
            Map<Long, IbmCfgDepProductDtl> productDtlOldMap = productDtlRepository.findAllByIbmDepProductCode(oldProductCode).stream().collect(Collectors.toMap(IbmCfgDepProductDtl::getId, item -> item));

            productDtls = cfgDepProductDtlDTOs.stream().map(item -> {
                IbmCfgDepProductDtl productDtlOld = productDtlOldMap.get(item.getId());
                if (Objects.isNull(productDtlOld)) {

                    return buildProductDtl(newProductCode, orgCode, item);
                } else {
                    item.setIbmDepProductCode(newProductCode);
                    item.setOrgCode(orgCode);
                    productDtlMapper.updateEntityFromDto(item, productDtlOld);
                    applyRecordStatus(productDtlOld);
                    return productDtlOld;
                }
            }).toList();

            productDtlRepository.saveAll(productDtls);
        }
    }

    private IbmCfgDepProductDtl buildProductDtl(String ibmDepProductCode, String orgCode, IbmCfgDepProductDtlDTO dto) {
        IbmCfgDepProductDtl productDtl = productDtlMapper.toEntity(dto);
        productDtl.setIbmDepProductCode(ibmDepProductCode);
        productDtl.setOrgCode(orgCode);
        applyRecordStatus(productDtl);
        return productDtl;
    }

    private void applyRecordStatus(IbmCfgDepProduct product) {
        if (Objects.isNull(product.getRecordStatus())) {
            product.setRecordStatus("approval");
        }
    }

    private void applyRecordStatus(IbmCfgDepProductDtl productDtl) {
        if (Objects.isNull(productDtl.getRecordStatus())) {
            productDtl.setRecordStatus("approval");
        }
    }
}