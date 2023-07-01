package wanderhub.server.domain.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wanderhub.server.domain.community.dto.BoardDto;
import wanderhub.server.domain.community.entity.Board;
import wanderhub.server.domain.community.mapper.BoardMapper;
import wanderhub.server.domain.community.service.BoardService;
import wanderhub.server.global.response.PageInfo;
import wanderhub.server.global.response.PageResponseDto;
import wanderhub.server.global.response.SingleResponse;

import java.security.Principal;

@Slf4j
@RestController
@Validated
@RequestMapping("/v1/community")
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper boardMapper;

    public BoardController(BoardService boardService, BoardMapper boardMapper) {
        this.boardService = boardService;
        this.boardMapper = boardMapper;
    }

    // 게시판 작성
    @PostMapping
    public ResponseEntity boardPost(Principal principal, @Validated @RequestBody BoardDto.Post post) {
        Board createBoardFromPostDto = boardMapper.boardPostDtoToBoardEntity(post);     // Dto로부터 생성된 객체
        String email = principal.getName();
        Board createdBoard = boardService.createBoard(createBoardFromPostDto, email);   // 서비스계층에서 Entity 생성
        BoardDto.Response response = boardMapper.boardEntityToBoardResponseDto(createdBoard);   // Response로
        return new ResponseEntity(new SingleResponse<>(response), HttpStatus.CREATED);
    }

    // 게시판 수정
    @PatchMapping("/{board-id}")
    public ResponseEntity boardPatch(@PathVariable("board-id")Long boardId, Principal principal, @Validated @RequestBody BoardDto.Patch patch) {
        Board patchBoardFromPatchDto = boardMapper.boardPatchDtoToBoardEntity(patch);           // Dto로부터 생성된 객체
        String email = principal.getName();                                                     // email을 찾는다.
        Board updatedBoard = boardService.updateBoard(boardId, patchBoardFromPatchDto, email);  // response로
        return ResponseEntity.ok(boardMapper.boardEntityToBoardResponseDto(updatedBoard));
    }

    // 게시판 삭제
    @DeleteMapping("/{board-id}")
    public ResponseEntity deleteBoard(@PathVariable("board-id")Long boardId, Principal principal) {
        boardService.removeBoard(boardId, principal.getName());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 게시판 단일 조회
    @GetMapping("/{board-id}")
    public ResponseEntity getBoard(@PathVariable("board-id")Long boardId) {
        Board getBoardEntity = boardService.getBoard(boardId);
        BoardDto.Response response = boardMapper.boardEntityToBoardResponseDto(getBoardEntity);
        return new ResponseEntity(new SingleResponse<>(response), HttpStatus.OK);
    }


    //  게시판 전체 조회
    @GetMapping
    public ResponseEntity getBoards(@PageableDefault Pageable pageable){
        Page<Board> boardEntityListPage = boardService.findBorrows(pageable);
        return  new ResponseEntity<>(
                new PageResponseDto<>(boardMapper.boardEntityListToBoardResponseDtoList(boardEntityListPage.getContent()),
                        new PageInfo(boardEntityListPage.getPageable(), boardEntityListPage.getTotalElements())), HttpStatus.OK);
    }

}
