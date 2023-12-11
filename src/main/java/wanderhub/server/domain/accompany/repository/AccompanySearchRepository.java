package wanderhub.server.domain.accompany.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany.dto.*;
import wanderhub.server.domain.accompany.dto.QAccompanySingleResponseDTO;
import wanderhub.server.domain.accompany.dto.QAccompanyListResponseDto;
import wanderhub.server.global.response.PageResponseDto;
import wanderhub.server.global.utils.Local;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static wanderhub.server.domain.accompany.entity.QAccompany.accompany;
import static wanderhub.server.domain.accompany_member.entity.QAccompanyMember.*;


@Repository
public class AccompanySearchRepository  {

    private final JPAQueryFactory queryFactory;

    public AccompanySearchRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public PageResponseDto<AccompanyListResponseDto> searchByLocalAndDate(AccompanySearchCondition accompanySearchDto, Integer currentPage) throws ParseException {
        List<AccompanyListResponseDto> accompanyDtoList;

        accompanyDtoList = queryFactory
                .select(new QAccompanyListResponseDto(
                        accompany.accompanyId.as("accompanyId"),
                        accompany.accompanyMaker,
                        accompany.local.stringValue(),
                        accompany.accompanyMemberList.size(),
                        accompany.maxMemberCount,
                        accompany.accompanyStartDate,
                        accompany.accompanyEndDate,
                        accompany.title,
                        accompany.recruitComplete,
                        accompany.createdAt))
                .from(accompany)
                .where(
                        localEq(accompanySearchDto.getLocal()),
                        startDateEq(accompanySearchDto.getDate())
                )
                .offset(currentPage*10-10)
                .limit(10)
                .fetch();

        Long totalElements = queryFactory
                .select(accompany.count())
                .from(accompany)
                .where(
                        localEq(accompanySearchDto.getLocal()),
                        startDateEq(accompanySearchDto.getDate())
                ).fetchOne(); // 총 요소 개수

        Long totalPage = (totalElements/10) + (totalElements%10 > 0 ? 1 : 0);  // totalPage



        return PageResponseDto.of(accompanyDtoList, totalPage, currentPage);
    }


    public AccompanySingleResponseVO getAccompany(Long accompanyId) {
        AccompanySingleResponseDTO accompanySingleResponseDTO;
        accompanySingleResponseDTO = queryFactory
                .select(new QAccompanySingleResponseDTO(
                        accompany.accompanyId.as("accompanyId"),
                        accompany.accompanyMaker,
                        accompany.local,
                        accompany.maxMemberCount,
                        accompany.accompanyStartDate,
                        accompany.accompanyEndDate,
                        accompany.title,
                        accompany.content,
                        accompany.recruitComplete,
                        accompany.coordinateX,
                        accompany.coordinateY,
                        accompany.placeName,
                        accompany.createdAt,
                        accompany.modifiedAt
                ))
                .from(accompany)
                .where(accompany.accompanyId.eq(accompanyId))
                .fetchOne();


        List<String> accompanyMemberList;
        accompanyMemberList = queryFactory
                .select(accompanyMember.member.nickname)
                .from(accompanyMember)
                .where(accompanyMember.accompany.accompanyId.eq(accompanyId))
                .fetch();

        return new AccompanySingleResponseVO(accompanySingleResponseDTO, accompanyMemberList);

    }


    private BooleanExpression localEq(Local local) {
        return Objects.nonNull(local) ? accompany.local.eq(local) : null;
    }

    private BooleanExpression startDateEq(LocalDate startDate) {
        return Objects.nonNull(startDate) ? accompany.accompanyStartDate.eq(startDate) : null;
    }
}
