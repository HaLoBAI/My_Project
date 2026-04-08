package com.hairaccent.ai.dto;

import lombok.Data;
import java.util.List;

@Data
public class GenerateRequest {
    private String productId;
    private String productName;
    private String category;
    private String targetMarket;
    private String description;
    private List<PainPointDTO> painPoints;
    private List<RequirementDTO> requirements;
    private List<ComponentDTO> components;
    
    @Data
    public static class PainPointDTO {
        private String content;
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
