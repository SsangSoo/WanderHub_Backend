package wanderhub.server.domain.mytrip_plan_detail.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(name = "SUBTITLE", length = 100, nullable = false)
    private String subTitle;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "COORDINATE_X")
    private Double coordinateX;

    @Column(name = "COORDINATE_Y")
    private Double coordinateY;

    @Column(name = "PLACE_NAME", length = 100)
    private String placeName;

    @Column(name = "WHEN_DATE")
    private LocalDate whenDate;

    @Column(name = "TIME_START")
    private LocalTime timeStart;

    @Column(name = "TIME_END")
    private LocalTime timeEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MY_TRIP_PLAN_ID")
    private MyTripPlan myTripPlan;

}
