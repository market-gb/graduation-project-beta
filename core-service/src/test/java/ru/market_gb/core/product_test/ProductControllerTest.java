package ru.market_gb.core.product_test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import ru.market_gb.core.controllers.ProductsController;
import ru.market_gb.core.converters.ProductConverter;
import ru.market_gb.core.dto.CategoryDto;
import ru.market_gb.core.dto.ProductDto;
import ru.market_gb.core.entities.Category;
import ru.market_gb.core.entities.Product;
import ru.market_gb.core.services.ProductService;

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


@WebMvcTest(ProductsController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private ProductService productService;
    @MockBean
    private ProductConverter productConverter;

    private final static String TITLE = "Milk";
    private final static String CATEGORY_TITLE = "Category";
    private final static BigDecimal PRICE = BigDecimal.valueOf(100);

    private static Product product;
    private static ProductDto productDto;

    private static Page<Product> productPage;

    @BeforeAll
    public static void initEntities() {
        product = new Product();
        product.setId(1L);
        product.setTitle(TITLE);
        product.setPrice(PRICE);
        product.setCategories(Set.of(new Category(1L, CATEGORY_TITLE)));

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setTitle(TITLE);
        productDto.setPrice(PRICE);
        productDto.setCategories(Set.of(new CategoryDto(1L, CATEGORY_TITLE)));

        productPage = new PageImpl<>(List.of(product));
    }

    @Test
    public void getAllTest() throws Exception {
        given(productService.findAll(null, null, null, null, 1)).willReturn(productPage);
        given(productConverter.entityToDto(product)).willReturn(productDto);
        mvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is(TITLE)));
    }

    @Test
    public void getByIdTest() throws Exception {
        given(productService.findById(1L)).willReturn(Optional.of(product));
        given(productConverter.entityToDto(product)).willReturn(productDto);
        mvc.perform(get("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(TITLE)))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    public void saveTest() throws Exception {
        given(productService.tryToSave(productDto, bindingResult)).willReturn(product);
        given(productConverter.entityToDto(product)).willReturn(productDto);
        mvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(productDto
                                )))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateTest() throws Exception {
        given(productService.tryToSave(productDto, bindingResult)).willReturn(product);
        given(productConverter.entityToDto(product)).willReturn(productDto);
        mvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(productDto
                                )))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteByIdTest() throws Exception {
        mvc.perform(delete("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
