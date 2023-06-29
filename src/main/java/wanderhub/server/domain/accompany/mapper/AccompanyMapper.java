package wanderhub.server.domain.accompany.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import wanderhub.server.domain.accompany.dto.AccompanyDto;
import wanderhub.server.domain.accompany.dto.AccompanyResponseDto;
import wanderhub.server.domain.accompany.entity.Accompany;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccompanyMapper {

    AccompanyMapper INSTANCE = Mappers.getMapper(AccompanyMapper.class);

    //Entity -> ResponseDto
    AccompanyResponseDto toDto(Accompany entity);
    List<AccompanyResponseDto> toDtoList(List<Accompany> entityList);

    //Entity -> RequestDto
    AccompanyDto toRequestDto(Accompany entity); //테스트코드 때문에 생성
    List<AccompanyDto> toRequestDtoList(List<Accompany> entityList); //테스트코드 때문에 생성

    //RequestDto -> Entity
    Accompany toEntity(AccompanyDto dto);
    List<Accompany> toEntityList(List<AccompanyDto> dtoList);

    //ResponseDto -> Entity
    Accompany fromResponseDtotoEntity(AccompanyResponseDto dto); //controller test 때문에 생성
    List<Accompany> fromResponseDtotoEntityList(List<AccompanyResponseDto> dtoList); //controller test 때문에 생성

}