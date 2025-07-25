package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c JOIN ci.product p WHERE p.productId =:productId AND c.cartId =:cartId")
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    List<CartItem> findByCartCartId(Long cartId);
}

