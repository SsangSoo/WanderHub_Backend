package wanderhub.server.domain.accompany.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import wanderhub.server.domain.accompany.dto.*;
import wanderhub.server.domain.accompany.dto.QAccompanyResponseDto;
import wanderhub.server.domain.accompany.dto.QAccompanyResponseListDto;
import wanderhub.server.domain.accompany_member.entity.QAccompanyMember;
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
public class AccompanySearchRepositoryImpl implements AccompanySearchRepository {

    private final JPAQueryFactory queryFactory;

    public AccompanySearchRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

//
    @Override
    public PageResponseDto<AccompanyResponseListDto> searchByLocalAndDate(AccompanySearchCondition accompanySearchDto, Integer page) throws ParseException {
        List<AccompanyResponseListDto> accompanyDtoList;
        accompanyDtoList = queryFactory
                .select(new QAccompanyResponseListDto(
                        accompany.accompanyId.as("accompanyId"),
                        accompany.nickname,
                        accompany.local.stringValue(),
                        accompany.accompanyMemberList.size().longValue(),
                        accompany.maxMemberNum,
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
                .offset(page*10-10)
                .limit(10)
                .fetch();

        Long totalElements = queryFactory
                .select(accompany.count())
                .from(accompany)
                .where(
                        localEq(accompanySearchDto.getLocal()),
                        startDateEq(accompanySearchDto.getDate())
                ).fetchOne();

        Long totalPage = (totalElements/10) + (totalElements%10 > 0 ? 1 : 0);  // totalPage

        // 마지막 페이지보다 작으면 10 아니라면, 총 요소갯수에서 % 10(페이지 사이즈)
        Long currentPageElements = page < totalPage ? 10 : totalElements % 10;

        return PageResponseDto.of(accompanyDtoList, totalPage, totalElements, currentPageElements, page);
    }


    @Override
    public AccompanyResponseDto getAccompany(Long accompanyId) {
        AccompanyResponseDto accompanyResponseDto;
        accompanyResponseDto = queryFactory
                .select(new QAccompanyResponseDto(
                        accompany.accompanyId.as("accompanyId"),
                        accompany.nickname,
                        accompany.local.stringValue(),
                        accompany.maxMemberNum,
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
                .select(accompanyMember.member.nickName)
                .from(accompanyMember)
                .where(accompanyMember.accompany.accompanyId.eq(accompanyId))
                .fetch();

        accompanyResponseDto.setMemberList(accompanyMemberList);
        return accompanyResponseDto;

    }


    private BooleanExpression localEq(Local local) {
        return Objects.nonNull(local) ? accompany.local.eq(local) : null;
    }

    private BooleanExpression startDateEq(LocalDate startDate) {
        return Objects.nonNull(startDate) ? accompany.accompanyStartDate.eq(startDate) : null;
    }
}
