package ngvgroup.com.rpt.features.ctgcfgstatscorerank.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreBenchmark;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreGroup;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CtgCfgStatScoreBenchmarkSpecification {
    private CtgCfgStatScoreBenchmarkSpecification() {
    }

    public static Specification<CtgCfgStatScoreBenchmark> build(String keyword) {
        return (root, query, cb) -> {
            Join<CtgCfgStatScoreGroup, CtgCfgStatScoreType> typeJoin =
                    root.join("statScoreType", JoinType.LEFT);
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && ! keyword.trim().isEmpty()) {
                String kw = "%" + keyword.trim().toLowerCase() + "%";

                Predicate nameMatch = cb.like(cb.lower(root.get("benchmarkName")), kw);
                Predicate codeMatch = cb.like(cb.lower(root.get("benchmarkCode")), kw);
                Predicate typeNameMatch = cb.like(cb.lower(typeJoin.get("statScoreTypeName")), kw);
                Predicate scoreValueMin = cb.like(cb.lower(cb.function(VariableConstants.TO_CHAR, String.class, root.get("scoreValueMin"))), kw);
                Predicate scoreValueMax = cb.like(cb.lower(cb.function(VariableConstants.TO_CHAR, String.class, root.get("scoreValueMax"))), kw);
                Predicate description = cb.like(cb.lower(root.get("description")), kw);

                predicates.add(cb.or(nameMatch,codeMatch, typeNameMatch, scoreValueMin, scoreValueMax, description));
            }
            assert query != null;
            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
