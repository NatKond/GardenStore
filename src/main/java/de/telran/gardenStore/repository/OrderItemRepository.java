package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
             SELECT oi
             FROM OrderItem oi
             JOIN oi.order o
             JOIN o.user u
             WHERE o.user = :user AND oi.orderItemId = :orderItemId
            """)
    Optional<OrderItem> findByUserAndId(AppUser user, Long orderItemId);
}