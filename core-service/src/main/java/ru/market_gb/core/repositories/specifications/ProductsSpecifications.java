package ru.market_gb.core.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.market_gb.core.entities.Product;

public class ProductsSpecifications {
    public static Specification<Product> priceGreaterOrEqualsThan(Integer price) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> priceLessThanOrEqualsThan(Integer price) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> titleLike(String titlePart) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), String.format("%%%s%%", titlePart));
    }

    public static Specification<Product> findByCategory(String categoryTitle) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("categories").get("title"), categoryTitle);
    }
}
