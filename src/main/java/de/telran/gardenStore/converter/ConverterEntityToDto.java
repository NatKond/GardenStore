package de.telran.gardenStore.converter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ConverterEntityToDto<Entity, ResponseDto, ShortResponseDto> {

    ResponseDto toDto(Entity entity);

    List<ShortResponseDto> toDtoList(List<Entity> entities);

    static <ResponseDto, Entity> List<ResponseDto> toList(List<Entity> entityList, Function<Entity, ResponseDto> converter) {
        return entityList.stream().map(converter).collect(Collectors.toList());
    }
}
