package de.telran.gardenStore.converter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ConverterEntityToDto<Entity, ResponseDto, ShortResponseDto>  {

    ResponseDto convertEntityToDto(Entity entity);

    List<ShortResponseDto> convertEntityListToDtoList(List<Entity> entities);

    static <ResponseDto, Entity> List<ResponseDto> convertList(List<Entity> entityList, Function<Entity, ResponseDto> converter) {
        return entityList.stream().map(converter).collect(Collectors.toList());
    }

}
