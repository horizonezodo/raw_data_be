package ngvgroup.com.rpt.features.ctgcfgstattemplate.specification;

import jakarta.persistence.criteria.*;
import ngvgroup.com.rpt.features.common.model.ComCfgCommon;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatorySearch;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatRegulatory;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CtgCfgStatRegulatorySpecification {
    private CtgCfgStatRegulatorySpecification() {
    }

    private static final String COMMON_NAME = "commonName";

    public static Specification<CtgCfgStatRegulatory> build(CtgCfgStatRegulatorySearch request) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();
            Join<CtgCfgStatRegulatory ,ComCfgCommon> commonJoin = root.join("common", JoinType.LEFT);

            addKeywordPredicate(request, root, commonJoin, cb, predicates);
            addInPredicate(root, predicates, "reportModuleCode", request.getReportModuleCode());
            addInPredicate(root, predicates, "statTypeCode", request.getStatTypeCode());
            applySorting(request, root, commonJoin, query, cb);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addKeywordPredicate(
            CtgCfgStatRegulatorySearch request,
            Root<CtgCfgStatRegulatory> root,
            Join<CtgCfgStatRegulatory, ComCfgCommon> commonJoin,
            CriteriaBuilder cb,
            List<Predicate> predicates) {

        if (!isNotBlank(request.getKeyword())) return;

        String kw = "%" + request.getKeyword().toLowerCase() + "%";

        predicates.add(cb.or(
                cb.like(cb.lower(root.get("statRegulatoryCode")), kw),
                cb.like(cb.lower(root.get("statRegulatoryName")), kw),
                cb.like(cb.lower(root.get("statTypeCode")), kw),
                cb.like(cb.lower(root.get("reportModuleCode")), kw),
                cb.like(cb.lower(commonJoin.get(COMMON_NAME)), kw)
        ));
    }

    private static void addInPredicate(
            Root<CtgCfgStatRegulatory> root,
            List<Predicate> predicates,
            String fieldName,
            String rawValue) {

        if (!isNotBlank(rawValue)) return;

        List<String> values = List.of(rawValue.split(","));
        predicates.add(root.get(fieldName).in(values));
    }

    private static void applySorting(
            CtgCfgStatRegulatorySearch request,
            Root<CtgCfgStatRegulatory> root,
            Join<CtgCfgStatRegulatory, ComCfgCommon> commonJoin,
            CriteriaQuery<?> query,
            CriteriaBuilder cb) {

        String sortBy = isNotBlank(request.getSortBy()) ? request.getSortBy() : "modifiedDate";
        String direction = request.getSortDirection() != null
                ? request.getSortDirection().toLowerCase()
                : "desc";

        Path<?> sortPath;

        if (COMMON_NAME.equals(sortBy)) {
            sortPath = commonJoin.get(COMMON_NAME);
        } else {
            sortPath = root.get(sortBy);
        }

        if ("asc".equals(direction)) {
            query.orderBy(cb.asc(sortPath));
        } else {
            query.orderBy(cb.desc(sortPath));
        }
    }

    private static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

}
