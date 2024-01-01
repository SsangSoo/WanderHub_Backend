//package wanderhub.server.domain.accompany.mapper;
//
//import org.mapstruct.Mapper;
//import wanderhub.server.domain.accompany.dto.AccompanyPostDto;
//import wanderhub.server.domain.accompany.entity.Accompany;
//import wanderhub.server.global.exception.CustomLogicException;
//import wanderhub.server.global.exception.ExceptionCode;
//import wanderhub.server.global.utils.Local;
//
//import java.time.LocalDate;
//import java.util.Objects;
//
//
//@Mapper(componentModel = "spring")
//public interface AccompanyMapper {
//
//    // 생성 mapper
//    default Accompany accompanyPostDtoToAccompanyEntity(AccompanyPostDto accompanyPost) {
//        LocalDate accompanyStartDate = accompanyPost.getAccompanyStartDate();
//        LocalDate accompanyEndDate = accompanyPost.getAccompanyEndDate();
//        if(accompanyEndDate.compareTo(accompanyStartDate) < 0) {
//            throw new CustomLogicException(ExceptionCode.DATE_INVALID);
//        }
//        if(Objects.nonNull(accompanyPost)) {
//               return Accompany.builder()
//                   .local(Local.getLocal(accompanyPost.getLocal()))
//                   .maxMemberCount(accompanyPost.getMaxMemberCount())
//                   .accompanyStartDate(accompanyPost.getAccompanyStartDate())
//                   .accompanyEndDate(accompanyPost.getAccompanyEndDate())
//                   .title(accompanyPost.getTitle())
//                   .content(accompanyPost.getContent())
//                   .coordinateX(accompanyPost.getCoordinateX())
//                   .coordinateY(accompanyPost.getCoordinateY())
//                   .placeName(accompanyPost.getPlaceName())
//                   .build();
//       }
//       return null;
//    }
//
//    // 수정시 mapper
//    default Accompany accompanyPatchDtoToAccompanyEntity(AccompanyPostDto.Patch accompanyPatch) {
//        if(accompanyPatch.getAccompanyEndDate().compareTo(accompanyPatch.getAccompanyStartDate())<0) {
//            throw new CustomLogicException(ExceptionCode.DATE_INVALID);
//        }
//        if(Objects.nonNull(accompanyPatch)) {
//            return Accompany.builder()
//                    .local(Local.getLocal(accompanyPatch.getLocal()))
//                    .maxMemberCount(accompanyPatch.getMaxMemberCount())
//                    .accompanyStartDate(accompanyPatch.getAccompanyStartDate())
//                    .accompanyEndDate(accompanyPatch.getAccompanyEndDate())
//                    .title(accompanyPatch.getTitle())
//                    .content(accompanyPatch.getContent())
//                    .coordinateX(accompanyPatch.getCoordinateX())
//                    .coordinateY(accompanyPatch.getCoordinateY())
//                    .placeName(accompanyPatch.getPlaceName())
//                    .build();
//        }
//        return null;
//    }
//
//}