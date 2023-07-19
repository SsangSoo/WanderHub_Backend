package wanderhub.server.domain.mytrip_plan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import wanderhub.server.domain.mytrip_plan_detail.dto.MyTripPlanDetailListResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MyTripPlanResponseDto {
    private Long myTripPlanId;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate tripStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate tripEndDate;
    @Setter
    List<MyTripPlanDetailListResponseDto> myTripPlanDetailResponseDtoList = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    @QueryProjection
    public MyTripPlanResponseDto(Long myTripPlanId, String title, LocalDate tripStartDate, LocalDate tripEndDate, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.myTripPlanId = myTripPlanId;
        this.title = title;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
