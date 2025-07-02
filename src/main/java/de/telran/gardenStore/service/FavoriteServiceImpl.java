package de.telran.gardenStore.service;

import de.telran.gardenStore.dto.FavoriteCreateRequestDto;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.exception.FavoriteNotFoundException;
import de.telran.gardenStore.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private ModelMapper modelMapper;

    @Override
    public List<FavoriteResponseDto> getAllFavorites() {
        return favoriteRepository.findAll()
                .stream()
                .map(favorite -> modelMapper
                        .map(favorite, FavoriteResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FavoriteResponseDto getFavoriteById(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(()
                -> new FavoriteNotFoundException("Favorite with id " + favoriteId + " not found"));
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @Override
    public FavoriteResponseDto createFavorite(FavoriteCreateRequestDto favoriteCreateRequestDto) {
        favorite = favoriteRepository.save(modelMapper.map(favoriteCreateRequestDto, Favorite.class));
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @Override
    public void deleteFavoriteById(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

}
