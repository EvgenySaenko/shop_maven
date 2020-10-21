package com.geekbrains.shop.beans;

import com.geekbrains.shop.entities.OrderItem;
import com.geekbrains.shop.entities.Product;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
//SCOPE_SESSION spring при обращении к корзине лезет в сессию и достает бин, если его нет то создает и достает
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class Cart {
    private List<OrderItem> items;
    private BigDecimal price;

    @PostConstruct
    public void init(){
        this.items = new ArrayList<OrderItem>();
    }

    public void clear(){
        items.clear();
        recalculate();
    }

    public void add(Product product) {
        for (OrderItem i : items) {
            if (i.getProduct().getId().equals(product.getId())) {
                i.increment();
                recalculate();
                return;
            }
        }
        items.add(new OrderItem(product));
        recalculate();
    }

    public void decrement(Product product) {
        for (OrderItem i : items) {
            if (i.getProduct().getId().equals(product.getId())) {
                i.decrement();
                if (i.isEmpty()) {
                    items.remove(i);
                }
                recalculate();
                return;
            }
        }
        items.add(new OrderItem(product));
        recalculate();
    }

    public void removeByProductId(Long productId){
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getId().equals(productId)) {
                items.remove(i);
                recalculate();
                return;
            }
        }
    }

    public void recalculate() {
        price = new BigDecimal(0.0);
        for (OrderItem i : items) {
            price = price.add(i.getPrice());
        }
    }
}
