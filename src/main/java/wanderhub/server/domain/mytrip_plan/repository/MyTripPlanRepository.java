package wanderhub.server.domain.mytrip_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.mytrip_plan.entity.MyTripPlan;

@Repository
public interface MyTripPlanRepository extends JpaRepository<MyTripPlan, Long> {
}
