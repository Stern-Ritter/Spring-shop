package com.geekbrains.springbootproject.config;

import com.geekbrains.springbootproject.utils.ShoppingCart;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AppLoggingAspect {

    @Before("execution(public * com.geekbrains.springbootproject.utils.ShoppingCart.*(..))")
    public void logUsingAllShoppingCartMethods(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("Использован метод: {} корзины.", methodSignature);
    }

    @Around("execution(public void com.geekbrains.springbootproject.utils.ShoppingCart.add(Long))" +
            "|| execution(public void com.geekbrains.springbootproject.utils.ShoppingCart.setQuantity(com.geekbrains.springbootproject.entities.Product, Long))" +
            "|| execution(public void com.geekbrains.springbootproject.utils.ShoppingCart.remove(com.geekbrains.springbootproject.entities.Product))")
    public void shoppingCartRecalculate(ProceedingJoinPoint proceedingJoinPoint){
        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            log.info("В ходе пересчёта корзины возникла ошибка: {}.", throwable.getMessage());
        }
        ((ShoppingCart)proceedingJoinPoint.getThis()).recalculate();
        log.info("Вызван метод пересчёта корзины на объекте {}.", proceedingJoinPoint.getThis());
    }
}