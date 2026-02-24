package ngv.vn.naascccd.repository;

import ngv.vn.naascccd.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {
    public Register getByUserId(String userId);
}
