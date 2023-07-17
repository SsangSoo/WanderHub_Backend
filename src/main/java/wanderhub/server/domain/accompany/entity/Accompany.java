package wanderhub.server.domain.accompany.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;
import wanderhub.server.global.audit.Auditable;
import wanderhub.server.global.utils.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Accompany extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOMPANY_ID", updatable = false)
    private Long accompanyId;

    @Column(name = "NICKNAME",length = 50, updatable = false)
    private String nickname;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(name = "LOCAL", length = 16)    // ERD상 Not Null이지만, 기본 X(선택없음)로 들어가므로 nullable 표시 안함.
    private Local local;

    @Setter
    @Column(name = "MAX_MEMBER_NUM", nullable = false)      // 최대인원
    private Long maxMemberNum;

    @Setter
    @Column(name = "ACCOMPANY_START_DATE")
    private LocalDate accompanyStartDate;        // 동행 시작 날짜

    @Setter
    @Column(name = "ACCOMPANY_END_DATE")
    private LocalDate accompanyEndDate;          // 동행 시작 날짜

    @Setter
    @Column(name = "TITLE", length = 100, nullable = false)
    private String title;

    @Lob
    @Setter
    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Setter
    @ColumnDefault("false")     // 기본값 false로 지정
    @Column(name = "RECRUIT_COMPLETE")    // 기본값 false이므로, Table상 Not Null이지만, nullable 포시 안 함.
    private boolean recruitComplete;   // 모집 완료 여부 // 모집 완료 되면 True // boolean 기본 false.

    @Setter
    @Column(name = "COORDINATE_X")
    private Double coordinateX;

    @Setter
    @Column(name = "COORDINATE_Y")
    private Double coordinateY;

    @Setter
    @Column(name = "PLACE_NAME", length = 50)
    private String placeName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accompany", orphanRemoval = true) // orphanRemoval 연관관계가 끊어지면 자동으로 삭제
    private List<AccompanyMember> accompanyMemberList = new ArrayList<>();

    public void setAccompanyInit(String nickname) {
        this.nickname = nickname;
    }
    

    @Builder
    public Accompany(Local local, Long maxMemberNum, LocalDate accompanyStartDate, LocalDate accompanyEndDate, String title, String content, Double coordinateX, Double coordinateY, String placeName) {
        this.local = local;
        this.maxMemberNum = maxMemberNum;
        this.accompanyStartDate = accompanyStartDate;
        this.accompanyEndDate = accompanyEndDate;
        this.title = title;
        this.content = content;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.placeName = placeName;
    }
}
