package ru.market_gb.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.market_gb.api.exceptions.ResourceNotFoundException;
import ru.market_gb.core.converters.ProductConverter;
import ru.market_gb.api.dto.core.ProductDto;
import ru.market_gb.core.entities.Product;
import ru.market_gb.core.exceptions.CoreValidationException;
import ru.market_gb.core.exceptions.InvalidParamsException;
import ru.market_gb.core.repositories.ProductRepository;
import ru.market_gb.core.repositories.specifications.ProductsSpecifications;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productsRepository;
    private final ProductConverter productConverter;

    public Page<Product> findAll(Integer minPrice, Integer maxPrice, String partTitle, String categoryTitle, Integer page) {
        Specification<Product> spec = Specification.where(null);
        if (minPrice != null) {
            spec = spec.and(ProductsSpecifications.priceGreaterOrEqualsThan(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductsSpecifications.priceLessThanOrEqualsThan(maxPrice));
        }
        if (partTitle != null) {
            spec = spec.and(ProductsSpecifications.titleLike(partTitle));
        }
        if (categoryTitle != null) {
            spec = spec.and(ProductsSpecifications.findByCategory(categoryTitle));
        }
        return productsRepository.findAll(spec, PageRequest.of(page - 1, 8));
    }

    public Optional<Product> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return productsRepository.findById(id);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new InvalidParamsException("Невалидный параметр идентификатор:" + null);
        }
        try {
            productsRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Ошибка удаления товара. Товар " + id + "не существует");
        }
    }

    public Product tryToSave(ProductDto productDto, BindingResult bindingResult) {
        if (productDto == null) {
            throw new InvalidParamsException("Невалидный параметр 'productDto':" + null);
        }
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new CoreValidationException("Ошибка валидации", errors);
        }
        return save(productConverter.dtoToEntity(productDto));
    }

    private Product save(Product product) {
        if (product == null) {
            throw new InvalidParamsException("Невалидный параметр 'product':" + null);
        }
        if (product.getId() == null && isTitlePresent(product.getTitle())) {
            throw new InvalidParamsException("Товар с таким наименованием уже существует:" + product.getTitle());
        }
        return productsRepository.save(product);
    }

    private Boolean isTitlePresent(String title) {
        return productsRepository.countByTitle(title) > 0;
    }
}
