package com.geekbrains.springbootproject.repositories;

import com.geekbrains.springbootproject.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Product findOneByTitle(String title);
}
