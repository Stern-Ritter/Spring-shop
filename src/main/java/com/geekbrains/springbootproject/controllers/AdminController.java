package com.geekbrains.springbootproject.controllers;

import com.geekbrains.springbootproject.entities.Order;
import com.geekbrains.springbootproject.entities.OrderStatus;
import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.services.MailService;
import com.geekbrains.springbootproject.services.OrderService;
import com.geekbrains.springbootproject.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final OrderService orderService;
    private final MailService mailService;

    @GetMapping
    public String showAdminPage(Model model,
                                @RequestParam(value = "word") Optional<String> nameFilter,
                                @RequestParam(value = "min") Optional<BigDecimal> minPrice,
                                @RequestParam(value = "max") Optional <BigDecimal> maxPrice,
                                @RequestParam(value = "page") Optional<Integer> page) {
        Page<Product> products = productService.findWithFilter(nameFilter, minPrice, maxPrice, page);
        model.addAttribute("products", products);
        return "admin-page";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders-page";
    }

    @GetMapping("/orders/ready/{id}")
    public void orderReady(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) throws Exception {
        Order order = orderService.findById(id);
        orderService.changeOrderStatus(order, OrderStatus.APPROVED);
        mailService.sendOrderMail(order);
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/orders/info/{id}")
    public String orderInfo(@PathVariable("id") Long id, Model model){
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "order-info";
    }
}