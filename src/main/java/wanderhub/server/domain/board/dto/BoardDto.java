package wanderhub.server.domain.board.dto;

import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanderhub.server.domain.bo_comment.dto.BoCommentDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class BoardDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        private String local;;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        private String title;
        private String content;
        private String local;
    }

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
        @Setter
        private List<BoCommentDto.Response> boComments;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

}