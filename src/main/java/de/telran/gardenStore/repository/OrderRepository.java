package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Order;
import de.telran.gardenStore.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(AppUser user);

    List<Order> findAllByStatusNotIn(Collection<OrderStatus> statuses);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findAllByStatusAndCreatedAtBetween(
            @Param("status") OrderStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Order> findAllByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findAllByCreatedAtBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
