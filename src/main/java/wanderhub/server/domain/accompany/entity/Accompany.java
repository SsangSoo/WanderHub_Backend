package wanderhub.server.domain.accompany.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.BeanWrapperImpl;
import wanderhub.server.domain.accompany.dto.AccompanyDto;
import wanderhub.server.domain.accompany_member.entity.AccompanyMember;
import wanderhub.server.global.audit.Auditable;
import wanderhub.server.global.utils.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Entity
@NoArgsConstructor
public class Accompany extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOMPANY_ID", updatable = false)
    private Long accompanyId;

    @Column(name = "ACCOMPANY_MAKER",length = 50, updatable = false)
    private String accompanyMaker;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "LOCAL", length = 16)    // ERD상 Not Null이지만, 기본 X(선택없음)로 들어가므로 nullable 표시 안함.
    private Local local;

    @Column(name = "MAX_MEMBER_COUNT", nullable = false)      // 최대인원
    private Long maxMemberCount;

    @Column(name = "ACCOMPANY_START_DATE")
    private LocalDate accompanyStartDate;        // 동행 시작 날짜

    @Column(name = "ACCOMPANY_END_DATE")
    private LocalDate accompanyEndDate;          // 동행 시작 날짜

    @Column(name = "TITLE", length = 100, nullable = false)
    private String title;

    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;

    @ColumnDefault("false")     // 기본값 false로 지정
    @Column(name = "RECRUIT_COMPLETE")    // 기본값 false이므로, Table상 Not Null이지만, nullable 포시 안 함.
    private boolean recruitComplete;   // 모집 완료 여부 // 모집 완료 되면 True // boolean 기본 false.

    @Column(name = "COORDINATE_X")
    private Double coordinateX;

    @Column(name = "COORDINATE_Y")
    private Double coordinateY;

    @Column(name = "PLACE_NAME", length = 50)
    private String placeName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accompany", orphanRemoval = true) // orphanRemoval 연관관계가 끊어지면 자동으로 삭제
    private List<AccompanyMember> accompanyMemberList = new ArrayList<>();



    @Builder
    public Accompany(String accompanyMaker, Local local, Long maxMemberCount, LocalDate accompanyStartDate, LocalDate accompanyEndDate, String title, String content, Double coordinateX, Double coordinateY, String placeName) {
        this.accompanyMaker = accompanyMaker;
        this.local = local;
        this.maxMemberCount = maxMemberCount;
        this.accompanyStartDate = accompanyStartDate;
        this.accompanyEndDate = accompanyEndDate;
        this.title = title;
        this.content = content;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.placeName = placeName;
    }

    public void updateAccompany(AccompanyDto.Patch accpmpanyPatchDto) {
        if(Objects.nonNull(accpmpanyPatchDto.getLocal())) this.local = Local.getLocal(accpmpanyPatchDto.getLocal());
        if(Objects.nonNull(accpmpanyPatchDto.getMaxMemberCount())) this.maxMemberCount = accpmpanyPatchDto.getMaxMemberCount();
        if(Objects.nonNull(accpmpanyPatchDto.getAccompanyStartDate())) this.accompanyStartDate = accpmpanyPatchDto.getAccompanyStartDate();
        if(Objects.nonNull(accpmpanyPatchDto.getAccompanyEndDate())) this.accompanyEndDate = accpmpanyPatchDto.getAccompanyEndDate();
        if(Objects.nonNull(accpmpanyPatchDto.getTitle())) this.title = accpmpanyPatchDto.getTitle();
        if(Objects.nonNull(accpmpanyPatchDto.getContent())) this.content = accpmpanyPatchDto.getContent();
        if(Objects.nonNull(accpmpanyPatchDto.getCoordinateX())) this.coordinateX = accpmpanyPatchDto.getCoordinateX();
        if(Objects.nonNull(accpmpanyPatchDto.getCoordinateY())) this.coordinateY = accpmpanyPatchDto.getCoordinateY();
        if(Objects.nonNull(accpmpanyPatchDto.getPlaceName())) this.placeName = accpmpanyPatchDto.getPlaceName();
    }


}
