package wanderhub.server.domain.board_heart.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanderhub.server.domain.board.entity.Board;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.global.audit.Auditable;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class BoardHeart extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_HEART_ID")
    private Long boardHeartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Builder
    public BoardHeart(Member member, Board board) {
        this.member = member;
        this.board = board;
    }
}
