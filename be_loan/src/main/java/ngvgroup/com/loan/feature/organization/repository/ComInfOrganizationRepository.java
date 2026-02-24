package ngvgroup.com.loan.feature.organization.repository;

import ngvgroup.com.loan.feature.organization.model.ComInfOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComInfOrganizationRepository extends JpaRepository<ComInfOrganization, Integer> {
    ComInfOrganization findByOrgCode(String orgCode);
}
