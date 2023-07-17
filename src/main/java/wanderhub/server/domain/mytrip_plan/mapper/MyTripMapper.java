package wanderhub.server.domain.mytrip_plan.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.mytrip_plan.dto.MyTripPlanDto;
import wanderhub.server.domain.mytrip_plan.entity.MyTripPlan;

@Mapper(componentModel = "spring")
public interface MyTripMapper {

    default MyTripPlan myTripPlanPostDtoToMyTripPlanEntity(MyTripPlanDto.Post myTripPlanPostDto) {
        return MyTripPlan.builder()
                .title(myTripPlanPostDto.getTitle())
                .tripStartDate(myTripPlanPostDto.getTripStartDate())
                .tripEndDate(myTripPlanPostDto.getTripEndDate())
                .build();
    }

    default MyTripPlan myTripPlanPatchDtoToMyTripPlanEntity(MyTripPlanDto.Patch myTripPlanPatchDto) {
        return MyTripPlan.builder()
                .title(myTripPlanPatchDto.getTitle())
                .tripStartDate(myTripPlanPatchDto.getTripStartDate())
                .tripEndDate(myTripPlanPatchDto.getTripEndDate())
                .build();
    };
}
