package wanderhub.server.domain.accompany_member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;
import wanderhub.server.domain.accompany_member.repository.AccompanyMemberRepository;
import wanderhub.server.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class AccompanyMemberService {

    private final AccompanyMemberRepository accompanyMemberRepository;

    public AccompanyMemberService(AccompanyMemberRepository accompanyMemberRepository) {
        this.accompanyMemberRepository = accompanyMemberRepository;
    }

    public void createAccompanyMember(Accompany accompany, Member member) {
        AccompanyMember createdAccomapnyMember = AccompanyMember.builder()
                .accompany(accompany)
                .member(member)
                .build();
        AccompanyMember savedAccomapnyMember = accompanyMemberRepository.save(createdAccomapnyMember);
        accompany.getAccompanyMemberList().add(savedAccomapnyMember);   // Accompany의 Member에 생성

    }

    public void removeAccompanyMemberByAccompanyId(Long accompanyId) {
        accompanyMemberRepository.deleteAccompanyMemberByAccompany_AccompanyId(accompanyId);
    }

    public Optional<AccompanyMember> findAccompanyMember(Long accompanyId, Long memberId) {
        return accompanyMemberRepository.findByAccompany_AccompanyIdAndMemberId(accompanyId, memberId);
    }

    public void outAccompanyMember(Long accompanyId, Long memberId) {
        accompanyMemberRepository.deleteAccompanyMemberByAccompany_AccompanyIdAndMember_Id(accompanyId, memberId);
    }
}
