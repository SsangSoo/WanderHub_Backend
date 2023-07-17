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

@Service
@Transactional
@Slf4j
public class MyTripPlanService {

    private final MemberService memberService;
    private final MyTripPlanRepository myTripPlanRepository;
    private final MyTripPlanQueryDsl myTripPlanQueryDsl;

    public MyTripPlanService(MemberService memberService, MyTripPlanRepository myTripPlanRepository, MyTripPlanQueryDsl myTripPlanQueryDsl) {
        this.memberService = memberService;
        this.myTripPlanRepository = myTripPlanRepository;
        this.myTripPlanQueryDsl = myTripPlanQueryDsl;
    }

    public MyTripPlanResponseDto createTripPlan(String email, MyTripPlan postMyTripPlan) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        postMyTripPlan.setMember(findMember);
        MyTripPlan savedMyTripPlan = myTripPlanRepository.save(postMyTripPlan);
        return myTripPlanQueryDsl.getMyTripPlan(savedMyTripPlan.getMyTripPlanId());
    }
}
