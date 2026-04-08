package com.hairaccent.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hairaccent.product.dto.ProductCreateRequest;
import com.hairaccent.product.entity.*;
import com.hairaccent.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public Product createProduct(String userId, ProductCreateRequest request) {
        log.info("Creating product: {} for user: {}", request.getName(), userId);
        
        Product product = new Product();
        product.setUserId(userId);
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setTargetMarket(request.getTargetMarket());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStatus("DRAFT");
        
        // Convert keywords to JSON
        try {
            if (request.getKeywords() != null) {
                product.setKeywords(objectMapper.writeValueAsString(request.getKeywords()));
            }
        } catch (Exception e) {
            log.error("Failed to convert keywords to JSON", e);
        }
        
        // Add components
        if (request.getComponents() != null) {
            List<ProductComponent> components = new ArrayList<>();
            for (int i = 0; i < request.getComponents().size(); i++) {
                ProductCreateRequest.ComponentDTO dto = request.getComponents().get(i);
                ProductComponent component = new ProductComponent();
                component.setProduct(product);
                component.setName(dto.getName());
                component.setDescription(dto.getDescription());
                component.setSortOrder(i);
                components.add(component);
            }
            product.setComponents(components);
        }
        
        // Add pain points
        if (request.getPainPoints() != null) {
            List<PainPoint> painPoints = new ArrayList<>();
            for (ProductCreateRequest.PainPointDTO dto : request.getPainPoints()) {
                PainPoint painPoint = new PainPoint();
                painPoint.setProduct(product);
                painPoint.setContent(dto.getContent());
                painPoint.setSource(dto.getSource());
                painPoint.setPriority(dto.getPriority());
                painPoints.add(painPoint);
            }
            product.setPainPoints(painPoints);
        }
        
        // Add requirements
        if (request.getRequirements() != null) {
            List<Requirement> requirements = new ArrayList<>();
            for (ProductCreateRequest.RequirementDTO dto : request.getRequirements()) {
                Requirement requirement = new Requirement();
                requirement.setProduct(product);
                requirement.setContent(dto.getContent());
                requirement.setCategory(dto.getCategory());
                requirement.setPriority(dto.getPriority());
                requirements.add(requirement);
            }
            product.setRequirements(requirements);
        }
        
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        
        return savedProduct;
    }
    
    public Product getProduct(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
    }
    
    public List<Product> getUserProducts(String userId) {
        return productRepository.findByUserId(userId);
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Transactional
    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
        log.info("Product deleted: {}", productId);
    }
}
