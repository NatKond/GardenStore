package de.telran.gardenStore.service;

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

    //After the Installation of the Ultimate version check if
    // the annotation @RequiredArgsConstructor works correctly
    public FavoriteServiceImpl(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public List<FavoriteResponseDto> getAllFavorites() {
        return favoriteRepository.findAll()
                .stream()
                .map(favorite -> modelMapper
                        .map(favorite, FavoriteResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FavoriteResponseDto getFavoriteById(Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(()
                -> new FavoriteNotFoundException("Favorite with id " + id + " not found"));
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @Override
    public FavoriteResponseDto createFavorite(AppUser user) {
        Favorite favorite = new Favorite();
        favorite.setUserId(user.getUserId);
        favorite = favoriteRepository.save(favorite);
        return modelMapper.map(favorite, FavoriteResponseDto.class);
    }

    @Override
    public void deleteFavoriteById(Long id) {
        favoriteRepository.deleteById(id);
    }

}
