package com.ecommerce.project.controller;

import com.ecommerce.project.dto.CategoryDto;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
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
    public ResponseEntity<CategoryResponse> getAllCategories(){
        CategoryResponse categoryResponse=categoryService.getAllCategories();
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);

    }


    @PostMapping("/public/add/categories")
     public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto savedCategoryDto= categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(savedCategoryDto,HttpStatus.CREATED);
     }

     @DeleteMapping("/admin/categories/{categoryId}")
     public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long categoryId){

             CategoryDto deletedCategory = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(deletedCategory,HttpStatus.OK);
     }

    @PutMapping("/public/update/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable Long categoryId){
            CategoryDto savedCategoryDto = categoryService.updateCategory(categoryDto,categoryId);
            return new ResponseEntity<>(savedCategoryDto,HttpStatus.OK);

     }
}
