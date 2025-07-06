package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> getAllByUser(AppUser user);

    Optional<Favorite> findByUserAndProduct(AppUser user,Product product);
}
