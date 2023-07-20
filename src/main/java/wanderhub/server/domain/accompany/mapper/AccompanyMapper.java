package wanderhub.server.domain.accompany.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.accompany.dto.AccompanyDto;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.global.exception.CustomLogicException;
import wanderhub.server.global.exception.ExceptionCode;
import wanderhub.server.global.utils.Local;

import java.util.Objects;


@Mapper(componentModel = "spring")
public interface AccompanyMapper {

    // 생성 mapper
    default Accompany accompanyPostDtoToAccompanyEntity(AccompanyDto.Post accompanyPost) {
        if(accompanyPost.getAccompanyEndDate().compareTo(accompanyPost.getAccompanyStartDate())<0) {
            throw new CustomLogicException(ExceptionCode.DATE_INVALID);
        }

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
    default Accompany accompanyPatchDtoToAccompanyEntity(AccompanyDto.Patch accompanyPatch) {
        if(accompanyPatch.getAccompanyEndDate().compareTo(accompanyPatch.getAccompanyStartDate())<0) {
            throw new CustomLogicException(ExceptionCode.DATE_INVALID);
        }
        if(Objects.nonNull(accompanyPatch)) {
            return Accompany.builder()
                    .local(Local.findByLocal(accompanyPatch.getLocal()))
                    .maxMemberNum(accompanyPatch.getMaxMemberNum())
                    .accompanyStartDate(accompanyPatch.getAccompanyStartDate())
                    .accompanyEndDate(accompanyPatch.getAccompanyEndDate())
                    .title(accompanyPatch.getTitle())
                    .content(accompanyPatch.getContent())
                    .coordinateX(accompanyPatch.getCoordinateX())
                    .coordinateY(accompanyPatch.getCoordinateY())
                    .placeName(accompanyPatch.getPlaceName())
                    .build();
        }
        return null;
    }

}