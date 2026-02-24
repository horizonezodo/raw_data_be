package ngvgroup.com.bpm.features.rule.repository;
import ngvgroup.com.bpm.features.rule.dto.InfUserDto;
import ngvgroup.com.bpm.features.rule.model.ComCfgRuleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ComCfgRuleUserRepository extends JpaRepository<ComCfgRuleUser, Long> {
    @Query("SELECT ur.userId FROM ComCfgRuleUser ur where ur.ruleCode =:ruleCode and ur.isDelete = 0")
    List<String> findUserCodeByRuleCode(@Param("ruleCode") String ruleCode);

    List<ComCfgRuleUser> findAllByRuleCode(String ruleCode);

    @Modifying
    @Query("update ComCfgRuleUser ur set ur.isDelete = 1, ur.isActive = 0 where ur.ruleCode = :ruleCode and ur.userId not in :userIds and ur.isDelete = 0")
    void softDeleteUsersNotInListUser(@Param("ruleCode") String ruleCode, @Param("userIds") List<String> userIds);

    @Modifying
    @Query("update ComCfgRuleUser ur set ur.isDelete = 0, ur.isActive = 1 where ur.ruleCode = :ruleCode and ur.userId in :userIds and ur.isDelete = 1")
    void restoreSoftDeleteUsers(@Param("ruleCode") String ruleCode, @Param("userIds") List<String> userIds);

    List<ComCfgRuleUser> findByUserIdAndOrgCodeAndIsDelete(String userId,String orgCode, int isDelete);

    boolean existsByUserId(String userId);









    @Modifying
    @Transactional
    @Query("DELETE FROM ComCfgRuleUser ur WHERE ur.userId = :userId AND ur.ruleCode = :ruleCode")
    void removeUserFromRule(String userId, String ruleCode);



    ComCfgRuleUser findByRuleCodeAndUserId(String ruleCode, String userId);

    @Modifying
    @Transactional
    void deleteByRuleCode(String ruleCode);

    boolean existsByRuleCodeAndUserId(String ruleCode,String userId);



    @Query("SELECT ur.ruleCode FROM ComCfgRuleUser ur where ur.userId =:userId and ur.isDelete = 0")
    List<String> getAllRuleCode(@Param("userId")String userId);

    List<ComCfgRuleUser> findAllByRuleCodeAndIsDelete(String ruleCode, int isDelete);

    @Query(value = "select ue.id, ue.username, ue.FIRST_NAME, ue.LAST_NAME, ue.email from EPAS_DEV_ADMIN.USER_ENTITY ue where ue.id in (:userIds)", nativeQuery = true)
    List<InfUserDto> getAllUserByUserId(List<String> userIds);
}
