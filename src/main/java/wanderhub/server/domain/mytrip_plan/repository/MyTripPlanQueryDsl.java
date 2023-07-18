package wanderhub.server.domain.mytrip_plan.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany.dto.AccompanyResponseDto;
import wanderhub.server.domain.accompany.dto.QAccompanyResponseDto;
import wanderhub.server.domain.mytrip_plan.dto.*;
import wanderhub.server.domain.mytrip_plan_detail.dto.MyTripPlanDetailListResponseDto;
import wanderhub.server.domain.mytrip_plan_detail.dto.QMyTripPlanDetailListResponseDto;

import javax.persistence.EntityManager;

import java.util.List;

import static wanderhub.server.domain.mytrip_plan.entity.QMyTripPlan.*;
import static wanderhub.server.domain.mytrip_plan.dto.QMyTripPlanListResponseDto.*;
import static wanderhub.server.domain.mytrip_plan_detail.dto.QMyTripPlanDetailListResponseDto.*;
import static wanderhub.server.domain.mytrip_plan_detail.entity.QMyTripPlanDetail.*;

@Repository
public class MyTripPlanQueryDsl {

    private final JPAQueryFactory queryFactory;

    public MyTripPlanQueryDsl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public MyTripPlanResponseDto getMyTripPlan(Long myTripPlanId) {
        MyTripPlanResponseDto myTripPlanResponseDto;
        myTripPlanResponseDto = queryFactory
                .select(new QMyTripPlanResponseDto(
                        myTripPlan.myTripPlanId.as("myTripPlanId"),
                        myTripPlan.title,
                        myTripPlan.tripStartDate,
                        myTripPlan.tripEndDate,
                        myTripPlan.createdAt,
                        myTripPlan.modifiedAt
                ))
                .from(myTripPlan)
                .where(myTripPlan.myTripPlanId.eq(myTripPlanId))
                .fetchOne();
        return myTripPlanResponseDto;
    }

    public List<MyTripPlanListResponseDto> getMyTripPlanList(Long memberId) {
        List<MyTripPlanListResponseDto> myTripPlanListResponseDtoList;
        myTripPlanListResponseDtoList = queryFactory
                .select(new QMyTripPlanListResponseDto(
                        myTripPlan.myTripPlanId.as("myTripPlanId"),
                        myTripPlan.title,
                        myTripPlan.tripStartDate,
                        myTripPlan.tripEndDate,
                        myTripPlan.createdAt
                ))
                .from(myTripPlan)
                .where(myTripPlan.member.Id.eq(memberId))
                .fetch();
        return myTripPlanListResponseDtoList;
    }

    public List<MyTripPlanDetailListResponseDto> getTripPlanDetailListOfMyTripPlan(Long myTripPlanId) {
        List<MyTripPlanDetailListResponseDto> myTripPlanDetailListResponseList;
        myTripPlanDetailListResponseList = queryFactory
                .select(new QMyTripPlanDetailListResponseDto(
                        myTripPlanDetail.myTripPlanDetailId,
                        myTripPlanDetail.subTitle,
                        myTripPlanDetail.placeName,
                        myTripPlanDetail.whenDate,
                        myTripPlanDetail.createdAt
                ))
                .from(myTripPlanDetail)
                .where(myTripPlanDetail.myTripPlan.myTripPlanId.eq(myTripPlanId))
                .fetch();
        return myTripPlanDetailListResponseList;
    }

    public MyTripPlanResponseDto getOnceMyTripPlan(Long myTripPlanId) {
        MyTripPlanResponseDto myTripPlan = getMyTripPlan(myTripPlanId);
        List<MyTripPlanDetailListResponseDto> myTripPlanDetailListResponseList = getTripPlanDetailListOfMyTripPlan(myTripPlanId);
        myTripPlan.setMyTripPlanDetailResponseDtoList(myTripPlanDetailListResponseList);
        return myTripPlan;
    }
}