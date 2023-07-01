package wanderhub.server.domain.accompany.mapper;

import org.mapstruct.Mapper;
import wanderhub.server.domain.accompany.dto.AccompanyDto;
import wanderhub.server.domain.accompany.entity.Accompany;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccompanyMapper {
    AccompanyDto.Response accompanyEntityToAccompanyResponseDto(Accompany entity);
    List<AccompanyDto.Response> accompanyListToAccompanyResponseDtoList(List<Accompany> entityList);

    Accompany accompanyPostDtoToAccompanyEntity(AccompanyDto.Post accompanyPostDto);
}