package wanderhub.server.domain.accompany.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import wanderhub.server.global.utils.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AccompanySearchCondition {
    private String local;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String startDate;

    public Local getLocal() {
        return this.local == null ? null : Local.findByLocal(this.local);
    }

    public LocalDate getDate() {
        if(Objects.nonNull(startDate)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(startDate, formatter);
        }
        return null;
    }
}