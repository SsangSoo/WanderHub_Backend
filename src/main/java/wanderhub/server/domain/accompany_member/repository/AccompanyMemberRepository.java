package wanderhub.server.domain.accompany_member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;

import java.util.List;

@Repository
public interface AccompanyMemberRepository extends JpaRepository<AccompanyMember, Long> {

    @Query("select am from AccompanyMember as am where am.accompany.id = :accompanyId")
    List<AccompanyMember> findByAccompanyId(Long accompanyId);

}
