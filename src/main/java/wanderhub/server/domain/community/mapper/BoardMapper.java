package wanderhub.server.domain.community.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.community.dto.BoardDto;
import wanderhub.server.domain.community.entity.Board;
import wanderhub.server.domain.community_comment.mapper.BoardCommentMapper;
import wanderhub.server.domain.community_comment.mapper.BoardCommentMapperImpl;
import wanderhub.server.global.utils.Local;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import wanderhub.server.global.utils.Local;

@Mapper(componentModel = "spring")
public interface BoardMapper {
  
    BoardCommentMapper boCommentMapper = new BoardCommentMapperImpl();

    default Board boardPostDtoToBoard(BoardDto.Post post) {
        if(post==null) {
            return null;
        } else {
            return Board.builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .local(Local.findByLocal(post.getLocal()))
                    .build();
        }
    }

    default Board boardPatchDtoToBoard(BoardDto.Patch patch) {
        if(patch==null) {
            return null;
        } else {
            return Board.builder()
                    .title(patch.getTitle())
                    .content(patch.getContent())
                    .local(Local.findByLocal(patch.getLocal()))
                    .build();
        }
    }

    default BoardDto.Response boardToBoardResponseDto(Board board) {
        if(board == null) {
            return null;
        } else {
            BoardDto.Response response = BoardDto.Response.builder()
                    .boardId(board.getBoardId())
                    .nickName(board.getNickName())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .local(board.getLocal().getLocal())
                    .viewPoint(board.getViewPoint())
                    .createdAt(board.getCreatedAt())
                    .modifiedAt(board.getModifiedAt())
                    .build();
            if(board.getBoComments()!=null) {       // 댓글들 스트림 사용해서 Response로 변환 후 list에 담음
                response.setBoComments(boCommentMapper.boCommentsToBoCommentDtoResponseList(board.getBoComments()));
            }
            return response;
        }
    }

    default List<BoardDto.Response> boardsToBoardDtoResponseList(List<Board> boardList) {
        if(boardList == null) {
            return null;
        } else {
            List<BoardDto.Response> list = new ArrayList<BoardDto.Response>(boardList.size());
            for(Board board : boardList) {
                BoardDto.Response response = boardToBoardResponseDto(board);
                response.setBoComments(null);
                list.add(response);
            }
            return list;
        }
    }

}
