package wanderhub.server.domain.accompany_member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import wanderhub.server.domain.accompany_member.dto.AccompanyMemberResponseDto;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccompanyMemberMapper {

    AccompanyMemberMapper INSTANCE = Mappers.getMapper(AccompanyMemberMapper.class);

    //Entity -> ResponseDto
    // 추가 : accompanyId와 memberId가 null값으로 들어옴.
        // 원인 : AccompanyMember 엔티티에는 객체로 되어있지만,
            // ResponseDto에는 Long으로 들어가기 때문, mapper로는 한계가 있음.
            // 고로 수동으로 매핑해주는 게 좋음
//    AccompanyMemberResponseDto accompanyMemberToAccompanyMemberResponseDto(AccompanyMember entity);
    default AccompanyMemberResponseDto accompanyMemberToAccompanyMemberResponseDto(AccompanyMember entity) {
        if(entity==null) {
            return null;
        } else {
            return AccompanyMemberResponseDto.builder()
                    .id(entity.getId())
                    .accompanyId(entity.getAccompany().getId())
                    .memberId(entity.getMember().getId())
                    .createdAt(entity.getCreatedAt())
                    .modifiedAt(entity.getModifiedAt())
                    .build();
        }
    }

    List<AccompanyMemberResponseDto> accompanyMemberToAccompanyMemberDtoResponseList(List<AccompanyMember> entityList);



}