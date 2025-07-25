package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.OrderItem;
import de.telran.gardenStore.exception.OrderItemNotFoundException;
import de.telran.gardenStore.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;


    @Override
    public OrderItem getById(Long orderItemId){
        return orderItemRepository.findById(orderItemId).orElseThrow(() -> new OrderItemNotFoundException("Order item with id " + orderItemId + " not found"));
    }

}
