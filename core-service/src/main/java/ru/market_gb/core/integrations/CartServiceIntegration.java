package ru.market_gb.core.integrations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.market_gb.core.dto.CartDto;
import ru.market_gb.core.exceptions.CartServiceIntegrationException;
import ru.market_gb.core.exceptions.ServiceAppError;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartServiceIntegration {
    @Value("${cartServiceIntegration.clearUserCartUri}")
    private String clearUserCartUri;
    @Value("${cartServiceIntegration.getUserCartUri}")
    private String getUserCartUri;
    private final WebClient cartServiceWebClient;

    public void clearUserCart(String username) {
        getOnStatus(clearUserCartUri, username)
                .toBodilessEntity()
                .block();
    }

    public CartDto getUserCart(String username) {
        return getOnStatus(getUserCartUri, username)
                .bodyToMono(CartDto.class)
                .block();
    }

    private WebClient.ResponseSpec getOnStatus(String uri, String username) {
        return cartServiceWebClient.get()
                .uri(uri)
                .header("username", username)
                .retrieve()
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(ServiceAppError.class).map(
                                body -> {
                                    if (body.getCode().equals(ServiceAppError.ServiceErrors.CART_NOT_FOUND)) {
                                        log.error("Выполнен некорректный запрос к сервису корзин: корзина не найдена");
                                        return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: корзина не найдена");
                                    }
                                    if (body.getCode().equals(ServiceAppError.ServiceErrors.CART_SERVICE_IS_BROKEN)) {
                                        log.error("Выполнен некорректный запрос к сервису корзин: корзина сломана");
                                        return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: корзина сломана");
                                    }
                                    log.error("Выполнен некорректный запрос к сервису корзин: причина неизвестна");
                                    return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: причина неизвестна");
                                }
                        )
                );
    }
}
