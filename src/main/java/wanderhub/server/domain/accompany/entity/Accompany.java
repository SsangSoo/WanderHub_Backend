package wanderhub.server.domain.accompany.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;
import wanderhub.server.global.audit.Auditable;
import wanderhub.server.global.utils.Local;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accompany extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOMPANY_ID", updatable = false)
    private Long id;

    @Column(name = "NICKNAME",length = 50, updatable = false)
    private String nickname;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(name = "LOCAL", length = 16)    // ERD상 Not Null이지만, 기본 X(선택없음)로 들어가므로 nullable 표시 안함.
    private Local local;

    @Setter
    @Column(name = "CURRENT_MEMBER_NUM")    // 현재 인원
    private long currentMemberNum;           // 기본 0

    @Setter
    @Column(name = "MAX_MEMBER_NUM", nullable = false)      // 최대인원
    private Long maxMemberNum;

    @Setter
    @Column(name = "ACCOMPANY_START_DATE")
    private Date accompanyStartDate;        // 동행 시작 날짜

    @Setter
    @Column(name = "ACCOMPANY_END_DATE")
    private Date accompanyEndDate;          // 동행 시작 날짜

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

}
