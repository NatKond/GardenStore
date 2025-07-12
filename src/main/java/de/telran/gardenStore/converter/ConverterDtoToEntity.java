package de.telran.gardenStore.converter;

public interface ConverterDtoToEntity <Entity, RequestDto>{

    Entity convertDtoToEntity(RequestDto requestDto);
}
