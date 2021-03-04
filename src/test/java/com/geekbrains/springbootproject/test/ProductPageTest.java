package com.geekbrains.springbootproject.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProductPageTest {

    private WebDriver driver;

    @BeforeSuite
    public void initialization() {
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
        this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void outputQuantityOfProductsIsGreaterThenZero() {
        driver.get("http://localhost:8189/project/shop");
        List<WebElement> products = driver
                .findElement(By.className("container"))
                .findElement(By.className("table-hover"))
                .findElement(By.tagName("tbody"))
                .findElements(By.tagName("tr"));
        Assert.assertTrue(products.size() > 0);
    }

    @AfterSuite
    public void shutdown() {
        this.driver.quit();
    }
}
