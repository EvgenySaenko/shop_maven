package com.geekbrains.shop.repositories.specifications;


import com.geekbrains.shop.entities.Category;
import com.geekbrains.shop.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
    public static Specification<Product> priceGreaterOrEqualsThan(int minPrice) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceLesserOrEqualsThan(int maxPrice) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> titleLike(String title) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), String.format("%%%s%%",title));
    }

    public static Specification<Product> categoryIs(Category category) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
//            Join join = root.join("categories");
//            return criteriaBuilder.equal(join.get("id"), category.getId());
            //isMember проверяет является ли эта категория (category) членом списка категорий,
            // если мы у нашего продукта спросим список категорий(и она там окажется то мы ее берем)
            return criteriaBuilder.isMember(category, root.get("categories"));
        };
    }
}
