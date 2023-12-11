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

    public void outMember(Member member) {
        accompanyMemberRepository.deleteAccompanyMemberByMemberId(member.getId()); // AccompanyMember 테이블에서 memberId를 가지는 데이터 삭제
        // 다른 회원이 있을 경우, 2번째 회원에게 방장을 양도한다. // 추후
        //      List<AccompanyMember> accompanyMemberList = member.getAccompanyMemberList();
            // 동행들 찾고,
            // 멤버가 2명이상인 동행 거르고 방장 양도하는 bulk 연산을하고,
            // 멤버가 1명이면, 동행에 참여한 다른 회원은 없으므로 연관된 동행은 삭제한다.
            // 다른 회원이 없을 경우
        accompanyMemberRepository.deleteAccompanyByAccompanyMaker(member.getNickname());

    }

    public void outAccompanyMember(Long accompanyId, Long memberId) {
        accompanyMemberRepository.deleteAccompanyMemberByAccompany_AccompanyIdAndMember_Id(accompanyId, memberId);
    }
}
