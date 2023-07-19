package wanderhub.server.domain.mytrip_plan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MyTripPlanListResponseDto {
    private Long myTripPlanId;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate tripStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate tripEndDate;
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public MyTripPlanListResponseDto(Long myTripPlanId, String title, LocalDate tripStartDate, LocalDate tripEndDate, LocalDateTime createdAt) {
        this.myTripPlanId = myTripPlanId;
        this.title = title;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.createdAt = createdAt;
    }
}
