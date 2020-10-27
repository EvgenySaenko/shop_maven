package com.geekbrains.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopMavenApplication {

	public static void main(String[] args) {
		// Домашнее задание:
		// 1. Вам нужно оборачивать исключения на backend в json и отправлять на
		// фронтенд. Обработайте таким образом ситуацию с попыткой редактирования
		// товара с несуществующим id. Если пользователь попытается это сделать
		// ему надо показать понятный alert
		// 2. С помощью spring aop выведите в файл history.txt строки с описанием
		// того в какое время создавались, удалялись или модифицировались
		// товары или категории товаров, и желательно выводить их навзвание
		// *. Попробуйте перенести спецификации на AJS
		SpringApplication.run(ShopMavenApplication.class, args);
	}

}
