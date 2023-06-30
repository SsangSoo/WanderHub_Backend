package wanderhub.server.domain.community_comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wanderhub.server.domain.community_comment.dto.BoCommentDto;
import wanderhub.server.domain.community_comment.entity.BoComment;
import wanderhub.server.domain.community_comment.mapper.BoardCommentMapper;
import wanderhub.server.domain.community_comment.service.BoCommentService;
import wanderhub.server.global.response.SingleResponse;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/v1/community/comment")
public class BoCommentController {

    private final BoardCommentMapper mapper;
    private final BoCommentService boCommentService;

    public BoCommentController(BoardCommentMapper mapper, BoCommentService boCommentService) {
        this.mapper = mapper;
        this.boCommentService = boCommentService;
    }

    // 댓글 생성
    @PostMapping("/{community-id}")
    public ResponseEntity postCommunityComment(Principal principal,
                                               @PathVariable("community-id")Long communityId,
                                               @Validated @RequestBody BoCommentDto.WriteDto post) {
        BoComment boComment = mapper.boCommentWriteDtoToBoComment(post);    // Dto로부터 엔티티를 만들고,
        BoComment createdComment = boCommentService.createComment(communityId, boComment, principal.getName()); // 서비스계층에서 엔티티를 만든 후,
        BoCommentDto.Response response = mapper.boCommentToBoCommentDtoResponse(createdComment);    // resposne 객체로 만들어서
        return new ResponseEntity(new SingleResponse<>(response), HttpStatus.CREATED);      // 응답
    }

    // 댓글 수정
    @PatchMapping("/{community-id}/{comment-id}")
    public ResponseEntity patchCommunityComment(@PathVariable("community-id")Long communityId,
                                                @PathVariable("comment-id")Long commentId,
                                                @Validated @RequestBody BoCommentDto.WriteDto patch,
                                                Principal principal) {
        BoComment boComment = mapper.boCommentWriteDtoToBoComment(patch);   // Dto로부터 엔티티를 만들고,
        BoComment updatedComment = boCommentService.updateComment(communityId, commentId, boComment, principal.getName()); // 서비스 계층에서 엔티티를 update해준 후,
        BoCommentDto.Response response = mapper.boCommentToBoCommentDtoResponse(updatedComment);    // response 객체로 만들어서
        return ResponseEntity.ok(new SingleResponse<>(response));           // 응답
    }

    // 댓글 삭제
    @DeleteMapping("/{community-id}/{comment-id}")
    public ResponseEntity patchCommunityComment(@PathVariable("community-id")Long communityId,
                                                @PathVariable("comment-id")Long commentId,
                                                Principal principal) {
        boCommentService.removeBoComment(communityId, commentId, principal.getName());    // 서비스계층에서 삭제
        return new ResponseEntity(HttpStatus.NO_CONTENT); // 204번 코드
    }
}
