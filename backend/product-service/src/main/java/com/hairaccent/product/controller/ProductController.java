package com.hairaccent.product.controller;

import com.hairaccent.common.dto.Result;
import com.hairaccent.product.dto.ProductCreateRequest;
import com.hairaccent.product.entity.Product;
import com.hairaccent.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * 创建产品
     */
    @PostMapping
    public Result<Product> createProduct(
            @RequestHeader(value = "X-User-Id", defaultValue = "test-user") String userId,
            @RequestBody ProductCreateRequest request) {
        try {
            Product product = productService.createProduct(userId, request);
            return Result.success("产品创建成功", product);
        } catch (Exception e) {
            log.error("Failed to create product", e);
            return Result.error("创建产品失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取产品详情
     */
    @GetMapping("/{id}")
    public Result<Product> getProduct(@PathVariable String id) {
        try {
            Product product = productService.getProduct(id);
            return Result.success(product);
        } catch (Exception e) {
            log.error("Failed to get product: {}", id, e);
            return Result.error("获取产品失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户的所有产品
     */
    @GetMapping("/user")
    public Result<List<Product>> getUserProducts(
            @RequestHeader(value = "X-User-Id", defaultValue = "test-user") String userId) {
        try {
            List<Product> products = productService.getUserProducts(userId);
            return Result.success(products);
        } catch (Exception e) {
            log.error("Failed to get user products", e);
            return Result.error("获取产品列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有产品
     */
    @GetMapping("/list")
    public Result<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return Result.success(products);
        } catch (Exception e) {
            log.error("Failed to get all products", e);
            return Result.error("获取产品列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除产品
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@PathVariable String id) {
        try {
            productService.deleteProduct(id);
            return Result.success("产品删除成功", null);
        } catch (Exception e) {
            log.error("Failed to delete product: {}", id, e);
            return Result.error("删除产品失败: " + e.getMessage());
        }
    }
}
