package de.telran.gardenStore.converter;

public interface ConverterEntityToDtoShort<Entity, ResponseDto> {

    ResponseDto toDto(Entity entity);
}
