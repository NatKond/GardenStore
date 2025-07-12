package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c JOIN c.user u WHERE u.userId =:productId")
    Optional<Cart> findByUserId(Long userId);
}

