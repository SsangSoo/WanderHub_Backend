package wanderhub.server.domain.bo_comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wanderhub.server.auth.jwt.refreshtoken.service.TokenService;
import wanderhub.server.domain.bo_comment.dto.BoCommentDto;
import wanderhub.server.domain.bo_comment.entity.BoComment;
import wanderhub.server.domain.bo_comment.mapper.BoardCommentMapper;
import wanderhub.server.domain.bo_comment.service.BoCommentService;
import wanderhub.server.global.response.SingleResponse;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/v1/board/comment")
public class BoCommentController {

    private final BoardCommentMapper boardCommentMapper;
    private final BoCommentService boCommentService;
    private final TokenService tokenService;

    public BoCommentController(BoardCommentMapper boardCommentMapper, BoCommentService boCommentService, TokenService tokenService) {
        this.boardCommentMapper = boardCommentMapper;
        this.boCommentService = boCommentService;
        this.tokenService = tokenService;
    }

    // 댓글 생성
    @PostMapping("/{board-id}")
    public ResponseEntity postBoardComment(HttpServletRequest request,
                                           Principal principal,
                                               @PathVariable("board-id")Long boardId,
                                               @Validated @RequestBody BoCommentDto.PostAndPatch post) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 Token확인
        BoComment createdBoCommentFromPostDto = boardCommentMapper.boCommentPostAndPatchDtoToBoCommentEntity(post);    // postDto로부터 생성된 객체
        BoComment createdBoComment = boCommentService.createComment(boardId, createdBoCommentFromPostDto, principal.getName()); // 서비스계층에서 엔티티로 생성
        BoCommentDto.Response response = boardCommentMapper.boCommentEntityToBoCommentResponseDto(createdBoComment);    // resposne로
        return new ResponseEntity(new SingleResponse<>(response), HttpStatus.CREATED);      // 응답
    }

    // 댓글 수정
    @PatchMapping("/{board-id}/{comment-id}")
    public ResponseEntity patchBoardComment(HttpServletRequest request,
                                            @PathVariable("board-id")Long boardId,
                                                @PathVariable("comment-id")Long commentId,
                                                @Validated @RequestBody BoCommentDto.PostAndPatch patch,
                                                Principal principal) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 Token확인
        BoComment patchBoCommentFromPatchDto = boardCommentMapper.boCommentPostAndPatchDtoToBoCommentEntity(patch);   // patchDto로부터 생성된 객체
        BoComment updatedComment = boCommentService.updateComment(boardId, commentId, patchBoCommentFromPatchDto, principal.getName()); // 서비스 계층에서 엔티티를 update
        BoCommentDto.Response response = boardCommentMapper.boCommentEntityToBoCommentResponseDto(updatedComment);    // response로
        return ResponseEntity.ok(new SingleResponse<>(response));           // 응답
    }

    // 댓글 삭제
    @DeleteMapping("/{board-id}/{comment-id}")
    public ResponseEntity patchBoardComment(HttpServletRequest request,
                                            @PathVariable("board-id")Long boardId,
                                                @PathVariable("comment-id")Long commentId,
                                                Principal principal) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 Token확인
        boCommentService.removeBoComment(boardId, commentId, principal.getName());    // 서비스계층에서 삭제
        return new ResponseEntity(HttpStatus.NO_CONTENT); // 204번 코드
    }

    // 댓글 좋아요
    @PatchMapping("/{board-id}/{comment-id}/heart")
    public ResponseEntity likeBoardComment(HttpServletRequest request,
                                           @PathVariable("board-id")Long boardId,
                                           @PathVariable("comment-id")Long commentId,
                                           Principal principal) {
        tokenService.verificationLogOutToken(request); // 블랙리스트 Token확인
        BoComment likeBoComment = boCommentService.likeBoCommnet(boardId, commentId, principal.getName());
        BoCommentDto.Response boCommentResponse = boardCommentMapper.boCommentEntityToBoCommentResponseDto(likeBoComment);
        return ResponseEntity.ok(new SingleResponse<>(boCommentResponse));

    }
}
