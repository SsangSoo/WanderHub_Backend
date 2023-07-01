package wanderhub.server.domain.accompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccompanyDto {

    public static class Post {
        private String accompanyLocal;
        //    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    private LocalDate accompanyDate;
        private String accompanyDate;
        private int maxNum;
        private String accompanyTitle;
        private String accompanyContent;
        private double coordX;
        private double coordY;
        private String placeTitle;

    }

    public static class ResponseDto {
        private Long id;
        private String nickname;
        private String accompanyLocal;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate accompanyDate;
        private int maxNum;
        private String accompanyTitle;
        private String accompanyContent;
        private boolean openStatus;
        private double coordX;
        private double coordY;
        private String placeTitle;
        private int registeredMembers;

        //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modifiedAt;
    }
}
