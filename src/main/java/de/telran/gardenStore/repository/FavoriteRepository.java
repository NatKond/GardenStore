package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> getAllByUser(AppUser user);

    Optional<Favorite> findByUserAndFavoriteId(AppUser user, Long favoriteId);

    @Query("""
            SELECT f
            FROM Favorite f
            JOIN f.product p
            WHERE p.productId = :productId AND f.user = :user
            """)
    Optional<Favorite> findByUserIdAndProductId(AppUser user, Long productId);
}
