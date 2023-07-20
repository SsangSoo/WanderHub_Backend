package wanderhub.server.domain.mytrip_plan_detail.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.mytrip_plan_detail.dto.MyTripPlanDetailDto;
import wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface MyTripPlanDetailMapper {

    default MyTripPlanDetail myTripPlanDetailPostDtoToEntity(MyTripPlanDetailDto.Post post) {
        if(post.getTimeEnd().compareTo(post.getTimeStart())<0) {
            throw new CustomLogicException(ExceptionCode.TIME_INVALID);
        }
        if(Objects.nonNull(post)) {
            return MyTripPlanDetail.builder()
                    .subTitle(post.getSubTitle())
                    .content(post.getContent())
                    .coordinateX(post.getCoordinateX())
                    .coordinateY(post.getCoordinateY())
                    .placeName(post.getPlaceName())
                    .whenDate(post.getWhenDate())
                    .timeStart(post.getTimeStart())
                    .timeEnd(post.getTimeEnd())
                    .build();
        }
        return null;
    }

    default MyTripPlanDetail myTripPlanDetailPatchDtoToEntity(MyTripPlanDetailDto.Patch patch) {
        if(patch.getTimeEnd().compareTo(patch.getTimeStart())<0) {
            throw new CustomLogicException(ExceptionCode.TIME_INVALID);
        }
        if(Objects.nonNull(patch)) {
            return MyTripPlanDetail.builder()
                    .subTitle(patch.getSubTitle())
                    .content(patch.getContent())
                    .coordinateX(patch.getCoordinateX())
                    .coordinateY(patch.getCoordinateY())
                    .placeName(patch.getPlaceName())
                    .whenDate(patch.getWhenDate())
                    .timeStart(patch.getTimeStart())
                    .timeEnd(patch.getTimeEnd())
                    .build();
        }
        return null;
    }
}