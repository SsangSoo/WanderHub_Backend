package wanderhub.server.domain.accompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.time.LocalDate;


@Getter
@NoArgsConstructor
public class AccompanyPatchDto {
    private String local;
    private Long maxMemberCount;
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate accompanyStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate accompanyEndDate;
    private String title;
    @Lob
    private String content;
    private Double coordinateX;
    private Double coordinateY;
    private String placeName;
}
