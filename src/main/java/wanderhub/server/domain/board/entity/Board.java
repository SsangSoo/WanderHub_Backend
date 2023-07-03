package wanderhub.server.domain.board.entity;

import lombok.*;
import wanderhub.server.domain.bo_comment.entity.BoComment;
import wanderhub.server.domain.board_heart.entity.BoardHeart;
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

    @Column(name = "NICKNAME", length = 50, nullable = false)
    @Setter
    private String nickName;    // 작성자

    @Column(name = "TITLE", length = 100, nullable = false)
    @Setter
    private String title;

    @Lob
    @Column(name = "CONTENT", nullable = false)
    @Setter
    private String content;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "LOCAL", length = 16)
    @Setter
    private Local local;


    @Column(name = "VIEW_POINT")
    @Setter
    private long viewPoint;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board", orphanRemoval = true)
    private List<BoardHeart> boardHeartList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    @Setter
    private Member member;


    // 댓글
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board", orphanRemoval = true) // orphanRemoval 연관관계가 끊어지면 자동으로 삭제
    private List<BoComment> boCommentList = new ArrayList<>();

    @Builder
    public Board(String title, String content, Local local) {
        this.title = title;
        this.content = content;
        this.local = local;
    }
}
