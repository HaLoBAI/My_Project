package com.hairaccent.product.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreateRequest {
    private String name;
    private String category;
    private String targetMarket;
    private String description;
    private BigDecimal price;
    private List<String> keywords;
    private List<PainPointDTO> painPoints;
    private List<RequirementDTO> requirements;
    private List<ComponentDTO> components;
    
    @Data
    public static class PainPointDTO {
        private String content;
        private String source;
        private String priority;
    }
    
    @Data
    public static class RequirementDTO {
        private String content;
        private String category;
        private String priority;
    }
    
    @Data
    public static class ComponentDTO {
        private String name;
        private String description;
        private List<String> imageUrls;
    }
}
