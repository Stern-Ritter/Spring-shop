package com.geekbrains.springbootproject.services;

import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.repositories.ProductsRepository;
import com.geekbrains.springbootproject.repositories.specifications.ProductFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 5;

    private ProductsRepository productsRepository;

    @Autowired
    public void setProductsRepository(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public Product findById(Long id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Override
    public Product findByTitle(String title) {
        return productsRepository.findOneByTitle(title);
    }

    @Override
    public Product save(Product product) {
        return productsRepository.save(product);
    }

    @Override
    public boolean delete(Long id){
        if (productsRepository.findById(id).isPresent()) {
            productsRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isProductWithTitleExists(String productTitle) {
        return productsRepository.findOneByTitle(productTitle) != null;
    }

    @Override
    public Page<Product> findWithFilter(Optional<String> nameFilter,
                                        Optional<BigDecimal> minPrice,
                                        Optional<BigDecimal> maxPrice,
                                        Optional<Integer> page
    ) {
        int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Specification<Product> spec = Specification.where(null);
        if (nameFilter.isPresent() && !nameFilter.get().isEmpty()) {
            spec = spec.and(ProductFilter.nameLike(nameFilter.get()));
        }
        if (minPrice.isPresent()) {
            spec = spec.and(ProductFilter.minPriceFilter(minPrice.get()));
        }
        if (maxPrice.isPresent()) {
            spec = spec.and(ProductFilter.maxPriceFilter(maxPrice.get()));
        }

        return productsRepository.findAll(spec, PageRequest.of(currentPage, PAGE_SIZE));
    }
}