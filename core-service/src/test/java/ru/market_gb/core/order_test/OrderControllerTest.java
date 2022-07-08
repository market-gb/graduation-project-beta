package ru.market_gb.core.order_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import ru.market_gb.api.dto.core.enums.OrderStatus;
import ru.market_gb.core.controllers.OrdersController;
import ru.market_gb.core.converters.OrderConverter;
import ru.market_gb.api.dto.core.OrderDetailsDto;
import ru.market_gb.api.dto.core.OrderDto;
import ru.market_gb.api.dto.core.OrderItemDto;
import ru.market_gb.core.entities.Category;
import ru.market_gb.core.entities.Order;
import ru.market_gb.core.entities.OrderItem;
import ru.market_gb.core.entities.Product;
import ru.market_gb.core.services.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(OrdersController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;
    @MockBean
    private OrderConverter orderConverter;
    private final static String TITLE = "Milk";
    private final static String CATEGORY_TITLE = "Category";
    private static final BigDecimal PRICE_PER_PRODUCT = BigDecimal.valueOf(100);
    private final static BigDecimal PRICE = BigDecimal.valueOf(100);
    private final static Integer QUANTITY = 1;
    private static final String USERNAME = "test_user";
    private static final String ADDRESS = "address";
    private static final String PHONE = "123456";
    public static OrderDetailsDto orderDetailsDto;
    private static OrderDto orderDto;
    private static Order order;
    private static List<Order> orderList;

    @BeforeAll
    public static void initEntities() {
        Product product = new Product();
        product.setId(1L);
        product.setTitle(TITLE);
        product.setPrice(PRICE_PER_PRODUCT);
        product.setCategories(Set.of(new Category(1L, CATEGORY_TITLE)));

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(1L);
        orderItemDto.setProductTitle(TITLE);
        orderItemDto.setQuantity(QUANTITY);
        orderItemDto.setPricePerProduct(PRICE_PER_PRODUCT);
        orderItemDto.setPrice(PRICE);

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUsername(USERNAME);
        orderDto.setAddress(ADDRESS);
        orderDto.setPhone(PHONE);
        orderDto.setOrderStatus(OrderStatus.CREATED);
        orderDto.setItems(Set.of(orderItemDto));

        order = new Order();
        order.setId(1L);
        order.setUsername(USERNAME);
        order.setTotalPrice(PRICE);
        order.setAddress(ADDRESS);
        order.setPhone(PHONE);
        order.setOrderStatus(OrderStatus.CREATED);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProduct(product);
        orderItem.setOrder(order);
        orderItem.setQuantity(QUANTITY);
        orderItem.setPrice(PRICE);
        orderItem.setPricePerProduct(PRICE_PER_PRODUCT);

        order.setItems(Set.of(orderItem));
        orderList = List.of(order);
        orderDetailsDto = new OrderDetailsDto(ADDRESS, PHONE);
    }

    @Test
    public void saveTest() throws Exception {
        given(orderService.save(USERNAME, orderDetailsDto)).willReturn(order);
        given(orderConverter.entityToDto(order)).willReturn(orderDto);
        mvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("username", USERNAME)
                        .content(objectMapper
                                .writeValueAsString(orderDetailsDto
                                )))
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllByUsernameTest() throws Exception {
        given(orderService.findAllByUsername(USERNAME)).willReturn(orderList);
        given(orderConverter.entityToDto(order)).willReturn(orderDto);
        mvc.perform(get("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("username", USERNAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(USERNAME)));
    }

    @Test
    public void getByIdTest() throws Exception {
        given(orderService.findById(1L)).willReturn(Optional.of(order));
        given(orderConverter.entityToDto(order)).willReturn(orderDto);
        mvc.perform(get("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username", is(USERNAME)));
    }

    @Test
    public void changeStatusTest() throws Exception {
        mvc.perform(put("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(OrderStatus.PAID
                                )))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteByIdTest() throws Exception {
        mvc.perform(delete("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
