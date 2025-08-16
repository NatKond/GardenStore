package de.telran.gardenStore.converter;

public interface Converter<Entity, RequestDto, ResponseDto, ShortResponseDto> extends ConverterDtoToEntity<Entity, RequestDto>,
        ConverterEntityToDto<Entity, ResponseDto, ShortResponseDto> {

}