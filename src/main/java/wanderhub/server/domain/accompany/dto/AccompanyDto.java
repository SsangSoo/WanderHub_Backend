package wanderhub.server.domain.accompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


public class AccompanyDto {

    // 생성시
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Post {
        private String local;
        @NotNull
        private Long maxMemberCount;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private LocalDate accompanyStartDate;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private LocalDate accompanyEndDate;
        @NotBlank
        private String title;
        @Lob
        @NotBlank
        private String content;
        private Double coordinateX;
        private Double coordinateY;
        private String placeName;

    }

    // Accompany 수정
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Patch {
        private String local;
        private Long maxMemberCount;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private LocalDate accompanyStartDate;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private LocalDate accompanyEndDate;
        private String title;
        @Lob
        private String content;
        private Double coordinateX;
        private Double coordinateY;
        private String placeName;
    }
}