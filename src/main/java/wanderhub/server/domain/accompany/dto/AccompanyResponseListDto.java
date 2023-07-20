package wanderhub.server.domain.accompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 전체 조회용 Response
@Getter
@NoArgsConstructor
public class AccompanyResponseListDto {
    private Long accompanyId;
    private String nickname;
    private String local;
    private long currentMemberNum;
    private Long maxMemberNum;
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate accompanyStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate accompanyEndDate;
    private String title;
    private boolean recruitComplete;
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public AccompanyResponseListDto(Long accompanyId, String nickname, String local, long currentMemberNum, Long maxMemberNum, LocalDate accompanyStartDate, LocalDate accompanyEndDate, String title, boolean recruitComplete, LocalDateTime createdAt) {
        this.accompanyId = accompanyId;
        this.nickname = nickname;
        this.local = local;
        this.currentMemberNum = currentMemberNum;
        this.maxMemberNum = maxMemberNum;
        this.accompanyStartDate = accompanyStartDate;
        this.accompanyEndDate = accompanyEndDate;
        this.title = title;
        this.recruitComplete = recruitComplete;
        this.createdAt = createdAt;
    }
}