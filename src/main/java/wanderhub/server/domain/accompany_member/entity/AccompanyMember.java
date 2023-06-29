package wanderhub.server.domain.accompany_member.entity;

import lombok.*;
import wanderhub.server.domain.accompany.entity.Accompany;
import wanderhub.server.domain.member.entity.Member;
import wanderhub.server.global.audit.Auditable;

import javax.persistence.*;

@Builder
@Getter
//@Setter   // 쓰는 곳 없어서 주석처리
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccompanyMember extends Auditable {    // Auditable 추가 // 날짜 null로 나옴.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOMPANY_ID", updatable = false)
    private Accompany accompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", updatable = false)
    private Member member;

}
