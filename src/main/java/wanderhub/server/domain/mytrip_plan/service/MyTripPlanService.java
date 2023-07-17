package wanderhub.server.domain.mytrip_plan.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.service.MemberService;
import wanderhub.server.domain.mytrip_plan.dto.MyTripPlanResponseDto;
import wanderhub.server.domain.mytrip_plan.entity.MyTripPlan;
import wanderhub.server.domain.mytrip_plan.repository.MyTripPlanQueryDsl;
import wanderhub.server.domain.mytrip_plan.repository.MyTripPlanRepository;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;
import wanderhub.server.global.utils.CustomBeanUtils;

import java.util.Optional;

@Service
@Transactional
public class MyTripPlanService {

    private final MemberService memberService;
    private final MyTripPlanRepository myTripPlanRepository;
    private final MyTripPlanQueryDsl myTripPlanQueryDsl;
    private final CustomBeanUtils<MyTripPlan> customBeanUtils;

    public MyTripPlanService(MemberService memberService, MyTripPlanRepository myTripPlanRepository, MyTripPlanQueryDsl myTripPlanQueryDsl, CustomBeanUtils<MyTripPlan> customBeanUtils) {
        this.memberService = memberService;
        this.myTripPlanRepository = myTripPlanRepository;
        this.myTripPlanQueryDsl = myTripPlanQueryDsl;
        this.customBeanUtils = customBeanUtils;
    }

    // 개인 일정 생성
    public MyTripPlanResponseDto createTripPlan(String email, MyTripPlan postMyTripPlan) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        postMyTripPlan.setMember(findMember);
        MyTripPlan savedMyTripPlan = myTripPlanRepository.save(postMyTripPlan);
        return myTripPlanQueryDsl.getMyTripPlan(savedMyTripPlan.getMyTripPlanId());
    }

    // 개인 일정 수정
    public MyTripPlanResponseDto updateMyTripPlan(String email, Long myTripPlanId, MyTripPlan patchMyTripPlan) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // MyTrip 회원 확인
        MyTripPlan destMyTripPlan = verificationMyTrip(myTripPlanId, findMember.getNickName());
        MyTripPlan updatedMyTripPlan = customBeanUtils.copyNonNullProoerties(patchMyTripPlan, destMyTripPlan);
        return myTripPlanQueryDsl.getMyTripPlan(updatedMyTripPlan.getMyTripPlanId());
        // 추후 디테일 추가.
    }


    // 개인 일정 삭제
    public void removeMyTripPlan(String email, Long myTripPlanId) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        // MyTrip 회원 확인
        MyTripPlan removingMyTripPlan = verificationMyTrip(myTripPlanId, findMember.getNickName());
        myTripPlanRepository.delete(removingMyTripPlan);
        // 추후 디테일 삭제 같이 해야됨.
    }


    //----------유효성 검증-----------------------

    // myTripPlan 확인
    private MyTripPlan verificationMyTrip(Long myTripPlanId, String nickName) {
        Optional<MyTripPlan> myTripPlanById = myTripPlanRepository.findByMyTripPlanId(myTripPlanId);
        // 여행계획이 있는지 확인
        if(!myTripPlanById.isPresent()) {
            throw new CustomLogicException(ExceptionCode.TRIP_PLAN_NOT_FOUND);
        }
        MyTripPlan myTripPlan = myTripPlanById.get();
        // 닉네임이 만약 다른 사람이라면,
        if(!myTripPlan.getMember().getNickName().equals(nickName)) {
            throw new CustomLogicException(ExceptionCode.TRIP_PLAN_DIFFERENT_WRITER);
        }
        return myTripPlan;
    }
}