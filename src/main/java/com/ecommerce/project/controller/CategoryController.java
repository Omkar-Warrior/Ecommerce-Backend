package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/fetch/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category>categories=categoryService.getAllCategories();
        if (categories.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(categories);
    }


    @PostMapping("/public/add/categories")
     public ResponseEntity<String> createCategory(@RequestBody Category category){
        categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category Created Successfully");
     }

     @DeleteMapping("/admin/categories/{categoryId}")
     public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
         try {
             String status = categoryService.deleteCategory(categoryId);
             return new ResponseEntity<>(status, HttpStatus.OK);
         } catch (ResponseStatusException e) {
             return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
         }
     }

    @PutMapping("/public/update/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category,@PathVariable Long categoryId){
        try {
            Category savedCategory = categoryService.updateCategory(category,categoryId);
            return new ResponseEntity<>("Category Updated with Category id "+categoryId, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
     }
}
