package com.hairaccent.ai.dto;

import lombok.Data;
import java.util.List;

@Data
public class GenerateResponse {
    private String taskId;
    private String status;
    private Integer progress;
    private String message;
    private List<GeneratedImage> images;
    
    @Data
    public static class GeneratedImage {
        private String imageId;
        private String url;
        private String type; // MAIN, DETAIL, INFO
        private Integer index; // 1-10
        private Long fileSize;
    }
}
