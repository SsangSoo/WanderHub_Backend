package wanderhub.server.domain.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import wanderhub.server.domain.bo_comment.dto.BoCommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long boardId;
    private String nickName;
    private String title;
    private String content;
    private String local;
    private long viewPoint; // 기본값 0 // null로 식별할 필요없기 때문에, 기본형
    private long likePoint; // 기본값 0 // 위와 마찬가지 이유.
    @Setter
    private List<BoCommentResponseDto> boComments;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    @QueryProjection
    public BoardResponseDto(Long boardId, String nickName, String title, String content, String local, long viewPoint, long likePoint, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.boardId = boardId;
        this.nickName = nickName;
        this.title = title;
        this.content = content;
        this.local = local;
        this.viewPoint = viewPoint;
        this.likePoint = likePoint;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
