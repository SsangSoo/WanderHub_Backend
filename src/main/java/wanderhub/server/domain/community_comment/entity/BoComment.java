package wanderhub.server.domain.community_comment.entity;

import lombok.*;
import wanderhub.server.domain.community.entity.Board;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.global.audit.Auditable;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BoComment extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOCOMMENT_ID")
    private Long boCommentId;

    @Column(name = "NICKNAME",length = 50, nullable = false, updatable = false)
    @Setter
    private String nickName;

    @Column(name = "CONTENT", nullable = false)
    @Lob
    @Setter
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    @Setter
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    @Setter
    private Board board;

    @Builder
    public BoComment(String content, String nickName) {
        this.nickName = nickName;
        this.content = content;
    }
}
