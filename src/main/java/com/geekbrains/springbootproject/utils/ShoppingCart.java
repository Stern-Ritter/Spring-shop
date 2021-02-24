package com.geekbrains.springbootproject.utils;

import com.geekbrains.springbootproject.entities.OrderItem;
import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.services.ProductsServiceImpl;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCart {

    private ProductsServiceImpl productsService;

    @Value("${rabbitmq.host}")
    private String host;
    @Value("${rabbitmq.queue}")
    private String queueName;

    @Autowired
    public void setProductsService(ProductsServiceImpl productsService) {
        this.productsService = productsService;
    }

    private List<OrderItem> items;
    private Double totalCost;

    public ShoppingCart() {
        items = new ArrayList<>();
        totalCost = 0.0;
    }

    public void add(Long productId) {
        Product product = productsService.findById(productId);
        this.add(product);
        this.sendToQueue(product);
    }

    public void add(Product product) {
        OrderItem orderItem = findOrderFromProduct(product);
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
    }

    private void sendToQueue(Product product) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.queueDeclare(queueName, false, false, false, null);
            String msg = String.format("Товар: %s, цена: %.2f.", product.getTitle(), product.getPrice());
            channel.basicPublish("", queueName, null, msg.getBytes("UTF-8"));
        } catch (Exception ex) {
            log.error("В ходе отправки в очередь события по добавлению товара в корзину выявлена ошибка.");
        }
    }

    public void processFromQueue(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            while(channel.messageCount(queueName) != 0){
                String msg = new String(channel.basicGet(queueName, true).getBody(), "UTF-8");
                log.info("{}", msg);
            }
        } catch (Exception ex){
            log.error("В ходе получения из очереди событий по добавлению товаров в корзину выявлена ошибка.");
            ex.printStackTrace();
        }
    }

    public void setQuantity(Product product, Long quantity) {
        OrderItem orderItem = findOrderFromProduct(product);
        if (orderItem == null) {
            return;
        }
        orderItem.setQuantity(quantity);
    }

    public void remove(Product product) {
        OrderItem orderItem = findOrderFromProduct(product);
        if (orderItem == null) {
            return;
        }
        items.remove(orderItem);
    }

    public void recalculate() {
        totalCost = 0.0;
        for (OrderItem o : items) {
            o.setTotalPrice(o.getQuantity() * o.getProduct().getPrice());
            totalCost += o.getTotalPrice();
        }
    }

    private OrderItem findOrderFromProduct(Product product) {
        return items.stream().filter(o -> o.getProduct().getId().equals(product.getId())).findFirst().orElse(null);
    }
}
