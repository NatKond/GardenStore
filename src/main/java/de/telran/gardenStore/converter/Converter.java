package de.telran.gardenStore.converter;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Converter<Entity, RequestDto, ResponseDto, ShortResponseDto> {

    Entity convertDtoToEntity(RequestDto requestDto);

    ResponseDto convertEntityToDto(Entity entity);

    List<ShortResponseDto> convertEntityListToDtoList(List<Entity> entities);

    static <ResponseDto, Entity> List<ResponseDto> convertList(List<Entity> entityList, Function<Entity, ResponseDto> converter) {

        if (entityList == null) {
            return Collections.emptyList();
        }

        return entityList.stream().map(converter).collect(Collectors.toList());
    }
}