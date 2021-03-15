package com.geekbrains.springbootproject.controllers;

import com.geekbrains.springbootproject.entities.DeliveryAddress;
import com.geekbrains.springbootproject.entities.Order;
import com.geekbrains.springbootproject.entities.User;
import com.geekbrains.springbootproject.services.DeliveryAddressService;
import com.geekbrains.springbootproject.services.OrderService;
import com.geekbrains.springbootproject.services.UserService;
import com.geekbrains.springbootproject.utils.ShopCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class OrderController {
    private UserService userService;
    private OrderService orderService;
    private DeliveryAddressService deliverAddressService;
    private ShopCart shopCart;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setDeliverAddressService(DeliveryAddressService deliverAddressService) {
        this.deliverAddressService = deliverAddressService;
    }

    @Autowired
    public void setShoppingCart(ShopCart shopCart) {
        this.shopCart = shopCart;
    }

    @GetMapping("/order/fill")
    public String orderFill(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        DeliveryAddress deliveryAddress = deliverAddressService.getUserAddress(user.getId());
        String lastAddress = deliveryAddress == null ? "" : deliveryAddress.getAddress();
        model.addAttribute("cart", shopCart);
        model.addAttribute("deliveryAddress", lastAddress);
        return "order-filler";
    }

    @PostMapping("/order/confirm")
    public String orderConfirm(Model model, Principal principal, @RequestParam("deliveryAddress") String deliveryAddress,
                               @RequestParam("phoneNumber") String phoneNumber) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName());
        DeliveryAddress address = new DeliveryAddress();
        address.setUser(user);
        address.setAddress(deliveryAddress);
        deliverAddressService.save(address);

        Order order = orderService.makeOrder(shopCart, user);
        order.setDeliveryAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setDeliveryDate(LocalDateTime.now().plusDays(7));
        order.setDeliveryPrice(10.0);
        order = orderService.saveOrder(order);
        model.addAttribute("order", order);
        return "order-result";
    }

    @GetMapping("/order/result/{id}")
    public String orderConfirm(Model model, @PathVariable(name = "id") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        // todo ждем до оплаты, проверка безопасности и проблема с повторной отправкой письма сделать одноразовый вход
        User user = userService.findByUserName(principal.getName());
        Order confirmedOrder = orderService.findById(id);
        if (!user.getId().equals(confirmedOrder.getUser().getId())) {
            return "redirect:/";
        }
        model.addAttribute("order", confirmedOrder);
        return "order-result";
    }
}
