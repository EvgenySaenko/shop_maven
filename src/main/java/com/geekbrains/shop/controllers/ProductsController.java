package com.geekbrains.shop.controllers;


import com.geekbrains.shop.entities.Product;
import com.geekbrains.shop.entities.dtos.ProductDto;
import com.geekbrains.shop.exceptions.ResourceNotFoundException;
import com.geekbrains.shop.services.ProductsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
@Api("Set of endpoints for CRUD operations for Products")
@AllArgsConstructor
public class ProductsController {
    private ProductsService productsService;

    @GetMapping(produces = "application/json")
    @ApiOperation("Returns list of all products")
    public List<ProductDto> getAllProducts() {
        return productsService.findAllDtos();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ApiOperation("Returns one product by id")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "demo", type = "String", required = false, paramType = "query")
//    })
    public ResponseEntity<?> getOneProduct(@PathVariable @ApiParam("Id of the product to be requested. Cannot be empty") Long id) {
        if (!productsService.existsById(id)) {
            throw new ResourceNotFoundException("Product not found, id: " + id);
        }
        return new ResponseEntity<>(productsService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping
    @ApiOperation("Removes all products")
    public void deleteAllProducts() {
        productsService.deleteAll();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletes a product from the system. 404 if the product's identifier is not found.")
    public void deleteOneProduct(@PathVariable Long id) {
        if (!productsService.existsById(id)) {
            throw new ResourceNotFoundException("Product with id: " + id + " doesn't exists (for delete)");
        }
        productsService.deleteById(id);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Creates a new product. If id != null, then it will be cleared")
    public Product saveNewProduct(@RequestBody Product product) {
        if (product.getId() != null) {
            product.setId(null);
        }
        return productsService.saveOrUpdate(product);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    @ApiOperation("Modifies an existing product")
    public ResponseEntity<?> modifyProduct(@RequestBody Product product) {
        if (product.getId() == null || !productsService.existsById(product.getId())) {
            throw new ResourceNotFoundException("Product not found, id: " + product.getId());
        }
        if (product.getPrice().doubleValue() < 0.0) {
            return new ResponseEntity<>("Product's price can not be negative", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productsService.saveOrUpdate(product), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(ResourceNotFoundException exc) {
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
    }
}