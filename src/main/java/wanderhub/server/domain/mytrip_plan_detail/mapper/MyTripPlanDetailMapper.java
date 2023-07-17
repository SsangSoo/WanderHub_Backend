package wanderhub.server.domain.mytrip_plan_detail.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.mytrip_plan_detail.dto.MyTripPlanDetailDto;
import wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface MyTripPlanDetailMapper {

    default MyTripPlanDetail myTripPlanDetailPostDtoToEntity(MyTripPlanDetailDto.Post post) {
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
}
