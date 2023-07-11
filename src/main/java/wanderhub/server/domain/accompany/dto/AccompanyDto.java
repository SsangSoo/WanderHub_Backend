package wanderhub.server.domain.accompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class AccompanyDto {

    // 생성시
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post {
        private String local;
        @NotNull
        private Long maxMemberNum;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private Date accompanyStartDate;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private Date accompanyEndDate;
        @NotBlank
        private String title;
        @Lob
        @NotBlank
        private String content;
        private Double coordinateX;
        private Double coordinateY;
        private String placeName;
    }

    // Accompany 수정
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Patch {
        private String local;
        private Long maxMemberNum;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private Date accompanyStartDate;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private Date accompanyEndDate;
        private String title;
        @Lob
        private String content;
        private Double coordinateX;
        private Double coordinateY;
        private String placeName;
    }

    
    // 응답용
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String nickname;
        private String local;
        private long currentMemberNum;
        private Long maxMemberNum;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private Date accompanyStartDate;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private Date accompanyEndDate;
        private String title;
        private String content;
        private boolean recruitComplete;
        private Double coordinateX;
        private Double coordinateY;
        private String placeName;
        private List<String> joinMembers;
    }

    // 전체 조회용 Response
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ListResponse {
        private Long id;
        private String nickname;
        private String local;
        private long currentMemberNum;
        private Long maxMemberNum;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private Date accompanyStartDate;
        @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
        private Date accompanyEndDate;
        private String title;
        private boolean recruitComplete;
    }


}
