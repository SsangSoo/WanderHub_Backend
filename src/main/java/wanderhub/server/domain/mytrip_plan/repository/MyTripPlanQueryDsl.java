package wanderhub.server.domain.mytrip_plan.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany.dto.AccompanyResponseDto;
import wanderhub.server.domain.accompany.dto.QAccompanyResponseDto;
import wanderhub.server.domain.mytrip_plan.dto.MyTripPlanDto;
import wanderhub.server.domain.mytrip_plan.dto.MyTripPlanResponseDto;
import wanderhub.server.domain.mytrip_plan.dto.QMyTripPlanResponseDto;

import javax.persistence.EntityManager;

import static wanderhub.server.domain.mytrip_plan.entity.QMyTripPlan.*;

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

//        List<>

        return myTripPlanResponseDto;
    }

}
