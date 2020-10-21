package com.geekbrains.shop.controllers;

import com.geekbrains.shop.beans.Cart;
import com.geekbrains.shop.services.ProductsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {

    private final Cart cart;
    private final ProductsService productsService;

    @GetMapping
    public String showCartPage(Model model) {
        return "cart";
    }

    //в протоколе http есть headerReferer - это ссылка откуда мы послали запрос, с какой страницы
    @GetMapping("/add/{productId}")//закидываем  - request(вся инфа о запросе) и response(вся инфа об ответе)
    public void addProductToCartById(@PathVariable Long productId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.add(productsService.findById(productId));
        //тут мы вытаскиваем данные откуда пришел запрос и потом переходим на эту страницу(метод может быть void при таком виде)
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/decrement/{productId}")
    public void decrementProductToCartById(@PathVariable Long productId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.decrement(productsService.findById(productId));
        response.sendRedirect(request.getHeader("referer"));
    }

    //в протоколе http есть headerReferer - это ссылка откуда мы послали запрос, с какой страницы
    @GetMapping("/remove/{productId}")//закидываем  - request(вся инфа о запросе) и response(вся инфа об ответе)
    public void removeProductFromCartById(@PathVariable Long productId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.removeByProductId(productId);
        //тут мы вытаскиваем данные откуда пришел запрос и потом переходим на эту страницу(метод может быть void при таком виде)
        response.sendRedirect(request.getHeader("referer"));
    }
}
