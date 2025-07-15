package de.telran.gardenStore.repository;

import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(AppUser user);
}