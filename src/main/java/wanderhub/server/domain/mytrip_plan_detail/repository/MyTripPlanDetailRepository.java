package wanderhub.server.domain.mytrip_plan_detail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail;

@Repository
public interface MyTripPlanDetailRepository extends JpaRepository<MyTripPlanDetail, Long> {
}
