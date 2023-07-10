package wanderhub.server.domain.board.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.bo_comment.mapper.BoardCommentMapperImpl;
import wanderhub.server.domain.board.dto.BoardDto;
import wanderhub.server.domain.board.entity.Board;
import wanderhub.server.domain.bo_comment.mapper.BoardCommentMapper;
import wanderhub.server.global.utils.Local;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    BoardCommentMapper boCommentMapper = new BoardCommentMapperImpl();

    default Board boardPostDtoToBoardEntity(BoardDto.Post post) {
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

    default Board boardPatchDtoToBoardEntity(BoardDto.Patch patch) {
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

    // 게시판 단일 조회 Response Mapper
    default BoardDto.Response boardEntityToBoardResponseDto(Board board) {
        if(board == null) {
            return null;
        } else {
            BoardDto.Response response = BoardDto.Response.builder()
                    .boardId(board.getBoardId())
                    .nickName(board.getNickName())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .local(board.getLocal().getLocalString())
                    .viewPoint(board.getViewPoint())
                    .likePoint(board.getBoardHeartList().size())  // 연산
                    .createdAt(board.getCreatedAt())
                    .modifiedAt(board.getModifiedAt())
                    .boComments(boCommentMapper.boCommentEntityListToBoCommentResponseDtoList(board.getBoCommentList()))
                    .build();
            return response;
        }
    }

    // 개시판 전체 조회용 Response Mapper
    default BoardDto.ListResponse boardEntityToBoardListResponseDto(Board board) {
        if(board == null) {
            return null;
        } else {
            BoardDto.ListResponse listCaseResponse = BoardDto.ListResponse.builder()
                    .boardId(board.getBoardId())
                    .nickName(board.getNickName())
                    .title(board.getTitle())
                    .local(board.getLocal().getLocalString())
                    .viewPoint(board.getViewPoint())
                    .likePoint(board.getBoardHeartList().size())
                    .createdAt(board.getCreatedAt())
                    .modifiedAt(board.getModifiedAt())
                    .build();
            return listCaseResponse;
        }

    }

    // 게시판 전체 조회 List형식으로 mapping
    default List<BoardDto.ListResponse> boardEntityListToBoardResponseDtoList(List<Board> boardList) {
        if(boardList == null) {
            return null;
        } else {
            List<BoardDto.ListResponse> list = new ArrayList<BoardDto.ListResponse>(boardList.size());
            for(Board board : boardList) {
                BoardDto.ListResponse response = boardEntityToBoardListResponseDto(board);
                list.add(response);
            }
            return list;
        }
    }

}