package wanderhub.server.domain.bo_comment.entity;

import lombok.*;
import wanderhub.server.domain.bo_comment_heart.entity.BoCommentHeart;
import wanderhub.server.domain.board.entity.Board;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.global.audit.Auditable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BoComment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOCOMMENT_ID")
    private Long boCommentId;

    @Column(name = "NICKNAME", length = 50, nullable = false, updatable = false)
    private String nickName;

    @Column(name = "CONTENT", nullable = false)
    @Lob
    @Setter
    private String content;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "boComment", orphanRemoval = true)
    private List<BoCommentHeart> boCommentHeartList = new ArrayList();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    public void setBoardInit(Board board, Member writer) {
        this.board = board;
        this.nickName = writer.getNickName();
        this.member = writer;
    }
}
