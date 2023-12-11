package wanderhub.server.domain.member.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;
import wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart;
import wanderhub.server.domain.board.entity.Board;
import wanderhub.server.domain.bo_comment.entity.BoComment;
import wanderhub.server.domain.board_heart.entity.BoardHeart;
import wanderhub.server.domain.mytrip_plan.entity.MyTripPlan;
import wanderhub.server.global.audit.Auditable;
import wanderhub.server.global.utils.Local;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Member extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID", updatable = false)
    private Long Id;

    @Setter
    @Column(name = "NAME", length = 50)
    private String name;

    @Column(name = "EMAIL", length = 50, nullable = false, updatable = false)
    private String email;

    @Setter
    @Column(name = "NICKNAME", length = 50)
    private String nickname;

    @Lob
    @Column(name = "IMG_URL")
    private String imgUrl;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(name = "LOCAL", length = 16)
    private Local local;

    @Setter
    @ElementCollection
    private List<String> roles = new ArrayList<>();

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(name = "MEMBER_STATUS", length = 16)
    private MemberStatus memberStatus;

    @Setter
    @ColumnDefault("false")     // 기본값 false로 지정
    @Column(name = "NEWBIE")
    private boolean newbie;     // boolean 기본값 false

    @OneToMany(mappedBy = "member")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<BoComment> boCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<BoardHeart> boardHeartList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<BoCommentHeart> boCommentHeartList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<AccompanyMember> accompanyMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MyTripPlan> myTripPlanList = new ArrayList<>();


    public Member(String email, Boolean newbie) {   // 이메일로 멤버 생성
        this.email = email;
        this.newbie = newbie;
    }

    @Builder
    public Member(String name, String nickname, String imgUrl, Local local) {
        this.name = name;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.local = local;
    }

}
