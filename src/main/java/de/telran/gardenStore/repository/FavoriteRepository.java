package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> getAllByUserId(Long userId);

    Optional<Favorite> findByUserIdAndProductId(Long userId, Long productId);
}
