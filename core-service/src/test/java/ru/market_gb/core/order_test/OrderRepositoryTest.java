package ru.market_gb.core.order_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.market_gb.core.entities.Order;
import ru.market_gb.core.repositories.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TestEntityManager entityManager;
    private static final String USERNAME = "test_user";
    private static final String ADDRESS = "address";
    private static final String PHONE = "123456";
    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(100);

    @BeforeEach
    public void init() {
        Order order = new Order();
        order.setUsername(USERNAME);
        order.setAddress(ADDRESS);
        order.setPhone(PHONE);
        order.setOrderStatus(Order.OrderStatus.CREATED);
        order.setTotalPrice(TOTAL_PRICE);
        order.setItems(Set.of());
        entityManager.persist(order);
        entityManager.flush();
    }

    @Test
    @Transactional
    public void changeOrderStatusTest() {
        orderRepository.changeStatus(Order.OrderStatus.PAID, 1L);
        Order order = orderRepository.findById(1L).orElse(new Order());
        Assertions.assertEquals(Order.OrderStatus.PAID, order.getOrderStatus());
    }

    @Test
    public void findAllByUsernameTest() {
        List<Order> orderList = orderRepository.findAllByUsername(USERNAME);
        Assertions.assertEquals(2, orderList.size());
        Assertions.assertEquals(Order.OrderStatus.CREATED, orderList.get(0).getOrderStatus());
        Assertions.assertEquals(USERNAME, orderList.get(0).getUsername());
        Assertions.assertEquals(USERNAME, orderList.get(1).getUsername());
    }
}
