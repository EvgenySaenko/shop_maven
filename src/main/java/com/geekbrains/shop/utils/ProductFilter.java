package com.geekbrains.shop.utils;

import com.geekbrains.shop.entities.Category;
import com.geekbrains.shop.entities.Product;
import com.geekbrains.shop.repositories.specifications.ProductSpecifications;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

@Getter
public class ProductFilter {
    private Specification<Product> spec;
    private StringBuilder filterDefinition;
    //будет формировать кусочек пути при использовании фильтра
    public ProductFilter(Map<String, String> map, List<Category>categories) {
        //создаем пустую спецификацию
        this.spec = Specification.where(null);
        this.filterDefinition = new StringBuilder();
        //если есть мин прайс и он не пустой=> мы его парсим и аппендим к filterDefinition
        if (map.containsKey("min_price") && !map.get("min_price").isEmpty()) {
            int minPrice = Integer.parseInt(map.get("min_price"));
            //говорим что наши товары должны быть дороже чем min_price
            spec = spec.and(ProductSpecifications.priceGreaterOrEqualsThan(minPrice));
            filterDefinition.append("&min_price=").append(minPrice);
        }
        if (map.containsKey("max_price") && !map.get("max_price").isEmpty()) {
            int maxPrice = Integer.parseInt(map.get("max_price"));
            spec = spec.and(ProductSpecifications.priceLesserOrEqualsThan(maxPrice));
            filterDefinition.append("&max_price=").append(maxPrice);
        }
        if (map.containsKey("title") && !map.get("title").isEmpty()) {
            String title = map.get("title");
            spec = spec.and(ProductSpecifications.titleLike(title));
            filterDefinition.append("&title=").append(title);
        }
        //если нам придут категории или пустой список категорий
        if (categories != null && !categories.isEmpty()){
            Specification specCategories = null;//формируем спеку для категорий
            for (Category c : categories){//пробегаемся по списку категорий
                if (specCategories == null){//если не одна не была выбрана то мы указываем ее
                    specCategories = ProductSpecifications.categoryIs(c);
                }else {//в другом случае мы все категории что были выбраны склеиваем через или(or)
                    specCategories = specCategories.or(ProductSpecifications.categoryIs(c));
                }
            }
            //к вышеуказанным параметрам соединенным через и(and) обавляем какие то категории если их указали
            spec = spec.and(specCategories);
        }
    }
}
