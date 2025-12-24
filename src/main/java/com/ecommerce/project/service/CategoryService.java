package com.ecommerce.project.service;

import com.ecommerce.project.dto.CategoryDto;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {


    CategoryResponse getAllCategories();

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto deleteCategory(Long categoryId);

    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);


}
