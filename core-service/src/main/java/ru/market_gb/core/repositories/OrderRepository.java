package ru.market_gb.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.market_gb.core.entities.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.username = ?1")
    List<Order> findAllByUsername(String username);

    @Modifying
    @Query("update Order o set o.orderStatus = ?1 where o.id = ?2")
    void changeStatus(Order.OrderStatus orderStatus, Long orderId);
}
