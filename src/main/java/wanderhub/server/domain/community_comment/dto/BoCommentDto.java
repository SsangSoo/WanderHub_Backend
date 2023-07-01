package wanderhub.server.domain.community_comment.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class BoCommentDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostAndPatch {
        @NotBlank
        @Lob
        private String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long boCommentId;
        private Long boardId;
        private String nickName;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }



}
