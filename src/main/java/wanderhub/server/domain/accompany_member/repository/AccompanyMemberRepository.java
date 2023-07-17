package wanderhub.server.domain.accompany_member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccompanyMemberRepository extends JpaRepository<AccompanyMember, Long> {

    @Query("select am from AccompanyMember as am where am.accompany.id = :accompanyId")
    List<AccompanyMember> findByAccompanyId(Long accompanyId);

    @Query("select am from AccompanyMember as am where am.accompany.accompanyId = :accompanyId and am.member.Id = :memberId")
    Optional<AccompanyMember> findByAccompany_AccompanyIdAndMemberId(Long accompanyId, Long memberId);

    @Modifying
    @Query("delete from AccompanyMember as am where am.accompany.accompanyId = :accompanyId")
    int deleteAccompanyMemberByAccompany_AccompanyId(Long accompanyId);

    @Modifying
    @Query("delete from AccompanyMember as am where am.accompany.accompanyId = :accompanyId and am.member.Id = :memberId")
    int deleteAccompanyMemberByAccompany_AccompanyIdAndMember_Id(Long accompanyId, Long memberId);
}
