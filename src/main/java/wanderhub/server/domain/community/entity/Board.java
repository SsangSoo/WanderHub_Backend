package wanderhub.server.domain.community.entity;

import lombok.*;
import wanderhub.server.domain.community_comment.entity.BoComment;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.global.audit.Auditable;
import wanderhub.server.global.utils.Local;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Board extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long boardId;

    @Column(name = "NICKNAME", length = 50)
    @Setter
    private String nickName;    // 작성자

    @Column(name = "TITLE", length = 100)
    @Setter
    private String title;

    @Lob
    @Column(name = "CONTENT")
    @Setter
    private String content;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "LOCAL")
    @Setter
    private Local local;


    @Column(name = "VIEW_POINT")
    @Setter
    private Long viewPoint;

    // 좋아요 추가하면서 좋아요 수 넣기
//////


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    @Setter
    private Member member;


    // 댓글
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board", orphanRemoval = true) // orphanRemoval 연관관계가 끊어지면 자동으로 삭제
    private List<BoComment> boComments = new ArrayList<>();

    @Builder
    public Board(String title, String content, Local local) {
        this.title = title;
        this.content = content;
        this.local = local;
    }
}
