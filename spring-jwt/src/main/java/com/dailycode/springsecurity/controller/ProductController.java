package com.dailycode.springsecurity.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
@RestController
@RequestMapping("/product")
public class ProductController {
    private record Product(Integer productId, String productName, double price) {}
        List<Product> products = new ArrayList<>(List.of(
            new Product(1, "Laptop", 75000.0),
            new Product(2, "Smartphone", 45000.0),
            new Product(3, "Headphones", 2500.0),
            new Product(4, "Smartwatch", 10000.0),
            new Product(5, "Tablet", 30000.0)
        ));
        @GetMapping
     public List<Product> getProducts(){
    	 return products;
     }
        @PostMapping
        public Product saveProduct(@RequestBody Product product) {
        	products.add(product);
        	return product;
        }
        
}

