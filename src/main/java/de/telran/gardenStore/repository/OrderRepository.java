package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Order> findByUserAndOrderId(AppUser user, Long orderId);

    List<Order> findAllByUser(AppUser user);

    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findByStatusAndUpdatedAtAfter(OrderStatus status, LocalDateTime updatedAt);

    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findAllByUserAndStatus(AppUser current, OrderStatus orderStatus);
}
