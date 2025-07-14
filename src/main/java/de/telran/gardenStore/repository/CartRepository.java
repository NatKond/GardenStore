package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(AppUser user);

//    @Query("SELECT c FROM Cart c JOIN c.user u WHERE u.userId =:productId")
//    Optional<Cart> findByUserId(Long userId);
}

