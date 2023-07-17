package wanderhub.server.domain.mytrip_plan_detail.entity;

import lombok.*;
import wanderhub.server.domain.mytrip_plan.entity.MyTripPlan;
import wanderhub.server.global.audit.Auditable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyTripPlanDetail extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MY_TRIP_PLAN_DETAIL_ID")
    private Long myTripPlanDetailId;

    @Setter
    @Column(name = "SUBTITLE", length = 100, nullable = false)
    private String subTitle;

    @Lob
    @Setter
    @Column(name = "CONTENT")
    private String content;

    @Setter
    @Column(name = "COORDINATE_X")
    private Double coordinateX;

    @Setter
    @Column(name = "COORDINATE_Y")
    private Double coordinateY;

    @Setter
    @Column(name = "PLACE_NAME", length = 100)
    private String placeName;

    @Setter
    @Column(name = "WHEN_DATE")
    private LocalDate whenDate;

    @Setter
    @Column(name = "TIME_START")
    private LocalTime timeStart;

    @Setter
    @Column(name = "TIME_END")
    private LocalTime timeEnd;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MY_TRIP_PLAN_ID")
    private MyTripPlan myTripPlan;


    @Builder
    public MyTripPlanDetail(String subTitle, String content, Double coordinateX, Double coordinateY, String placeName, LocalDate whenDate, LocalTime timeStart, LocalTime timeEnd) {
        this.subTitle = subTitle;
        this.content = content;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.placeName = placeName;
        this.whenDate = whenDate;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }
}
