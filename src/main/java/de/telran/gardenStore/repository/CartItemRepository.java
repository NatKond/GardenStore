package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

//    @Query("SELECT ci " +
//            "FROM CartItem ci " +
//            "JOIN ci.cart c " +
//            "JOIN c.user u " +
//            "WHERE c.user = :user AND ci.cartItemId = :cartItemId ")
//    Optional<CartItem> findByUserAndId(AppUser user, Long cartItemId);
}

