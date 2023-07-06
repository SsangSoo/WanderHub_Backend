package wanderhub.server.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.member.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query("select m FROM Member m WHERE m.nickName = :nickName")
    Optional<Member> findByNickName(@Param("nickName") String nickName);

//    @Query("select am from AccompanyMember as am where am.member.Id = (select m.Id from Member as m)")  // 멤버에 해당하는 AccompanyMember
//    List<AccompanyMember> accompanyMemberByEquMemberId(Member member);



}
