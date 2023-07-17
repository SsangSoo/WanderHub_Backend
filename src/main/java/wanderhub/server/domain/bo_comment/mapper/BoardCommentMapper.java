package wanderhub.server.domain.bo_comment.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.bo_comment.dto.BoCommentDto;
import wanderhub.server.domain.bo_comment.entity.BoComment;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface BoardCommentMapper {

    BoComment boCommentPostAndPatchDtoToBoCommentEntity(BoCommentDto.PostAndPatch postDto);

    default BoCommentDto.Response boCommentEntityToBoCommentResponseDto(BoComment boComment) {
        if(Objects.nonNull(boComment)) {
            return BoCommentDto.Response.builder()
                    .boCommentId(boComment.getBoCommentId())
                    .boardId(boComment.getBoard().getBoardId())
                    .nickName(boComment.getNickName())
                    .content(boComment.getContent())
                    .likePoint(boComment.getBoCommentHeartList().size())
                    .createdAt(boComment.getCreatedAt())
                    .modifiedAt(boComment.getModifiedAt())
                    .build();
        }
        return null;
    }

    List<BoCommentDto.Response> boCommentEntityListToBoCommentResponseDtoList(List<BoComment> boComments);   // 댓글들을 리스트로 담아서 응답객체로 주기 위해.
}

