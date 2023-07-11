package wanderhub.server.domain.accompany.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.accompany.dto.AccompanyDto;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;
import wanderhub.server.global.utils.Local;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccompanyMapper {

    // 생성 mapper
    default Accompany accompanyPostDtoToAccompanyEntity(AccompanyDto.Post accompanyPost) {
       if(accompanyPost==null) {
           return null;
       } else {
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
    }

    // 수정시 mapper
    default Accompany accompanyPatchDtoToAccompanyEntity(AccompanyDto.Patch patch) {
        if(patch==null) {
            return null;
        } else {
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

    }

    // 응답 mapper
    default AccompanyDto.Response accompanyEntityToAccompanyResponseDto(Accompany accompany) {
        if(accompany==null) {
            return null;
        } else {
            return AccompanyDto.Response.builder()
                    .id(accompany.getId())
                    .nickname(accompany.getNickname())
                    .local(accompany.getLocal().getLocalString())
                    .currentMemberNum(accompany.getAccompanyMemberList().size())
                    .maxMemberNum(accompany.getMaxMemberNum())
                    .accompanyStartDate(accompany.getAccompanyStartDate())
                    .accompanyEndDate(accompany.getAccompanyEndDate())
                    .title(accompany.getTitle())
                    .content(accompany.getContent())
                    .recruitComplete(accompany.isRecruitComplete())
                    .coordinateX(accompany.getCoordinateX())
                    .coordinateY(accompany.getCoordinateY())
                    .placeName(accompany.getPlaceName())
                    .joinMembers(nickNameOfMemberListFromAccomapnyMember(accompany.getAccompanyMemberList()))
                    .build();
        }
    }

    default List<String> nickNameOfMemberListFromAccomapnyMember(List<AccompanyMember> accompanyMemberList) {
        if (accompanyMemberList == null) {
            return null;
        } else {
            List<String> nickNameList = new ArrayList<>();
            for (AccompanyMember accompanyMember : accompanyMemberList) {
                String joinMemberNickName = accompanyMember.getMember().getNickName();
                nickNameList.add(joinMemberNickName);
            }
            return nickNameList;
        }
    }

    // List형식의 Accompany
    default AccompanyDto.ListResponse accompanyEntityToAccompanyResponseList(Accompany accompany) {
        if(accompany==null) {
            return null;
        } else {
            return AccompanyDto.ListResponse.builder()
                    .id(accompany.getId())
                    .nickname(accompany.getNickname())
                    .local(accompany.getLocal().getLocalString())
                    .currentMemberNum(accompany.getAccompanyMemberList().size())
                    .maxMemberNum(accompany.getMaxMemberNum())
                    .accompanyStartDate(accompany.getAccompanyStartDate())
                    .accompanyEndDate(accompany.getAccompanyEndDate())
                    .title(accompany.getTitle())
                    .recruitComplete(accompany.isRecruitComplete())
                    .build();
        }
    }

    // 전체 목록 형식으로 만들기 // 페이지네이션, 전체조회
    default List<AccompanyDto.ListResponse> accompanyEntityListToResponseList(List<Accompany> accompanyList) {
        if(accompanyList==null) {
            return null;
        } else {
            List<AccompanyDto.ListResponse> responseList = new ArrayList<>();
            for(Accompany accompany : accompanyList) {
                responseList.add(accompanyEntityToAccompanyResponseList(accompany));
            }
            return responseList;
        }
    }

}