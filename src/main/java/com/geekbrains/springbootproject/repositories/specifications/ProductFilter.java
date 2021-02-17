package com.geekbrains.springbootproject.repositories.specifications;

import com.geekbrains.springbootproject.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductFilter {

    public static Specification<Product> nameLike(String name) {
        return (root, query, builder) -> builder.like(root.get("title"), "%" + name + "%");
    }

    public static Specification<Product> minPriceFilter(BigDecimal minPrice) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> maxPriceFilter(BigDecimal maxPrice) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
