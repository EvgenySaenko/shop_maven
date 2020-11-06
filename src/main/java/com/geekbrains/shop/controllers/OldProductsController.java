package com.geekbrains.shop.controllers;


import com.geekbrains.shop.entities.Category;
import com.geekbrains.shop.entities.Product;
import com.geekbrains.shop.services.CategoriesService;
import com.geekbrains.shop.services.ProductsService;
import com.geekbrains.shop.utils.ProductFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
@Deprecated
public class OldProductsController {
    private ProductsService productsService;
    private CategoriesService categoriesService;

    @Autowired
    public OldProductsController(ProductsService productsService, CategoriesService categoriesService) {
        this.productsService = productsService;
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public String showAll(Model model, @RequestParam Map<String, String> requestParams, @RequestParam(name = "categories", required = false) List<Long> categoriesIds) {
        Integer pageNumber = Integer.parseInt(requestParams.getOrDefault("p", "1"));

        List<Category> categoriesFilter = null;
        if (categoriesIds != null) {
            categoriesFilter = categoriesService.getCategoriesByIds(categoriesIds);
        }
        ProductFilter productFilter = new ProductFilter(requestParams, categoriesFilter);
        Page<Product> products = productsService.findAll(productFilter.getSpec(), pageNumber);
        model.addAttribute("products", products);
        model.addAttribute("filterDef", productFilter.getFilterDefinition().toString());
        return "all_products";
    }

    @GetMapping("/add")
    public String showAddForm() {
        return "add_product_form";
    }

    @PostMapping("/add")
    public String saveNewProduct(@ModelAttribute Product product) {
        productsService.saveOrUpdate(product);
        return "redirect:/products/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productsService.findById(id));
        return "edit_product_form";
    }

    @PostMapping("/edit")
    public String modifyProduct(@ModelAttribute Product product) {
        productsService.saveOrUpdate(product);
        return "redirect:/products/";
    }
}