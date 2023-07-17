package wanderhub.server.domain.mytrip_plan.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.domain.mytrip_plan_detail.entity.MyTripPlanDetail;
import wanderhub.server.global.audit.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class MyTripPlan extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MY_TRIP_PLAN_ID")
    private Long myTripPlanId;

    @Column(name = "TITLE", length = 100, nullable = false)
    private String title;

    @Column(name = "TRIP_START_DATE", nullable = false)
    private LocalDateTime tripStartDate;

    @Column(name = "TRIP_END_DATE",nullable = false)
    private LocalDateTime tripEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "myTripPlan", orphanRemoval = true) // orphanRemoval 연관관계가 끊어지면 자동으로 삭제
    private List<MyTripPlanDetail> myTripPlanDetailList = new ArrayList<>();

}
