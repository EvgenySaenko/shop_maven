package com.geekbrains.shop.repositories;

import com.geekbrains.shop.entities.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends CrudRepository<Order,Long> {
}
