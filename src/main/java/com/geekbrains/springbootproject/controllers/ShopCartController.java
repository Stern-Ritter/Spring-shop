package com.geekbrains.springbootproject.controllers;

import com.geekbrains.springbootproject.utils.ShopCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/shop/cart")
public class ShopCartController {
    private ShopCart shopCart;

    @Autowired
    public void setShoppingCartService(ShopCart shopCart) {
        this.shopCart = shopCart;
    }

    @GetMapping
    public String cartPage(Model model) {
        model.addAttribute("cart", shopCart);
        return "cart-page";
    }

    @GetMapping("/add/{id}")
    public String addProductToCart(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        shopCart.add(id);
        String referrer = httpServletRequest.getHeader("referer");
        return "redirect:" + referrer;
    }

    @GetMapping("/dec/{id}")
    public String decrementProductCountInCart(@PathVariable("id") Long id, HttpServletRequest httpServletRequest){
        shopCart.decrement(id);
        String referer = httpServletRequest.getHeader("referer");
        return "redirect:" + referer;
    }

    @GetMapping("/delete/{id}")
    public String deleteProductFromCart(@PathVariable("id") Long id, HttpServletRequest httpServletRequest){
        shopCart.remove(id);
        String referer = httpServletRequest.getHeader("referer");
        return "redirect:" + referer;
    }
}
