package wanderhub.server.domain.community.dto;

import lombok.*;
import wanderhub.server.domain.community_comment.dto.BoCommentDto;
import wanderhub.server.domain.community_comment.entity.BoComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanderhub.server.global.utils.Local;
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
        private Long viewPoint;
        //        private Long likePoint;
        @Setter
        private List<BoCommentDto.Response> boComments;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

}
