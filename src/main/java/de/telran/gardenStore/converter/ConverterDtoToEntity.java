package de.telran.gardenStore.converter;

public interface ConverterDtoToEntity<Entity, RequestDto> {

    Entity toEntity(RequestDto requestDto);
}
