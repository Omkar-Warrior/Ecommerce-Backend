package com.ecommerce.project.service;

import com.ecommerce.project.dto.ProductDto;
import com.ecommerce.project.dto.ProductResponse;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;



@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDto addProduct(Long categoryId, ProductDto productDto) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDto.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if (isProductNotPresent) {
            Product product = modelMapper.map(productDto, Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            Double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(product, ProductDto.class);
        } else {
            throw new APIException("Product already exist!!!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize, sortByAndSortOrder);
        Page<Product> productsPage= productRepository.findAll(pageDetails);

        List<Product> products=productsPage.getContent();
        List<ProductDto> productDtos=products.stream()
                .map(product -> modelMapper.map(product,ProductDto.class))
                .toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDtos);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalElements(productsPage.getTotalElements());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setLastPage(productsPage.isLast());

        return productResponse;

    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category","categoryId",categoryId));

        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize, sortByAndSortOrder);
        Page<Product> productsPage= productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);

        List<Product> products=productsPage.getContent();

        List<ProductDto> productDtos=products.stream()
                .map(product -> modelMapper.map(product,ProductDto.class))
                .toList();

        if (products.isEmpty()){
            throw new APIException("Category does not have any Products ");
        }

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDtos);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalElements(productsPage.getTotalElements());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setLastPage(productsPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndSortOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize, sortByAndSortOrder);
        Page<Product> productsPage= productRepository.findByProductNameLikeIgnoreCase( '%' +keyword+ '%',pageDetails);


            List<Product> products=productsPage.getContent();
            List<ProductDto> productDtos=products.stream()
                    .map(product -> modelMapper.map(product,ProductDto.class))
                    .toList();

            if (products.isEmpty()){
                throw new APIException("Product Not Found with the Keyword "+keyword);
            }

            ProductResponse productResponse=new ProductResponse();
            productResponse.setContent(productDtos);
            productResponse.setPageNumber(productsPage.getNumber());
            productResponse.setPageSize(productsPage.getSize());
            productResponse.setTotalElements(productsPage.getTotalElements());
            productResponse.setTotalPages(productsPage.getTotalPages());
            productResponse.setLastPage(productsPage.isLast());
            return productResponse;

    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        //Get existing product from db
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
        //update Product Info with the one in request body
        Product product=modelMapper.map(productDto,Product.class);
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());
        //save to database
        Product savedProduct = productRepository .save(productFromDb);

        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException {
        //Get the product from DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
        //Upload image to server
        //Get the file name of uploaded image

        String fileName = fileService.uploadImage(path,image);
        //Updating the new file name to the product
        productFromDb.setImage(fileName);
        //save updated product
        Product updatedProduct=productRepository.save(productFromDb);
        //return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct, ProductDto.class);
    }




}
