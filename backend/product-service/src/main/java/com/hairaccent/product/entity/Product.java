package com.hairaccent.product.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String category;
    
    @Column(name = "target_market")
    private String targetMarket;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private BigDecimal price;
    
    @Column(length = 10)
    private String currency = "RUB";
    
    @Column(columnDefinition = "JSON")
    private String keywords;
    
    @Column(name = "seo_title")
    private String seoTitle;
    
    @Column(name = "seo_description", columnDefinition = "TEXT")
    private String seoDescription;
    
    @Column(length = 32)
    private String status = "DRAFT";
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductComponent> components;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PainPoint> painPoints;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Requirement> requirements;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
