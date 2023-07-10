package wanderhub.server.domain.accompany_member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.global.audit.Auditable;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccompanyMember extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOMPANY_MEMBER_ID")
    private Long accompanyMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOMPANY_ID")
    private Accompany accompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

}
