package com.geekbrains.shop.services;


import com.geekbrains.shop.entities.Product;
import com.geekbrains.shop.entities.dtos.ProductDto;
import com.geekbrains.shop.exceptions.ProductNotFoundException;
import com.geekbrains.shop.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductsService {
    private ProductsRepository productsRepository;

    @Autowired
    public void setProductsRepository(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Product saveOrUpdate(Product product) {
        return productsRepository.save(product);
    }

    public Product findById(Long id) {
        return productsRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Can't found product with id = " + id));
    }

    public List<Product> findAll() {
        return productsRepository.findAll();
    }

    public Page<Product> findAll(Specification<Product> spec, Integer page) {
        if (page < 1L) {
            page = 1;
        }
        return productsRepository.findAll(spec, PageRequest.of(page - 1, 10));
    }

    public void deleteAll(){
        productsRepository.deleteAll();
    }

    public void deleteById(Long id){
        productsRepository.deleteById(id);
    }

    public boolean existsById(Long id){
        return productsRepository.existsById(id);
    }

    public List<ProductDto> getDtoData(){
        return productsRepository.findAllBy();
    }
}
