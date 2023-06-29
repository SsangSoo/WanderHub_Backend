package wanderhub.server.domain.accompany_member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.accompany.service.AccompanyService;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;
import wanderhub.server.domain.accompany_member.repository.AccompanyMemberRepository;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.service.MemberService;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccompanyMemberService {

    private final AccompanyMemberRepository accompanyMemberRepository;
    private final AccompanyService accompanyService;
    private final MemberService memberService;

    public void createAccompanyMember(Long accompanyId, Long memberId) {
        Member member = memberService.findMember(memberId);
        memberService.verificationMember(member); //회원 검증

        //인원수 체크
        Accompany accompany = accompanyService.findById(accompanyId).get();
        List<AccompanyMember> memberList = accompanyMemberRepository.findByAccompany(accompany);
        if(!memberList.isEmpty() && memberList.size()>=accompany.getMaxNum()) {
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_JOIN_MAX_NUM_OVER);
        }

        //이미 가입되어있으면 안되게 처리
        List<AccompanyMember> accompanyList = accompanyMemberRepository.findByMember(member);
        for(AccompanyMember chk : accompanyList) {
            if(chk.getAccompany().getId()==accompanyId) {
                throw new CustomLogicException(ExceptionCode.ACCOMPANY_JOIN_ALREADY_JOINED);
            }
        }

        AccompanyMember am = AccompanyMember.builder()
                .accompany(accompanyService.findById(accompanyId).get())
                .member(memberService.findMember(memberId))
                .build();
        accompanyMemberRepository.save(am);
    }

    //남이 생성한 동행에서 나오기 기능
    public void deleteAccompanyMember(Long accompanyId, Long memberId) {
        Member member = memberService.findMember(memberId);
        memberService.verificationMember(member); //회원 검증

        Accompany accompany = accompanyService.findById(accompanyId).get();

        //본인이 생성한 동행글인지 검증
        if(accompany.getNickname().equals(member.getNickName())) {
            throw new CustomLogicException(ExceptionCode.ACCOMPANY_JOIN_CANNOT_QUIT);
        }

        List<AccompanyMember> memberList = accompanyMemberRepository.findByAccompany(accompany);
        for (AccompanyMember tmp : memberList) {
            if(tmp.getMember().getId()==memberId) {
                accompanyMemberRepository.delete(tmp);
                return;
            }
        }
        //들어가있던 사람 아니면 예외처리
        throw new CustomLogicException(ExceptionCode.ACCOMPANY_JOIN_NOT_A_MEMBER);
    }



}
