package wanderhub.server.domain.mytrip_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.mytrip_plan.entity.MyTripPlan;

import java.util.Optional;

@Repository
public interface MyTripPlanRepository extends JpaRepository<MyTripPlan, Long> {

    @Query("select mtp from MyTripPlan as mtp where mtp.myTripPlanId = :myTripPlanId")
    Optional<MyTripPlan> findByMyTripPlanId(Long myTripPlanId);

}