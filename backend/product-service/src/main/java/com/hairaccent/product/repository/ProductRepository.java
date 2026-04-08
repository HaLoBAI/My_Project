package com.hairaccent.product.repository;

import com.hairaccent.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByUserId(String userId);
    List<Product> findByCategory(String category);
    List<Product> findByStatus(String status);
}
