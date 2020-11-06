package com.geekbrains.shop.controllers;

import com.geekbrains.shop.beans.Cart;
import com.geekbrains.shop.entities.Product;
import com.geekbrains.shop.entities.dtos.OrderItemDto;
import com.geekbrains.shop.exceptions.ResourceNotFoundException;
import com.geekbrains.shop.services.OrderItemService;
import com.geekbrains.shop.services.ProductsService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
public class CartController {
    private OrderItemService orderItemService;
    private ProductsService productsService;
    private Cart cart;

    @GetMapping("/add/{productId}")
    public void addProductToCartById(@PathVariable Long productId) {
        Product product = productsService.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Unable to add product (id = " + productId + " ) to cart. Product not found"));
        cart.add(product);
    }

    @GetMapping
    public List<OrderItemDto> getCartContent() {
        return orderItemService.mapEntityListToDtoList(cart.getItems());
    }

//    @GetMapping("/decrement/{productId}")
//    public void decrementProductToCartById(@PathVariable Long productId, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        cart.decrement(productsService.findById(productId));
//        response.sendRedirect(request.getHeader("referer"));
//    }
//
//    @GetMapping("/remove/{productId}")
//    public void removeProductFromCartById(@PathVariable Long productId, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        cart.removeByProductId(productId);
//        response.sendRedirect(request.getHeader("referer"));
//    }
}
