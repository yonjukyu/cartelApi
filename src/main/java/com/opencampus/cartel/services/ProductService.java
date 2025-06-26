package com.opencampus.cartel.services;

import com.opencampus.cartel.dto.request.ProductCreateRequest;
import com.opencampus.cartel.dto.request.ProductUpdateRequest;
import com.opencampus.cartel.dto.response.ProductResponse;
import com.opencampus.cartel.model.entity.Product;
import com.opencampus.cartel.model.enums.ProductType;
import com.opencampus.cartel.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsByCodeName(request.getCodeName())) {
            throw new RuntimeException("Product code name already exists");
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setCodeName(request.getCodeName());
        product.setProductType(request.getProductType());
        product.setDescription(request.getDescription());
        product.setPricePerUnit(request.getPricePerUnit());
        product.setUnitMeasure(request.getUnitMeasure());
        product.setOriginCountry(request.getOriginCountry());
        product.setPurityLevel(request.getPurityLevel());
        product.setIsAvailable(true);

        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToResponse(product);
    }

    public ProductResponse getProductByCodeName(String codeName) {
        Product product = productRepository.findByCodeName(codeName)
                .orElseThrow(() -> new RuntimeException("Product not found with code name: " + codeName));
        return convertToResponse(product);
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByIsAvailableTrue(pageable);
        return products.map(this::convertToResponse);
    }

    public Page<ProductResponse> getProductsByType(ProductType productType, Pageable pageable) {
        Page<Product> products = productRepository.findByProductTypeAndIsAvailableTrue(productType, pageable);
        return products.map(this::convertToResponse);
    }

    public Page<ProductResponse> searchProductsByName(String name, Pageable pageable) {
        Page<Product> products = productRepository.findByNameContainingAndIsAvailableTrue(name, pageable);
        return products.map(this::convertToResponse);
    }

    public List<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = productRepository.findByPriceRange(minPrice, maxPrice);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByOriginCountry(String country) {
        List<Product> products = productRepository.findByOriginCountry(country);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByMinimumPurity(Integer minPurity) {
        List<Product> products = productRepository.findByMinimumPurity(minPurity);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getProductType() != null) {
            product.setProductType(request.getProductType());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPricePerUnit() != null) {
            product.setPricePerUnit(request.getPricePerUnit());
        }
        if (request.getUnitMeasure() != null) {
            product.setUnitMeasure(request.getUnitMeasure());
        }
        if (request.getOriginCountry() != null) {
            product.setOriginCountry(request.getOriginCountry());
        }
        if (request.getPurityLevel() != null) {
            product.setPurityLevel(request.getPurityLevel());
        }
        if (request.getIsAvailable() != null) {
            product.setIsAvailable(request.getIsAvailable());
        }

        Product updatedProduct = productRepository.save(product);
        return convertToResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setIsAvailable(false);
        productRepository.save(product);
    }

    ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setCodeName(product.getCodeName());
        response.setProductType(product.getProductType());
        response.setDescription(product.getDescription());
        response.setPricePerUnit(product.getPricePerUnit());
        response.setUnitMeasure(product.getUnitMeasure());
        response.setOriginCountry(product.getOriginCountry());
        response.setPurityLevel(product.getPurityLevel());
        response.setIsAvailable(product.getIsAvailable());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}