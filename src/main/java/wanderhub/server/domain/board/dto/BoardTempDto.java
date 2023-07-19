package wanderhub.server.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanderhub.server.domain.bo_comment.dto.BoCommentDto;
import wanderhub.server.domain.bo_comment.dto.BoCommentResponseDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class BoardTempDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long boardId;
        private String nickName;
        private String title;
        private String content;
        private String local;
        private long viewPoint; // 기본값 0 // null로 식별할 필요없기 때문에, 기본형
        private long likePoint; // 기본값 0 // 위와 마찬가지 이유.
        private List<BoCommentResponseDto> boComments;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    // 임시
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Long boardId;
        private String nickName;
        private String title;
        private String local;
        private long viewPoint; // 기본값 0 // null로 식별할 필요없기 때문에, 기본형
        private long likePoint; // 기본값 0 // 위와 마찬가지 이유.
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

}
