package wanderhub.server.domain.accompany_member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;
import wanderhub.server.domain.accompany_member.repository.AccompanyMemberRepository;
import wanderhub.server.domain.member.entity.Member;

import java.util.List;

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
        accompany.getAccompanyMemberList().add(accompanyMemberRepository.save(createdAccomapnyMember));

    }

    public void removeAccompanyMemberByAccompanyId(Long accompanyId) {
        List<AccompanyMember> findAccompanyMembersByAccompanyId = accompanyMemberRepository.findByAccompanyId(accompanyId);
        for(AccompanyMember am : findAccompanyMembersByAccompanyId) {
            accompanyMemberRepository.delete(am);
        }
    }
}
