package com.geekbrains.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopMavenApplication {

	public static void main(String[] args) {
		// Домашнее задание:
		// 1. Добавьте spring web service для выгрузки всех товаров
		// 2. (без практики) Полистайте код с безопасностью и токенами

		// Планы на будущее (не дз):
		// 1. Добавить refresh jwt
		SpringApplication.run(ShopMavenApplication.class, args);
	}

}
