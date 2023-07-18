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
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;
import wanderhub.server.global.utils.CustomBeanUtils;

import java.util.Optional;

@Service
@Transactional
public class MyTripPlanDetailService {

    private final MemberService memberService;
    private final MyTripPlanService myTripPlanService;
    private final MyTripPlanDetailQueryDsl myTripPlanDetailQueryDsl;
    private final MyTripPlanDetailRepository myTripPlanDetailRepository;
    private final CustomBeanUtils<MyTripPlanDetail> customBeanUtils;

    public MyTripPlanDetailService(MemberService memberService, MyTripPlanService myTripPlanService, MyTripPlanDetailQueryDsl myTripPlanDetailQueryDsl, MyTripPlanDetailRepository myTripPlanDetailRepository, CustomBeanUtils<MyTripPlanDetail> customBeanUtils) {
        this.memberService = memberService;
        this.myTripPlanService = myTripPlanService;
        this.myTripPlanDetailQueryDsl = myTripPlanDetailQueryDsl;
        this.myTripPlanDetailRepository = myTripPlanDetailRepository;
        this.customBeanUtils = customBeanUtils;
    }

    // 일정 디테일 생성
    public MyTripPlanDetailResponseDto createTripPlanDetail(String email, Long myTripPlanId, MyTripPlanDetail myTripPlanDetail) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        MyTripPlan myTripPlan = myTripPlanService.verificationMyTrip(myTripPlanId, findMember.getNickName());
        myTripPlanDetail.setMyTripPlan(myTripPlan);
        MyTripPlanDetail createdTripPlanDetail = myTripPlanDetailRepository.save(myTripPlanDetail);
        return myTripPlanDetailQueryDsl.getMyTripPlanDetail(createdTripPlanDetail.getMyTripPlanDetailId());
    }
    
    // 일정 디테일 수정
    public MyTripPlanDetailResponseDto updateTripPlanDetail(String email, Long myTripPlanId, Long myTripPlanDetailId, MyTripPlanDetail patchToMyTripPlanDetail) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        myTripPlanService.verificationMyTrip(myTripPlanId, findMember.getNickName()); // MyTripPlan 확인
        // 부모와 자식 Id가 맞는 MyTripPlanDetail 확인
        MyTripPlanDetail byMyTripPlanDetailId = findByMyTripPlanDetailId(myTripPlanDetailId, myTripPlanId);
        MyTripPlanDetail updatedMyTripPlanDetail = customBeanUtils.copyNonNullProoerties(patchToMyTripPlanDetail, byMyTripPlanDetailId);
        return myTripPlanDetailQueryDsl.getMyTripPlanDetail(updatedMyTripPlanDetail.getMyTripPlanDetailId());
    }

    // 일정 디테일 삭제
    public void removeMyTripPlanDetail(String email, Long myTripPlanId, Long myTripPlanDetailId) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        myTripPlanService.verificationMyTrip(myTripPlanId, findMember.getNickName()); // MyTripPlan 확인
        // 부모와 자식 Id가 맞는 MyTripPlanDetail 확인
        MyTripPlanDetail byMyTripPlanDetailId = findByMyTripPlanDetailId(myTripPlanDetailId, myTripPlanId);
        myTripPlanDetailRepository.delete(byMyTripPlanDetailId);
    }

    // 일정 단일 조회
    public MyTripPlanDetailResponseDto getOnceMyTripPlanDetail(String email, Long myTripPlanId, Long myTripPlanDetailId) {
        Member findMember = memberService.findMember(email);
        memberService.verificationMember(findMember);       // 통과시 회원 검증 완료
        myTripPlanService.verificationMyTrip(myTripPlanId, findMember.getNickName()); // MyTripPlan 확인
        // 부모와 자식 Id가 맞는 MyTripPlanDetail 확인
        MyTripPlanDetail byMyTripPlanDetailId = findByMyTripPlanDetailId(myTripPlanDetailId, myTripPlanId);
        return myTripPlanDetailQueryDsl.getMyTripPlanDetail(myTripPlanDetailId);
    }

    //--------------유효성 검증------------------
    // MyTripPlanDetail이 있는지 확인
    public MyTripPlanDetail findByMyTripPlanDetailId(Long myTripPlanDetailId, Long myTripPlanId) {
        Optional<MyTripPlanDetail> byMyTripPlanDetailId = myTripPlanDetailRepository.findByMyTripPlanDetailId(myTripPlanDetailId, myTripPlanId);
        return byMyTripPlanDetailId.orElseThrow(() -> new CustomLogicException(ExceptionCode.TRIP_PLAN_DETAIL_NOT_FOUND));
    }

}