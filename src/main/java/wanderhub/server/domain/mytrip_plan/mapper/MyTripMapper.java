package wanderhub.server.domain.mytrip_plan.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.mytrip_plan.dto.MyTripPlanDto;
import wanderhub.server.domain.mytrip_plan.entity.MyTripPlan;

import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface MyTripMapper {

    default MyTripPlan myTripPlanPostDtoToMyTripPlanEntity(MyTripPlanDto.Post myTripPlanPostDto) {
        if(myTripPlanPostDto.getTripEndDate().compareTo(myTripPlanPostDto.getTripStartDate())<0) {
            throw new CustomLogicException(ExceptionCode.DATE_INVALID);
        }

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
        if(myTripPlanPatchDto.getTripEndDate().compareTo(myTripPlanPatchDto.getTripStartDate())<0) {
            throw new CustomLogicException(ExceptionCode.DATE_INVALID);
        }
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
