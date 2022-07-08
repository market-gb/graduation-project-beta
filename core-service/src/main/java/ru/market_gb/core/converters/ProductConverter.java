package ru.market_gb.core.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.market_gb.core.dto.ProductDto;
import ru.market_gb.core.entities.Product;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final CategoryConverter categoryConverter;

    public Product dtoToEntity(ProductDto productDto) {
        return new Product(productDto.getId(), productDto.getTitle(), productDto.getPrice(), categoryConverter.setDtoToSetEntities(productDto.getCategories()));
    }

    public ProductDto entityToDto(Product product) {
        return new ProductDto(product.getId(), product.getTitle(), product.getPrice(), categoryConverter.setEntitiesToSetDto(product.getCategories()));
    }
}
