package wanderhub.server.domain.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardListResponseDto {

    private Long boardId;
    private String nickName;
    private String title;
    private String local;
    private long viewPoint; // 기본값 0 // null로 식별할 필요없기 때문에, 기본형
    private long likePoint; // 기본값 0 // 위와 마찬가지 이유.
    private LocalDateTime createdAt;



    @Builder
    @QueryProjection
    public BoardListResponseDto(Long boardId, String nickName, String title, String local, long viewPoint, long likePoint, LocalDateTime createdAt) {
        this.boardId = boardId;
        this.nickName = nickName;
        this.title = title;
        this.local = local;
        this.viewPoint = viewPoint;
        this.likePoint = likePoint;
        this.createdAt = createdAt;
    }
}
