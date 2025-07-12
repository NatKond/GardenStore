package de.telran.gardenStore.converter;

public interface Converter<Entity, RequestDto, ResponseDto, ShortResponseDto> extends ConverterDtoToEntity<Entity, RequestDto>,
        ConverterEntityToDto<Entity, ResponseDto, ShortResponseDto> {
//        <Entity, RequestDto, ResponseDto, ShortResponseDto> {
//
//    Entity convertDtoToEntity(RequestDto requestDto);
//
//    ResponseDto convertEntityToDto(Entity entity);
//
//    List<ShortResponseDto> convertEntityListToDtoList(List<Entity> entities);
//
//    static <ResponseDto, Entity> List<ResponseDto> convertList(List<Entity> entityList, Function<Entity, ResponseDto> converter) {
//        return entityList.stream().map(converter).collect(Collectors.toList());
//    }
}