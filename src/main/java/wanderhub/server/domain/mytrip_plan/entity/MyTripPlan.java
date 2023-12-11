package wanderhub.server.domain.mytrip_plan.entity;

import lombok.*;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail;
import wanderhub.server.global.audit.Auditable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class MyTripPlan extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MY_TRIP_PLAN_ID")
    private Long myTripPlanId;

    @Setter
    @Column(name = "TITLE", length = 100, nullable = false)
    private String title;

    @Setter
    @Column(name = "TRIP_START_DATE", nullable = false)
    private LocalDate tripStartDate;

    @Setter
    @Column(name = "TRIP_END_DATE",nullable = false)
    private LocalDate tripEndDate;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "myTripPlan", cascade = CascadeType.ALL, orphanRemoval = true) // orphanRemoval 연관관계가 끊어지면 자동으로 삭제
    private List<MyTripPlanDetail> myTripPlanDetailList = new ArrayList<>();

    @Builder
    public MyTripPlan(String title, LocalDate tripStartDate, LocalDate tripEndDate) {
        this.title = title;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
    }
}
