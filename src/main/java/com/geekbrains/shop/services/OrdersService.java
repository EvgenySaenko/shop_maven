package com.geekbrains.shop.services;

import com.geekbrains.shop.entities.Order;
import com.geekbrains.shop.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

    private OrdersRepository ordersRepository;

    @Autowired
    public void setOrdersRepository(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public Order saveOrder(Order order) {
        //почему мы тут не сохраняем ордер айтемы?
        //потому что в Order у нас включены каскадные операции, и сохраняя ордер мы сохраним ордер айтемы
        return ordersRepository.save(order);
    }
}
