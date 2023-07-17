package wanderhub.server.domain.mytrip_plan_detail.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.member.service.MemberService;
import wanderhub.server.domain.mytrip_plan.entity.MyTripPlan;
import wanderhub.server.domain.mytrip_plan.service.MyTripPlanService;
import wanderhub.server.domain.mytrip_plan_detail.dto.MyTripPlanDetailResponseDto;
import wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail;
import wanderhub.server.domain.mytrip_plan_detail.repository.MyTripPlanDetailQueryDsl;
import wanderhub.server.domain.mytrip_plan_detail.repository.MyTripPlanDetailRepository;

@Service
@Transactional
public class MyTripPlanDetailService {

    private final MemberService memberService;
    private final MyTripPlanService myTripPlanService;
    private final MyTripPlanDetailQueryDsl myTripPlanDetailQueryDsl;
    private final MyTripPlanDetailRepository myTripPlanDetailRepository;

    public MyTripPlanDetailService(MemberService memberService, MyTripPlanService myTripPlanService, MyTripPlanDetailQueryDsl myTripPlanDetailQueryDsl, MyTripPlanDetailRepository myTripPlanDetailRepository) {
        this.memberService = memberService;
        this.myTripPlanService = myTripPlanService;
        this.myTripPlanDetailQueryDsl = myTripPlanDetailQueryDsl;
        this.myTripPlanDetailRepository = myTripPlanDetailRepository;
    }

    public MyTripPlanDetailResponseDto createTripPlanDetail(String email, Long myTripPlanId, MyTripPlanDetail myTripPlanDetail) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        MyTripPlan myTripPlan = myTripPlanService.verificationMyTrip(myTripPlanId, findMember.getNickName());
        myTripPlanDetail.setMyTripPlan(myTripPlan);
        MyTripPlanDetail createdTripPlanDetail = myTripPlanDetailRepository.save(myTripPlanDetail);
        return myTripPlanDetailQueryDsl.getMyTripPlanDetail(createdTripPlanDetail.getMyTripPlanDetailId());
    }
}
