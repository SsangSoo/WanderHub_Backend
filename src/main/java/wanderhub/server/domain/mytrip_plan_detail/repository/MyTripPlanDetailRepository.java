package wanderhub.server.domain.mytrip_plan_detail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail;

import java.util.Optional;

@Repository
public interface MyTripPlanDetailRepository extends JpaRepository<MyTripPlanDetail, Long> {

    @Query("select mtpd from MyTripPlanDetail mtpd where mtpd.myTripPlanDetailId = :myTripPlanDetailId and mtpd.myTripPlan.myTripPlanId = :myTripPlanId")
    Optional<MyTripPlanDetail> findByMyTripPlanDetailId(Long myTripPlanDetailId, Long myTripPlanId);
}
