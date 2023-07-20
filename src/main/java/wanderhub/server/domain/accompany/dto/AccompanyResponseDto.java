package wanderhub.server.domain.accompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AccompanyResponseDto {
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
    private String content;
    private boolean recruitComplete;
    private Double coordinateX;
    private Double coordinateY;
    private String placeName;
    private List<String> joinMembers;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    @QueryProjection
    public AccompanyResponseDto(Long accompanyId, String nickname, String local, Long maxMemberNum, LocalDate accompanyStartDate, LocalDate accompanyEndDate, String title, String content, boolean recruitComplete, Double coordinateX, Double coordinateY, String placeName, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.accompanyId = accompanyId;
        this.nickname = nickname;
        this.local = local;
        this.maxMemberNum = maxMemberNum;
        this.accompanyStartDate = accompanyStartDate;
        this.accompanyEndDate = accompanyEndDate;
        this.title = title;
        this.content = content;
        this.recruitComplete = recruitComplete;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.placeName = placeName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public void setMemberList(List<String> memberNicknameList) {
        this.joinMembers = memberNicknameList;
        this.currentMemberNum = memberNicknameList.size();
    }
}
