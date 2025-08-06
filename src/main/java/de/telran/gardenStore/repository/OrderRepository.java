package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserAndOrderId(AppUser user, Long orderId);

    List<Order> findAllByUser(AppUser user);

    List<Order> findByStatusAndUpdatedAtAfter(OrderStatus status, LocalDateTime updatedAt);

}
