package wanderhub.server.domain.accompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanderhub.server.global.utils.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AccompanySingleResponseDTO {
    private Long accompanyId;           //
    private String accompanyMaker;      //
    private Local local;               //
    private Long maxMemberCount;           //
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate accompanyStartDate;   //
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate accompanyEndDate;     //
    private String title;               //
    private String content;             //
    private boolean recruitComplete;    //
    private Double coordinateX;         //
    private Double coordinateY;         //
    private String placeName;           //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modifiedAt;   //

    @Override
    public String toString() {
        return "AccompanySingleResponseDTO{" +
                "accompanyId=" + accompanyId +
                ", accompanyMaker='" + accompanyMaker + '\'' +
                ", local=" + local +
                ", maxMemberCount=" + maxMemberCount +
                ", accompanyStartDate=" + accompanyStartDate +
                ", accompanyEndDate=" + accompanyEndDate +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", recruitComplete=" + recruitComplete +
                ", coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                ", placeName='" + placeName + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

    @Builder
    @QueryProjection
    public AccompanySingleResponseDTO(Long accompanyId, String accompanyMaker, Local local, Long maxMemberCount, LocalDate accompanyStartDate, LocalDate accompanyEndDate, String title, String content, boolean recruitComplete, Double coordinateX, Double coordinateY, String placeName, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.accompanyId = accompanyId;
        this.accompanyMaker = accompanyMaker;
        this.local = local;
        this.maxMemberCount = maxMemberCount;
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
}

