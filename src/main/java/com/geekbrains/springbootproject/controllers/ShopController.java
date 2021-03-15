package com.geekbrains.springbootproject.controllers;

import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ShopController {
    private final ProductService productsService;

    @GetMapping
    public String shopPage(Model model,
                           @RequestParam(value = "word") Optional<String> nameFilter,
                           @RequestParam(value = "min") Optional<BigDecimal> minPrice,
                           @RequestParam(value = "max") Optional <BigDecimal> maxPrice,
                           @RequestParam(value = "page") Optional<Integer> page
    ) {

        Page<Product> products = productsService.findWithFilter(nameFilter, minPrice, maxPrice, page);
        model.addAttribute("products", products);
        return "shop-page";
    }

    @GetMapping("/product_info/{id}")
    public String productPage(Model model, @PathVariable(value = "id") Long id) {
        Product product = productsService.findById(id);
        model.addAttribute("product", product);
        return "product-page";
    }
}
