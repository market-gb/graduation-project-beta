package ru.market_gb.cart.converters;

import org.springframework.stereotype.Component;
import ru.market_gb.cart.dto.CartDto;
import ru.market_gb.cart.dto.CartItemDto;
import ru.market_gb.cart.entities.Cart;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartConverter {
    public CartDto modelToDto(Cart cart) {
        List<CartItemDto> cartItemDtos = cart.getItems().stream().map(it ->
                new CartItemDto(it.getProductId(), it.getProductTitle(), it.getQuantity(), it.getPricePerProduct(), it.getPrice())
        ).collect(Collectors.toList());
        return new CartDto(cartItemDtos, cart.getTotalPrice());
    }
}
