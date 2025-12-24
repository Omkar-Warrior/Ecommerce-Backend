package com.ecommerce.project.service;

import com.ecommerce.project.dto.CategoryDto;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//import java.util.ReverseOrderListView;

@Service
public class CategoryServiceimpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category>categories=categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIException("No Category created till now");

        List<CategoryDto> categoryDtos=categories.stream()
                .map(category -> modelMapper.map(category,CategoryDto.class))
                .toList();

        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(categoryDtos);
        return categoryResponse;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category=modelMapper.map(categoryDto,Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(categoryDto.getCategoryName());
        if(categoryFromDB!=null)
             throw new APIException("Category with the name "+category.getCategoryName()+" already exists !!!");
      Category savedCategory= categoryRepository.save(category);
      return modelMapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto deleteCategory(Long categoryId) {

        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {

        Category savedCategory=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        Category category=modelMapper.map(categoryDto,Category.class);
        category.setCategoryId(categoryId);
        savedCategory=categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDto.class);

    }


}
