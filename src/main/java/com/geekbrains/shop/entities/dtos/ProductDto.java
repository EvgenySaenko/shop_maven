package com.geekbrains.shop.entities.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@ApiModel(description = "Product dto in the application.")
public interface ProductDto {
    @ApiModelProperty(notes = "Unique identifier of the product. No two products can have the same id.", example = "1", required = true, position = 0)
    Long getId();

    @ApiModelProperty(notes = "Title of the product.", example = "Milk", required = true, position = 1)
    @Size(min = 4, max = 255)
    String getTitle();

    @ApiModelProperty(notes = "Price of the product.", example = "200.00", required = true, position = 2)
    BigDecimal getPrice();
}
