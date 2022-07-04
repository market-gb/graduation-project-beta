package ru.market_gb.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.market_gb.core.entities.Order;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель заказа")
public class OrderDto {
    @Schema(description = "ID заказа", required = true, example = "1")
    private Long id;
    @Schema(description = "Имя получателя", required = true, example = "Иванов Иван Иванович")
    private String username;
    @Schema(description = "Список покупок", required = true, example = "{Product#1 - 4 pcs, Product#2 - 3 pcs}")
    private Set<OrderItemDto> items;
    @Schema(description = "Стоимость заказа", required = true, example = "1000.00")
    private BigDecimal totalPrice;
    @Schema(description = "Адрес доставки", required = true, example = "603000, г. Москва, ул. Прямая, д.10, кв.1")
    private String address;
    @Schema(description = "Телефон получателя", required = true, example = "222-22-22")
    private String phone;
    @Schema(description = "Статус заказа", required = true, example = "CREATED")
    private Order.OrderStatus orderStatus;
}
