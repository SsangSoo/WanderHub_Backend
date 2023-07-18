package wanderhub.server.domain.mytrip_plan_detail.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MyTripPlanDetailListResponseDto {
    private Long myTripPlanDetailId;
    private String subTitle;
    private String placeName;
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate whenDate;
    private LocalDateTime createdAt;

    @QueryProjection
    public MyTripPlanDetailListResponseDto(Long myTripPlanDetailId, String subTitle, String placeName, LocalDate whenDate, LocalDateTime createdAt) {
        this.myTripPlanDetailId = myTripPlanDetailId;
        this.subTitle = subTitle;
        this.placeName = placeName;
        this.whenDate = whenDate;
        this.createdAt = createdAt;
    }

}
