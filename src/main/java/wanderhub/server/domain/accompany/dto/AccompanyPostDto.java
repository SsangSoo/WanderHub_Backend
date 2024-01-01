package wanderhub.server.domain.accompany.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class AccompanyPostDto {

    private String local;
    @NotNull
    private Long maxMemberCount;
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate accompanyStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd") // 2022-10-02
    private LocalDate accompanyEndDate;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private Double coordinateX;
    private Double coordinateY;
    private String placeName;

}
