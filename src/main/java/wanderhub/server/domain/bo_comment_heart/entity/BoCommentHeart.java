package wanderhub.server.domain.bo_comment_heart.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanderhub.server.domain.bo_comment.entity.BoComment;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.global.audit.Auditable;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoCommentHeart extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BO_COMMENT_HEART_ID")
    private Long boCommentHeartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOCOMMENT_ID")
    private BoComment boComment;

    @Builder
    public BoCommentHeart(Member member, BoComment boComment) {
        this.member = member;
        this.boComment = boComment;
    }
}
