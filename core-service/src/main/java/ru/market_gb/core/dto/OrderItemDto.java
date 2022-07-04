package ru.market_gb.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель элемента заказа")
public class OrderItemDto {
    @Schema(description = "ID элемента заказа", required = true, example = "1")
    private Long productId;
    @Schema(description = "Название продукта", required = true, example = "Product#1")
    private String productTitle;
    @Schema(description = "Количество продуктов", required = true, example = "3")
    private int quantity;
    @Schema(description = "Цена за еденицу", required = true, example = "500.00")
    private BigDecimal pricePerProduct;
    @Schema(description = "Стоимость элемента заказа", required = true, example = "1500.00")
    private BigDecimal price;

    @Override
    public String toString() {
        return productTitle +
                " - " + quantity+ " pcs";
    }
}
