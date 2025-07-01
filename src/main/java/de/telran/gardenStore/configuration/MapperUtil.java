package de.telran.gardenStore.configuration;

import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class MapperUtil {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        Converter<String, String> passwordToHash = new Converter<>() {
            public String convert(MappingContext<String, String> context) {
                return passwordEncoder.encode(context.getSource());
            }
        };

        modelMapper.typeMap(UserCreateRequestDto.class, AppUser.class).addMappings(
                mapper -> {
                    mapper
                            .using(passwordToHash)
                            .map(UserCreateRequestDto::getPassword, AppUser::setPasswordHash);
                });

        return modelMapper;
    }
}
