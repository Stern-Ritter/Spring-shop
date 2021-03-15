package com.geekbrains.springbootproject.utils;

import com.geekbrains.springbootproject.entities.OrderItem;
import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.services.ProductServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
@Data
public class ShopCart {

    private final ProductServiceImpl productsService;
    private List<OrderItem> items;
    private Double totalCost;

    @PostConstruct
    public void init(){
        items = new ArrayList<>();
    }

    public void add(Long productId) {
        for(OrderItem o: items){
            if(o.getProduct().getId().equals(productId)){
                o.setQuantity(o.getQuantity() + 1);
                recalculate();
                return;
            }
        }
        Product product = productsService.findById(productId);
        OrderItem orderItem = findProductInOrderItems(product.getId());
        if (orderItem == null) {
            orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setItemPrice(product.getPrice());
            orderItem.setQuantity(0L);
            orderItem.setId(0L);
            orderItem.setTotalPrice(0.0);
            items.add(orderItem);
        }
        orderItem.setQuantity(orderItem.getQuantity() + 1);
        recalculate();
    }

    public void decrement(Long productId) {
        OrderItem orderItem = findProductInOrderItems(productId);
        if (orderItem == null) {
            return;
        }
        if(orderItem.getQuantity() > 1) {
            orderItem.setQuantity(orderItem.getQuantity() - 1);
        } else{
            items.remove(orderItem);
        }
        recalculate();
    }

    public void remove(Long productId) {
        OrderItem orderItem = findProductInOrderItems(productId);
        if (orderItem == null) {
            return;
        }
        items.remove(orderItem);
        recalculate();
    }

    public void recalculate() {
        totalCost = 0.0;
        for (OrderItem o : items) {
            o.setTotalPrice(o.getQuantity() * o.getProduct().getPrice());
            totalCost += o.getTotalPrice();
        }
    }

    private OrderItem findProductInOrderItems(Long productId) {
        return items.stream().filter(o -> o.getProduct().getId().equals(productId)).findFirst().orElse(null);
    }
}
