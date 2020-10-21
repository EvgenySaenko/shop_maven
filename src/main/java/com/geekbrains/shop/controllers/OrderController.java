package com.geekbrains.shop.controllers;

import com.geekbrains.shop.beans.Cart;
import com.geekbrains.shop.entities.Order;
import com.geekbrains.shop.entities.User;
import com.geekbrains.shop.services.OrdersService;
import com.geekbrains.shop.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final UsersService usersService;
    private OrdersService ordersService;
    private Cart cart;

    @GetMapping("/create")
    public String createOrder(Principal principal, Model model) {
        User user = usersService.findByPhone(principal.getName()).get();
        model.addAttribute("user", user);
        return "order_info";
    }

    @PostMapping("/confirm")
    @ResponseBody
    public String confirmOrder(Principal principal, @RequestParam String address, @RequestParam String phone) {
        User user = usersService.findByPhone(principal.getName()).get();//вытаскиваем имя юзера(а имя юзера у нас телефон)
        Order order = new Order(user, cart, phone, address);//собираем заказ
        order = ordersService.saveOrder(order);
        //после этого заказ сормирован, а корзина стала пустой
        return order.getId() + " " + order.getPrice();
    }

}
