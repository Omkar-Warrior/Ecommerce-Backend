package com.ecommerce.project.service;

import com.ecommerce.project.dto.CategoryDto;
import com.ecommerce.project.dto.CategoryResponse;


public interface CategoryService {


    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize, String sortBy, String sortOrder);

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto deleteCategory(Long categoryId);

    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);


}
