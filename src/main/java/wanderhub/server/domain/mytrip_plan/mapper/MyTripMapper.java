package wanderhub.server.domain.mytrip_plan.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.mytrip_plan.dto.MyTripPlanDto;
import wanderhub.server.domain.mytrip_plan.entity.MyTripPlan;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface MyTripMapper {

    default MyTripPlan myTripPlanPostDtoToMyTripPlanEntity(MyTripPlanDto.Post myTripPlanPostDto) {
        if(Objects.nonNull(myTripPlanPostDto)) {
            return MyTripPlan.builder()
                    .title(myTripPlanPostDto.getTitle())
                    .tripStartDate(myTripPlanPostDto.getTripStartDate())
                    .tripEndDate(myTripPlanPostDto.getTripEndDate())
                    .build();
        }
        return null;
    }

    default MyTripPlan myTripPlanPatchDtoToMyTripPlanEntity(MyTripPlanDto.Patch myTripPlanPatchDto) {
        if(Objects.nonNull(myTripPlanPatchDto)) {
            return MyTripPlan.builder()
                    .title(myTripPlanPatchDto.getTitle())
                    .tripStartDate(myTripPlanPatchDto.getTripStartDate())
                    .tripEndDate(myTripPlanPatchDto.getTripEndDate())
                    .build();
        }
        return null;
    }
}
