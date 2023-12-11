package wanderhub.server.domain.accompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AccompanySingleResponseVO {
    private Long accompanyId;
    private String accompanyMaker;
    private String local;
    private Long maxMemberCount;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modifiedAt;
    private List<String> memberList;
    private int currentMemberCount;

    @Builder
    public AccompanySingleResponseVO(AccompanySingleResponseDTO accompanySingleResponseDTO, List<String> memberList) {
        this.accompanyId = accompanySingleResponseDTO.getAccompanyId();
        this.accompanyMaker = accompanySingleResponseDTO.getAccompanyMaker();
        this.local = accompanySingleResponseDTO.getLocal().getLocalString();
        this.maxMemberCount = accompanySingleResponseDTO.getMaxMemberCount();
        this.accompanyStartDate = accompanySingleResponseDTO.getAccompanyStartDate();
        this.accompanyEndDate = accompanySingleResponseDTO.getAccompanyEndDate();
        this.title = accompanySingleResponseDTO.getTitle();
        this.content = accompanySingleResponseDTO.getContent();
        this.recruitComplete = accompanySingleResponseDTO.isRecruitComplete();
        this.coordinateX = accompanySingleResponseDTO.getCoordinateX();
        this.coordinateY = accompanySingleResponseDTO.getCoordinateY();
        this.placeName = accompanySingleResponseDTO.getPlaceName();
        this.createdAt = accompanySingleResponseDTO.getCreatedAt();
        this.modifiedAt = accompanySingleResponseDTO.getModifiedAt();
        this.memberList = memberList;
        this.currentMemberCount = memberList.size();
    }

}
