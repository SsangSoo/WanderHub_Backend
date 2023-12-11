package wanderhub.server.domain.accompany_member.entity;

import lombok.*;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.global.audit.Auditable;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class AccompanyMember extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOMPANY_MEMBER_ID")
    private Long accompanyMemberId;

    // Accompany가 삭제되면, AccompanyMember도 없어져야 함.
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ACCOMPANY_ID")
    private Accompany accompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public AccompanyMember(Accompany accompany, Member member) {
        this.accompany = accompany;
        this.member = member;
    }
}