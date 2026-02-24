package ngvgroup.com.loan.feature.interest_process.helper;

import com.ngvgroup.bpm.core.persistence.model.BaseEntitySimple;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.interest_process.dto.*;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRateFixed;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRateTier;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRateFixed;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRateTier;
import ngvgroup.com.loan.feature.interest_process.projection.FixedRateEntity;
import ngvgroup.com.loan.feature.interest_process.projection.TierRateEntity;
import ngvgroup.com.loan.feature.interest_process.repository.txn.LnmTxnIntRateFixedRepository;
import ngvgroup.com.loan.feature.interest_process.repository.txn.LnmTxnIntRateTierRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SyncHelper {

    private final LnmTxnIntRateTierRepository lnmTxnIntRateTierRepository;
    private final LnmTxnIntRateFixedRepository lnmTxnIntRateFixedRepository;

    public <E extends BaseEntitySimple> void syncEntities(
            List<E> dbList,
            List<FlatHistoryDetail> flat,
            Function<FlatHistoryDetail, Long> dtoIdGetter,
            BiConsumer<FlatHistoryDetail, E> mapper,
            Supplier<E> newEntitySupplier,
            JpaRepository<E, Long> repository
    ) {

        // ===== DB MAP =====
        Map<Long, E> dbMap =
                dbList.stream()
                        .filter(e -> e.getId() != null)
                        .collect(Collectors.toMap(
                                BaseEntitySimple::getId,
                                Function.identity()
                        ));

        // ===== DTO IDS =====
        Set<Long> dtoIds =
                flat.stream()
                        .map(dtoIdGetter)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

        // ===== DELETE =====
        List<E> toDelete =
                dbList.stream()
                        .filter(e -> e.getId() != null)
                        .filter(e -> !dtoIds.contains(e.getId()))
                        .toList();

        if (!toDelete.isEmpty()) {
            repository.deleteAll(toDelete);
        }

        // ===== INSERT / UPDATE =====
        for (FlatHistoryDetail f : flat) {

            Long dtoId = dtoIdGetter.apply(f);
            E e = null;

            if (dtoId != null) {
                e = dbMap.get(dtoId);
            }

            if (e == null) {
                e = newEntitySupplier.get(); // INSERT
            }

            mapper.accept(f, e);
            repository.save(e);
        }
    }

    public List<FlatHistoryDetail> flattenHistory(InterestProfileDTO dto) {
        if (dto.getHistory() == null) return List.of();

        return dto.getHistory().stream()
                .flatMap(h ->
                        h.getHistoryDetails().stream()
                                .map(d -> new FlatHistoryDetail(h, d))
                )
                .toList();
    }

    public void saveFixedHistory(
            InterestProfileDTO profile,
            String interestRateCode,
            String processInstanceCode
    ) {
        saveHistory(
                profile,
                LnmTxnIntRateFixed::new,
                (f, e) -> {
                    InterestHistoryDTO h = f.getParent();
                    InterestHistoryDetailDTO d = f.getDetail();

                    e.setOrgCode(profile.getOrgCode());
                    e.setInterestRateCode(interestRateCode);
                    e.setProcessInstanceCode(processInstanceCode);
                    mapFixed(h, d, e);
                },
                lnmTxnIntRateFixedRepository
        );
    }


    public void saveTierHistory(
            InterestProfileDTO profile,
            String interestRateCode,
            String processInstanceCode
    ) {
        saveHistory(
                profile,
                LnmTxnIntRateTier::new,
                (f, e) -> {
                    InterestHistoryDTO h = f.getParent();
                    InterestHistoryDetailDTO d = f.getDetail();

                    e.setOrgCode(profile.getOrgCode());
                    e.setInterestRateCode(interestRateCode);
                    e.setProcessInstanceCode(processInstanceCode);
                    mapTier(h, d, e);
                },
                lnmTxnIntRateTierRepository
        );
    }

    private <E> void saveHistory(
            InterestProfileDTO profile,
            Supplier<E> supplier,
            BiConsumer<FlatHistoryDetail, E> mapper,
            JpaRepository<E, Long> repository
    ) {
        List<E> entities =
                flattenHistory(profile).stream()
                        .map(f -> {
                            E e = supplier.get();
                            mapper.accept(f, e);
                            return e;
                        })
                        .toList();

        repository.saveAll(entities);
    }

    public void updateTier(InterestProfileDTO dto) {

        List<LnmTxnIntRateTier> dbList =
                lnmTxnIntRateTierRepository
                        .findAllByProcessInstanceCode(dto.getProcessInstanceCode());

        List<FlatHistoryDetail> flat = flattenHistory(dto);

        syncEntities(
                dbList,
                flat,
                f -> f.getDetail().getId(),
                (f, e) -> {
                    InterestHistoryDTO h = f.getParent();
                    InterestHistoryDetailDTO d = f.getDetail();

                    e.setOrgCode(dto.getOrgCode());
                    e.setInterestRateCode(dto.getInterestRateCode());
                    e.setProcessInstanceCode(dto.getProcessInstanceCode());

                    mapTier(h, d, e);
                },
                LnmTxnIntRateTier::new,
                lnmTxnIntRateTierRepository
        );
    }

    public <E extends TierRateEntity> void mapTier(
            InterestHistoryDTO h,
            InterestHistoryDetailDTO d,
            E e
    ) {
        e.setDecisionNo(h.getDecisionNo());
        e.setDecisionDate(h.getDecisionDate());
        e.setEffectiveDate(h.getEffectiveDate());

        e.setAmtFrom(d.getAmtFrom());
        e.setAmtTo(d.getAmtTo());

        mapSupProp(d, e);
    }

    public void updateFixed(InterestProfileDTO dto) {

        List<LnmTxnIntRateFixed> dbList =
                lnmTxnIntRateFixedRepository
                        .findAllByProcessInstanceCode(dto.getProcessInstanceCode());

        List<FlatHistoryDetail> flat = flattenHistory(dto);

        syncEntities(
                dbList,
                flat,
                f -> f.getDetail().getId(),
                (f, e) -> {
                    InterestHistoryDTO h = f.getParent();
                    InterestHistoryDetailDTO d = f.getDetail();

                    e.setOrgCode(dto.getOrgCode());
                    e.setInterestRateCode(dto.getInterestRateCode());
                    e.setProcessInstanceCode(dto.getProcessInstanceCode());

                    mapFixed(h, d, e);
                },
                LnmTxnIntRateFixed::new,
                lnmTxnIntRateFixedRepository
        );
    }

    public <E extends FixedRateEntity> void mapFixed(
            InterestHistoryDTO h,
            InterestHistoryDetailDTO d,
            E e
    ) {
        e.setDecisionNo(h.getDecisionNo());
        e.setDecisionDate(h.getDecisionDate());
        e.setEffectiveDate(h.getEffectiveDate());
        mapSupProp(d, e);
    }

    private <E extends FixedRateEntity> void mapSupProp(InterestHistoryDetailDTO d, E e) {
        e.setInterestRate(d.getInterestRate());
        e.setInterestRateMin(d.getInterestRateMin());
        e.setInterestRateMax(d.getInterestRateMax());
        e.setFloorInterestRate(d.getFloorInterestRate());
        e.setCapInterestRate(d.getCapInterestRate());
        e.setResetFreq(d.getResetFreq());
        e.setBaseRateCode(d.getBaseRateCode());
        e.setSpread(d.getSpread());
    }

    public <E> List<InterestHistoryDTO> buildHistory(
            List<E> list,
            Function<E, HistoryKey> keyExtractor,
            Function<E, InterestHistoryDetailDTO> detailMapper
    ) {

        Map<HistoryKey, List<E>> grouped =
                list.stream().collect(Collectors.groupingBy(keyExtractor));

        return grouped.entrySet().stream()
                .map(entry -> {
                    HistoryKey key = entry.getKey();
                    List<E> entities = entry.getValue();

                    InterestHistoryDTO history = new InterestHistoryDTO();
                    history.setDecisionNo(key.getDecisionNo());
                    history.setDecisionDate(key.getDecisionDate());
                    history.setEffectiveDate(key.getEffectiveDate());

                    history.setHistoryDetails(
                            entities.stream()
                                    .map(detailMapper)
                                    .toList()
                    );

                    return history;
                })
                .toList();
    }

    public InterestHistoryDetailDTO mapFixedToDetail(LnmTxnIntRateFixed e) {
        return new InterestHistoryDetailDTO(
                e.getId(),
                null,
                null,
                e.getInterestRate(),
                e.getInterestRateMin(),
                e.getInterestRateMax(),
                e.getFloorInterestRate(),
                e.getCapInterestRate(),
                e.getResetFreq(),
                e.getBaseRateCode(),
                e.getSpread()
        );
    }

    public InterestHistoryDetailDTO mapTierToDetail(LnmTxnIntRateTier e) {
        return new InterestHistoryDetailDTO(
                e.getId(),
                e.getAmtFrom() != null ? e.getAmtFrom() : null,
                e.getAmtTo() != null ? e.getAmtTo() : null,
                e.getInterestRate(),
                e.getInterestRateMin(),
                e.getInterestRateMax(),
                e.getFloorInterestRate(),
                e.getCapInterestRate(),
                e.getResetFreq(),
                e.getBaseRateCode(),
                e.getSpread()
        );
    }

    public List<InterestHistoryDTO> buildFixedHistory(String processInstanceCode) {

        List<LnmTxnIntRateFixed> list =
                lnmTxnIntRateFixedRepository
                        .findAllByProcessInstanceCode(processInstanceCode);

        return buildHistory(
                list,
                e -> new HistoryKey(
                        e.getDecisionNo(),
                        e.getDecisionDate(),
                        e.getEffectiveDate()
                ),
                this::mapFixedToDetail
        );
    }

    public List<InterestHistoryDTO> buildTierHistory(String processInstanceCode) {

        List<LnmTxnIntRateTier> list =
                lnmTxnIntRateTierRepository
                        .findAllByProcessInstanceCode(processInstanceCode);

        return buildHistory(
                list,
                e -> new HistoryKey(
                        e.getDecisionNo(),
                        e.getDecisionDate(),
                        e.getEffectiveDate()
                ),
                this::mapTierToDetail
        );
    }

    public InterestHistoryDetailDTO mapCfgFixedToDetail(LnmCfgIntRateFixed e) {
        return new InterestHistoryDetailDTO(
                e.getId(),
                null,
                null,
                e.getInterestRate(),
                e.getInterestRateMin(),
                e.getInterestRateMax(),
                e.getFloorInterestRate(),
                e.getCapInterestRate(),
                e.getResetFreq(),
                e.getBaseRateCode(),
                e.getSpread()
        );
    }

    public InterestHistoryDetailDTO mapCfgTierToDetail(LnmCfgIntRateTier e) {
        return new InterestHistoryDetailDTO(
                e.getId(),
                e.getAmtFrom(),
                e.getAmtTo(),
                e.getInterestRate(),
                e.getInterestRateMin(),
                e.getInterestRateMax(),
                e.getFloorInterestRate(),
                e.getCapInterestRate(),
                e.getResetFreq(),
                e.getBaseRateCode(),
                e.getSpread()
        );
    }

}
