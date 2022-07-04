package ru.market_gb.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.market_gb.core.entities.Product;
import ru.market_gb.core.repositories.ProductRepository;
import ru.market_gb.core.repositories.specifications.ProductsSpecifications;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductRepository productsRepository;

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
            return;
        }
        productsRepository.deleteById(id);
    }

    public void save(Product product) {
        if (product == null) {
            return;
        }
        if (isTitleOfProductPresent(product.getTitle())) {
            return;
        }
        productsRepository.save(product);
    }

    private Boolean isTitleOfProductPresent(String title) {
        return productsRepository.countByTitle(title) > 0;
    }
}
