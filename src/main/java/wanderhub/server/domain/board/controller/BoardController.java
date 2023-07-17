package wanderhub.server.domain.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wanderhub.server.auth.jwt.refreshtoken.service.TokenService;
import wanderhub.server.domain.board.dto.BoardTempDto;
import wanderhub.server.domain.board.entity.Board;
import wanderhub.server.domain.board.mapper.BoardMapper;
import wanderhub.server.domain.board.service.BoardService;
import wanderhub.server.global.response.PageInfo;
import wanderhub.server.global.response.PageTempResponseDto;
import wanderhub.server.global.response.SingleResponse;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Slf4j
@RestController
@Validated
@RequestMapping("/v1/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper boardMapper;
    private final TokenService tokenService;

    public BoardController(BoardService boardService, BoardMapper boardMapper, TokenService tokenService) {
        this.boardService = boardService;
        this.boardMapper = boardMapper;
        this.tokenService = tokenService;
    }

    // 게시판 작성
    @PostMapping
    public ResponseEntity boardPost(HttpServletRequest request, Principal principal, @Validated @RequestBody BoardTempDto.Post post) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 Token확인
        Board createBoardFromPostDto = boardMapper.boardPostDtoToBoardEntity(post);     // Dto로부터 생성된 객체
        String email = principal.getName();
        Board createdBoard = boardService.createBoard(createBoardFromPostDto, email);   // 서비스계층에서 Entity 생성
        BoardTempDto.Response boardResponse = boardMapper.boardEntityToBoardResponseDto(createdBoard);   // Response로
        return new ResponseEntity(new SingleResponse<>(boardResponse), HttpStatus.CREATED);
    }

    // 게시판 수정
    @PatchMapping("/{board-id}")
    public ResponseEntity boardPatch(HttpServletRequest request, @PathVariable("board-id")Long boardId, Principal principal, @Validated @RequestBody BoardTempDto.Patch patch) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 Token확인
        Board patchBoardFromPatchDto = boardMapper.boardPatchDtoToBoardEntity(patch);           // Dto로부터 생성된 객체
        String email = principal.getName();                                                     // email을 찾는다.
        Board updatedBoard = boardService.updateBoard(boardId, patchBoardFromPatchDto, email);  // response로
        return ResponseEntity.ok(boardMapper.boardEntityToBoardResponseDto(updatedBoard));
    }

    // 게시판 삭제
    @DeleteMapping("/{board-id}")
    public ResponseEntity deleteBoard(HttpServletRequest request, @PathVariable("board-id")Long boardId, Principal principal) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 Token확인
        boardService.removeBoard(boardId, principal.getName());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 게시판 단일 조회(N+1 문제 발생)
    @GetMapping("/temp/{board-id}")
    public ResponseEntity getTempBoard(@PathVariable("board-id")Long boardId) {
        Board getBoardEntity = boardService.getTempBoard(boardId);
        BoardTempDto.Response boardResponse = boardMapper.boardEntityToBoardResponseDto(getBoardEntity);
        return new ResponseEntity(new SingleResponse<>(boardResponse), HttpStatus.OK);
    }

    // 게시판 좋아요
    @PatchMapping("/{board-id}/heart")
    public ResponseEntity boardHeart(HttpServletRequest request, @PathVariable("board-id")Long boardId, Principal principal) {
        tokenService.verificationLogOutToken(request);  // 블랙리스트 Token확인
        Board likedBoard = boardService.likeBoard(boardId, principal.getName());
        BoardTempDto.Response boardResponse = boardMapper.boardEntityToBoardResponseDto(likedBoard);
        return ResponseEntity.ok(new SingleResponse<>(boardResponse));
    }


    // 게시판 전체 조회 (N+1 문제 발생)
    @GetMapping("/temp")
    public ResponseEntity tempGetBoards(@PageableDefault Pageable pageable){
        Page<Board> boardEntityListPage = boardService.findBoards(pageable);
        return  new ResponseEntity<>(
                new PageTempResponseDto<>(boardMapper.boardEntityListToBoardResponseDtoList(boardEntityListPage.getContent()),
                        new PageInfo(boardEntityListPage.getPageable(), boardEntityListPage.getTotalElements())), HttpStatus.OK);
    }

    // 게시판 전체 조회 N + 1 해결
    @GetMapping
    public ResponseEntity getAllBoard(@RequestParam(name = "page", defaultValue = "1") Integer page) {
        return ResponseEntity.ok(boardService.findAllBoards(page));
    }

    // 게시판 단일조회 N + 1 해결
    @GetMapping("/{board-id}")
    public ResponseEntity getBoard(@PathVariable("board-id")Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

}
