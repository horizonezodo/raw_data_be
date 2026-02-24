package ngvgroup.com.rpt.features.ctgcfgstatscorerank.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupSearch;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreGroup;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CtgCfgStatScoreGroupSpecification {
    private CtgCfgStatScoreGroupSpecification() {
    }

    public static Specification<CtgCfgStatScoreGroup> build(StatScoreGroupSearch req) {
        return (root, query, cb) -> {
            Join<CtgCfgStatScoreGroup, CtgCfgStatScoreType> typeJoin =
                    root.join("statScoreType", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (req.getKeyword() != null && !req.getKeyword().trim().isEmpty()) {
                String kw = "%" + req.getKeyword().trim().toLowerCase() + "%";

                Predicate nameMatch = cb.like(cb.lower(root.get("statScoreGroupName")), kw);
                Predicate typeNameMatch = cb.like(cb.lower(typeJoin.get("statScoreTypeName")), kw);
                Predicate groupCode = cb.like(cb.lower(root.get("statScoreGroupCode")), kw);
                Predicate description = cb.like(cb.lower(root.get("description")), kw);

                predicates.add(cb.or(nameMatch, typeNameMatch, groupCode, description));
            }

            String sortBy = (req.getSortBy() != null && !req.getSortBy().isEmpty())
                    ? req.getSortBy()
                    : "modifiedDate";
            String sortDirection = (req.getSortDirection() != null)
                    ? req.getSortDirection().toLowerCase()
                    : "desc";

            if ("asc".equals(sortDirection)) {
                assert query != null;
                query.orderBy(cb.asc(root.get(sortBy)));
            } else {
                assert query != null;
                query.orderBy(cb.desc(root.get(sortBy)));
            }

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
