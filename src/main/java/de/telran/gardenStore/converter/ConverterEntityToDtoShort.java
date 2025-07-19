package de.telran.gardenStore.converter;

public interface ConverterEntityToDtoShort<Entity, ResponseDto> {
    ResponseDto convertEntityToDto(Entity entity);
}
