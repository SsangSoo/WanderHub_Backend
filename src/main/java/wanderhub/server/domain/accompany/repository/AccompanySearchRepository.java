package wanderhub.server.domain.accompany.repository;

import wanderhub.server.domain.accompany.dto.AccompanyResponseDto;
import wanderhub.server.domain.accompany.dto.AccompanyResponseListDto;
import wanderhub.server.domain.accompany.dto.AccompanySearchCondition;
import wanderhub.server.global.response.PageResponseDto;

import java.text.ParseException;
import java.util.List;

public interface AccompanySearchRepository {
    PageResponseDto<AccompanyResponseListDto> searchByLocalAndDate(AccompanySearchCondition accompanySearchDto, Integer page) throws ParseException;

    AccompanyResponseDto getAccompany(Long accompanyId);

    List<String> getAccompanyMemberList(Long accompanyId);
}
