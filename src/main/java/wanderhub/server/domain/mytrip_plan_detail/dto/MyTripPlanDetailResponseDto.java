package wanderhub.server.domain.mytrip_plan_detail.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import javax.persistence.Lob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class MyTripPlanDetailResponseDto {
    private Long myTripPlanDetailId;
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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @QueryProjection

    public MyTripPlanDetailResponseDto(Long myTripPlanDetailId, String subTitle, String content, Double coordinateX, Double coordinateY, String placeName, LocalDate whenDate, LocalTime timeStart, LocalTime timeEnd, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.myTripPlanDetailId = myTripPlanDetailId;
        this.subTitle = subTitle;
        this.content = content;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.placeName = placeName;
        this.whenDate = whenDate;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
