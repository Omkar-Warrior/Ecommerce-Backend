package com.ecommerce.project.controller;

import com.ecommerce.project.dto.ProductDto;
import com.ecommerce.project.dto.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
        @Autowired
       private ProductService productService;

    @PostMapping("/admin/categories/product/{categoryId}")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto, @PathVariable Long categoryId){
         ProductDto savedProductDto = productService.addProduct(categoryId,productDto);
         return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(){

        ProductResponse productResponses=productService.getAllProducts();
        return new ResponseEntity<>(productResponses,HttpStatus.OK);

    }

    @GetMapping("/public/categories/products/{categoryId}")
    public ResponseEntity<ProductResponse> getProductByCategoryId(@Valid @PathVariable Long categoryId){
         ProductResponse productResponse = productService.searchByCategory(categoryId);
         return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword){
          ProductResponse productResponse=productService.searchProductByKeyword(keyword);
        return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDto> updateProductById(@Valid @RequestBody ProductDto productDto,@PathVariable Long productId){
       ProductDto updatedProductDto = productService.updateProduct(productId,productDto);
       return new ResponseEntity<>(updatedProductDto,HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDto> deleteProductById(@PathVariable Long productId){
        ProductDto deletedProductDto = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProductDto,HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDto> updateProductImage(@PathVariable Long productId, @RequestParam("image")MultipartFile image) throws IOException {
        ProductDto updatedProduct = productService.updateProductImage(productId,image);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }
}
