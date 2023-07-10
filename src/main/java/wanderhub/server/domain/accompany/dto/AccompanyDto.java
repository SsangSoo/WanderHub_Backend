package wanderhub.server.domain.accompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class AccompanyDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Post {
        @NotNull
        private Long maxMemberNum;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private Date accompanyStartDate;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private Date accompanyEndDate;
        @NotBlank
        private String title;
        @Lob
        @NotBlank
        private String content;
        private Double coordinateX;
        private Double coordinateY;
        private String placeName;
    }

}
