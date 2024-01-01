package wanderhub.server.domain.bo_comment.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import wanderhub.server.domain.bo_comment.dto.BoCommentDto;
import wanderhub.server.domain.bo_comment.dto.BoCommentResponseDto;
import wanderhub.server.domain.bo_comment.entity.BoComment;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-29T17:42:53+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
@Component
public class BoardCommentMapperImpl implements BoardCommentMapper {

    @Override
    public BoComment boCommentPostAndPatchDtoToBoCommentEntity(BoCommentDto.PostAndPatch postDto) {
        if ( postDto == null ) {
            return null;
        }

        BoComment boComment = new BoComment();

        boComment.setContent( postDto.getContent() );

        return boComment;
    }

    @Override
    public List<BoCommentResponseDto> boCommentEntityListToBoCommentResponseDtoList(List<BoComment> boComments) {
        if ( boComments == null ) {
            return null;
        }

        List<BoCommentResponseDto> list = new ArrayList<BoCommentResponseDto>( boComments.size() );
        for ( BoComment boComment : boComments ) {
            list.add( boCommentEntityToBoCommentResponseDto( boComment ) );
        }

        return list;
    }
}
