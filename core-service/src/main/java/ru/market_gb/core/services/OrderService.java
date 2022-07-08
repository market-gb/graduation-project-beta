package ru.market_gb.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market_gb.core.dto.CartDto;
import ru.market_gb.core.dto.OrderDetailsDto;
import ru.market_gb.core.entities.Order;
import ru.market_gb.core.entities.OrderItem;
import ru.market_gb.core.exceptions.CoreValidationException;
import ru.market_gb.core.exceptions.InvalidParamsException;
import ru.market_gb.core.exceptions.ResourceNotFoundException;
import ru.market_gb.core.integrations.CartServiceIntegration;
import ru.market_gb.core.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository ordersRepository;
    private final CartServiceIntegration cartServiceIntegration;
    private final ProductService productsService;

    @Transactional
    public Order save(String username, OrderDetailsDto orderDetailsDto) {
        if (username == null || orderDetailsDto == null){
            throw new InvalidParamsException("Невалидные параметры");
        }
        CartDto currentCart = cartServiceIntegration.getUserCart(username);
        Order order = new Order();
        order.setAddress(orderDetailsDto.getAddress());
        order.setPhone(orderDetailsDto.getPhone());
        order.setUsername(username);
        order.setTotalPrice(currentCart.getTotalPrice());
        order.setOrderStatus(Order.OrderStatus.CREATED);
        Set<OrderItem> items = currentCart.getItems().stream()
                .map(i -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setQuantity(i.getQuantity());
                    item.setPricePerProduct(i.getPricePerProduct());
                    item.setPrice(i.getPrice());
                    item.setProduct(productsService.findById(i.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Товар не найден")));
                    return item;
                }).collect(Collectors.toSet());
        order.setItems(items);
        ordersRepository.save(order);
        cartServiceIntegration.clearUserCart(username);
        return order;
    }

    public List<Order> findAllByUsername(String username) {
        if (username == null){
            throw new InvalidParamsException("Невалидные параметры");
        }
        return ordersRepository.findAllByUsername(username);
    }

    public Optional<Order> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return ordersRepository.findById(id);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new InvalidParamsException("Невалидный параметр идентификатор:" + null);
        }
        try {
            ordersRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Ошибка удаления заказа. Заказ " + id + "не существует");
        }
    }

    @Transactional
    public void changeStatus(Order.OrderStatus orderStatus, Long id) {
        if (orderStatus == null || id == null){
            throw new InvalidParamsException("Невалидные параметры");
        }
        try {
            ordersRepository.changeStatus(orderStatus, id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Ошибка изменения статуса заказа. Заказ " + id + "не существует");
        }
    }
}
