package wanderhub.server.domain.bo_comment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoCommentResponseDto {
    private Long boCommentId;
    private Long boardId;
    private String nickName;
    private String content;
    private long likePoint;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    @QueryProjection
    public BoCommentResponseDto(Long boCommentId, Long boardId, String nickName, String content, long likePoint, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.boCommentId = boCommentId;
        this.boardId = boardId;
        this.nickName = nickName;
        this.content = content;
        this.likePoint = likePoint;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

}