package wanderhub.server.domain.mytrip_plan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class MyTripPlanDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post {
        @NotBlank
        private String title;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private LocalDate tripStartDate;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private LocalDate tripEndDate;
    }

    @Getter
    public static class Patch {
        private String title;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private LocalDate tripStartDate;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private LocalDate tripEndDate;
    }
}
