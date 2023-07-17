package wanderhub.server.domain.accompany.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.accompany.dto.AccompanyDto;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.global.utils.Local;

import java.util.Objects;


@Mapper(componentModel = "spring")
public interface AccompanyMapper {

    // 생성 mapper
    default Accompany accompanyPostDtoToAccompanyEntity(AccompanyDto.Post accompanyPost) {
       if(Objects.nonNull(accompanyPost)) {
           return Accompany.builder()
                   .local(Local.findByLocal(accompanyPost.getLocal()))
                   .maxMemberNum(accompanyPost.getMaxMemberNum())
                   .accompanyStartDate(accompanyPost.getAccompanyStartDate())
                   .accompanyEndDate(accompanyPost.getAccompanyEndDate())
                   .title(accompanyPost.getTitle())
                   .content(accompanyPost.getContent())
                   .coordinateX(accompanyPost.getCoordinateX())
                   .coordinateY(accompanyPost.getCoordinateY())
                   .placeName(accompanyPost.getPlaceName())
                   .build();
       }
       return null;
    }

    // 수정시 mapper
    default Accompany accompanyPatchDtoToAccompanyEntity(AccompanyDto.Patch patch) {
        if(Objects.nonNull(patch)) {
            return Accompany.builder()
                    .local(Local.findByLocal(patch.getLocal()))
                    .maxMemberNum(patch.getMaxMemberNum())
                    .accompanyStartDate(patch.getAccompanyStartDate())
                    .accompanyEndDate(patch.getAccompanyEndDate())
                    .title(patch.getTitle())
                    .content(patch.getContent())
                    .coordinateX(patch.getCoordinateX())
                    .coordinateY(patch.getCoordinateY())
                    .placeName(patch.getPlaceName())
                    .build();
        }
        return null;
    }

}