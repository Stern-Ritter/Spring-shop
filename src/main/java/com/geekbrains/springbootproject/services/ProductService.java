package com.geekbrains.springbootproject.services;

import com.geekbrains.springbootproject.entities.Product;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductService {

    Product findById(Long id);

    Product findByTitle(String title);

    Product save(Product product);

    Page<Product> findWithFilter(Optional<String> nameFilter,
                                 Optional<BigDecimal> minPrice,
                                 Optional<BigDecimal> maxPrice,
                                 Optional<Integer> page);
}
