package wanderhub.server.domain.member.entity;

import lombok.Getter;
public enum MemberStatus {
    ACTIVE("활동 중"),
    HUMAN("휴먼 상태");  //  휴먼 상태

    @Getter
    private String status;

    MemberStatus(String status) {
        this.status = status;
    }
}
