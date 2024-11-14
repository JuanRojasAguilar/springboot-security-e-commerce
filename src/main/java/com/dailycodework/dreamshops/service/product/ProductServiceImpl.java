package com.dailycodework.dreamshops.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.requests.ProductRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;

  @Override
  public ProductDto addProduct(ProductRequest request) {
    Category category = Optional
        .ofNullable(
            categoryRepository.findByName(request.getCategory().getName()))
        .orElseGet(() -> {
          Category newCategory = new Category(request.getCategory().getName());
          return categoryRepository.save(newCategory);
        });
    request.setCategory(category);
    Product savedProduct = productRepository.save(createProduct(request, category));
    return convertToDto(savedProduct);
  }

  private Product createProduct(ProductRequest request, Category category) {
    return new Product(
        request.getName(),
        request.getBrand(),
        request.getPrice(),
        request.getQuantity(),
        request.getDescription(),
        category);
  }

  @Override
  public ProductDto getProductById(Long id) {
    return productRepository
        .findById(id)
        .map(this::convertToDto)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
  }

  @Override
  public void deleteProductById(Long id) {
    productRepository.findById(id)
        .ifPresentOrElse(
            productRepository::delete,
            () -> {
              throw new ResourceNotFoundException("Product Not Found");
            });
  }

  @Override
  public ProductDto updateProduct(ProductRequest request, Long productId) {
    return productRepository.findById(productId)
        .map(existingProduct -> updateExistingProduct(existingProduct, request))
        .map(productRepository::save)
        .map(this::convertToDto)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
  }

  private Product updateExistingProduct(Product existingProduct, ProductRequest request) {
    existingProduct.setName(request.getName());
    existingProduct.setBrand(request.getBrand());
    existingProduct.setPrice(request.getPrice());
    existingProduct.setQuantity(request.getQuantity());
    existingProduct.setDescription(request.getDescription());

    Category category = categoryRepository.findByName(request.getCategory().getName());
    existingProduct.setCategory(category);
    return existingProduct;
  }

  @Override
  public List<ProductDto> getAllProducts() {
    List<Product> products = productRepository.findAll();
    return convertToListDto(products);
  }

  @Override
  public List<ProductDto> getProductsByCategory(String category) {
    List<Product> products = productRepository.findByCategoryName(category);
    return convertToListDto(products);
  }

  @Override
  public List<ProductDto> getProductsByBrand(String brand) {
    List<Product> products = productRepository.findByBrand(brand);
    return convertToListDto(products);
  }

  @Override
  public List<ProductDto> getProductsByCategoryAndBrand(String category, String brand) {
    List<Product> products = productRepository.findByCategoryNameAndBrand(category, brand);

    return convertToListDto(products);
  }

  @Override
  public List<ProductDto> getProductsByName(String name) {
    List<Product> products = productRepository.findByName(name);
    return convertToListDto(products);
  }

  @Override
  public List<ProductDto> getProductByBrandAndName(String brand, String name) {
    List<Product> products = productRepository.findByBrandAndName(brand, name);

    return convertToListDto(products);
  }

  @Override
  public Long countProductsByBrandAndName(String brand, String name) {
    return productRepository.countByBrandAndName(brand, name);
  }

  private List<ProductDto> convertToListDto(List<Product> products) {
    return products.stream().map(this::convertToDto).toList();
  }

  private ProductDto convertToDto(Product product) {
    ProductDto productDto = modelMapper.map(product, ProductDto.class);
    List<Image> images = imageRepository.findByProductId(product.getId());
    List<ImageDto> imageDtos = images.stream()
        .map(image -> modelMapper.map(image, ImageDto.class))
        .toList();
    productDto.setImages(imageDtos);
    return productDto;
  }

}
