package com.geekbrains.springbootproject.utils;

import com.geekbrains.springbootproject.entities.Category;
import com.geekbrains.springbootproject.entities.OrderItem;
import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.services.ProductsServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import java.time.LocalDateTime;

@DisplayName("ShoppingCart (корзина товаров) должна:")
public class ShoppingCartTest {
    private ShoppingCart shoppingCart;
    private ProductsServiceImpl productService;

    @Before
    public void initializationShoppingCart(){
        shoppingCart = Mockito.spy(ShoppingCart.class);
        productService = Mockito.mock(ProductsServiceImpl.class);
        shoppingCart.setProductsService(productService);
    }

    @After
    public void destroyShoppingCart(){
        shoppingCart = null;
        productService = null;
    }

    @DisplayName("вызывать один раз методы: #findById(Long) у ProductsService и #add(Product) при вызове метода #add(Long)")
    @Test
    public void methodsCalledCorrectlyOnceWhenAddProduct() {
        Product product = Mockito.spy(new Product(1L, new Category(1L, "Основная","Основная категория"),
                "11111111", null, "Наименование","Краткое описание", "Полное описание",
                10.0, LocalDateTime.now(), LocalDateTime.now()));
        Mockito.when(productService.findById(1L)).thenReturn(product);
        shoppingCart.add(1L);
        Mockito.verify(productService, Mockito.times(1)).findById(1L);
        Mockito.verify(shoppingCart, Mockito.times(1)).add(product);
    }
    @DisplayName("выдавать ошибку при попытке добавить товар с несуществующим product id (вызове метода #add(Long))")
    @Test(expected = NullPointerException.class)
    public void addProductWithNotExistingId(){
        Mockito.when(productService.findById(-1L)).thenReturn(null);
        shoppingCart.add(-1L);
    }

    @DisplayName("корректно увеличивать кол-во для товара, ранее отсутствовавшего в корзине, при вызове метода #add(Product)")
    @Test
    public void addProductExistingInShoppingCart(){
        Product firstProduct = Mockito.spy(new Product(1L, new Category(1L, "Основная","Основная категория"),
                "11111111", null, "Наименование","Краткое описание", "Полное описание",
                10.0, LocalDateTime.now(), LocalDateTime.now()));
        Product secondProduct = Mockito.spy(new Product(2L, new Category(2L, "Основная","Основная категория"),
                "22222222", null, "Наименование","Краткое описание", "Полное описание",
                10.0, LocalDateTime.now(), LocalDateTime.now()));
        shoppingCart.add(firstProduct);
        shoppingCart.add(secondProduct);
        OrderItem orderItem = null;
        for(OrderItem o : shoppingCart.getItems()){
            if(o.getProduct().getId().equals(firstProduct.getId())){
                orderItem = o;
                break;
            }
        }
        Assert.assertNotNull(orderItem);
        Assert.assertEquals(firstProduct, orderItem.getProduct());
        Assert.assertTrue(firstProduct.getPrice() - orderItem.getItemPrice() < 0.01);
        Assert.assertEquals(Long.valueOf(1), orderItem.getQuantity());
    }

    @DisplayName("корректно увеличивать кол-во для товара, ранее отсутствовавшего в корзине, при вызове метода #add(Product)")
    @Test
    public void addProductMissingInShoppingCart(){
        Product firstProduct = Mockito.spy(new Product(1L, new Category(1L, "Основная","Основная категория"),
                "11111111", null, "Наименование","Краткое описание", "Полное описание",
                10.0, LocalDateTime.now(), LocalDateTime.now()));
        Product secondProduct = Mockito.spy(new Product(2L, new Category(2L, "Основная","Основная категория"),
                "22222222", null, "Наименование","Краткое описание", "Полное описание",
                10.0, LocalDateTime.now(), LocalDateTime.now()));
        shoppingCart.add(firstProduct);
        shoppingCart.add(firstProduct);
        shoppingCart.add(firstProduct);
        shoppingCart.add(secondProduct);
        OrderItem orderItem = null;
        for(OrderItem o : shoppingCart.getItems()){
            if(o.getProduct().getId().equals(firstProduct.getId())){
                orderItem = o;
                break;
            }
        }
        Assert.assertNotNull(orderItem);
        Assert.assertEquals(firstProduct, orderItem.getProduct());
        Assert.assertTrue(firstProduct.getPrice() - orderItem.getItemPrice() < 0.01);
        Assert.assertEquals(Long.valueOf(3), orderItem.getQuantity());
    }

    @DisplayName("корректно удалять ранее добавленный в корзину товар, при вызове метода #remove(Product)")
    @Test
    public void removeProductFromShoppingCart() {
        Product product = Mockito.spy(new Product(1L, new Category(1L, "Основная","Основная категория"),
                "11111111", null, "Наименование","Краткое описание", "Полное описание",
                10.0, LocalDateTime.now(), LocalDateTime.now()));
        shoppingCart.add(product);
        shoppingCart.remove(product);
        OrderItem orderItem = null;
        for(OrderItem o : shoppingCart.getItems()){
            if(o.getProduct().getId().equals(product.getId())){
                 orderItem = o;
                break;
            }
        }
        Assert.assertNull(orderItem);
    }
}