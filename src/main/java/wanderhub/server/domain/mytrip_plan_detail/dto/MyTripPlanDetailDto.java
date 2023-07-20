package wanderhub.server.domain.mytrip_plan_detail.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

public class MyTripPlanDetailDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        private String subTitle;
        @Lob
        private String content;
        private Double coordinateX;
        private Double coordinateY;
        private String placeName;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private LocalDate whenDate;
        @JsonFormat(pattern = "HH:mm")
        private LocalTime timeStart;
        @JsonFormat(pattern = "HH:mm")
        private LocalTime timeEnd;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        private String subTitle;
        @Lob
        private String content;
        private Double coordinateX;
        private Double coordinateY;
        private String placeName;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private LocalDate whenDate;
        @JsonFormat(pattern = "HH:mm")
        private LocalTime timeStart;
        @JsonFormat(pattern = "HH:mm")
        private LocalTime timeEnd;
    }
}
