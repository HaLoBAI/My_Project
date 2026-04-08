package com.hairaccent.ai.service;

import com.hairaccent.ai.dto.GenerateRequest;
import com.hairaccent.ai.dto.GenerateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AI图片生成服务（简化版）
 * 实际使用时需要对接真实的AI服务（OpenAI/Stability AI/Replicate等）
 */
@Slf4j
@Service
public class AIGenerationService {
    
    /**
     * 生成产品图集（10张图）
     */
    public GenerateResponse generateProductImages(GenerateRequest request) {
        log.info("Starting image generation for product: {}", request.getProductId());
        
        String taskId = UUID.randomUUID().toString();
        
        // 构建提示词
        String mainPrompt = buildMainImagePrompt(request);
        String detailPrompt = buildDetailImagePrompt(request);
        String infoPrompt = buildInfoImagePrompt(request);
        
        log.info("Main image prompt: {}", mainPrompt);
        log.info("Detail image prompt: {}", detailPrompt);
        log.info("Info image prompt: {}", infoPrompt);
        
        // 模拟生成图片（实际需要调用AI API）
        List<GenerateResponse.GeneratedImage> images = new ArrayList<>();
        
        // 前3张：主图（美女佩戴效果）
        for (int i = 1; i <= 3; i++) {
            images.add(createMockImage(taskId, "MAIN", i, 
                    "https://placeholder.com/main-" + i + ".jpg"));
        }
        
        // 中3张：细节图
        for (int i = 4; i <= 6; i++) {
            images.add(createMockImage(taskId, "DETAIL", i, 
                    "https://placeholder.com/detail-" + (i-3) + ".jpg"));
        }
        
        // 后4张：信息图
        for (int i = 7; i <= 10; i++) {
            images.add(createMockImage(taskId, "INFO", i, 
                    "https://placeholder.com/info-" + (i-6) + ".jpg"));
        }
        
        GenerateResponse response = new GenerateResponse();
        response.setTaskId(taskId);
        response.setStatus("COMPLETED");
        response.setProgress(100);
        response.setMessage("10张图片生成完成");
        response.setImages(images);
        
        log.info("Image generation completed. Task ID: {}", taskId);
        
        return response;
    }
    
    /**
     * 构建主图提示词（俄罗斯风格美女佩戴）
     */
    private String buildMainImagePrompt(GenerateRequest request) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Professional e-commerce product photography. ");
        prompt.append("A beautiful European woman with elegant Russian aesthetic, age 25-35, ");
        prompt.append("wearing ").append(request.getProductName()).append(" hair accessory set. ");
        
        if (request.getDescription() != null) {
            prompt.append(request.getDescription()).append(". ");
        }
        
        prompt.append("Natural elegant style, soft studio lighting, neutral background. ");
        prompt.append("High quality 4K, commercial photography for OZON platform. ");
        
        return prompt.toString();
    }
    
    /**
     * 构建细节图提示词（产品细节与卖点）
     */
    private String buildDetailImagePrompt(GenerateRequest request) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("High-density information e-commerce detail image. ");
        prompt.append("Product: ").append(request.getProductName()).append(". ");
        
        // 整合痛点
        if (request.getPainPoints() != null && !request.getPainPoints().isEmpty()) {
            prompt.append("Solving problems: ");
            request.getPainPoints().forEach(p -> {
                if ("high".equals(p.getPriority())) {
                    prompt.append(p.getContent()).append("; ");
                }
            });
        }
        
        prompt.append("Multiple close-up shots in grid layout, ");
        prompt.append("showing material quality, craftsmanship, unique features. ");
        prompt.append("Bilingual labels (English and Russian). ");
        
        return prompt.toString();
    }
    
    /**
     * 构建信息图提示词（配件、场景、规格等）
     */
    private String buildInfoImagePrompt(GenerateRequest request) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("E-commerce information graphics for ").append(request.getProductName()).append(". ");
        prompt.append("Include: all components flat lay, usage scenarios, ");
        prompt.append("specifications chart, quality assurance badges. ");
        prompt.append("Clean professional design, high information density. ");
        
        return prompt.toString();
    }
    
    /**
     * 创建模拟图片对象
     */
    private GenerateResponse.GeneratedImage createMockImage(String taskId, String type, int index, String url) {
        GenerateResponse.GeneratedImage image = new GenerateResponse.GeneratedImage();
        image.setImageId(UUID.randomUUID().toString());
        image.setUrl(url);
        image.setType(type);
        image.setIndex(index);
        image.setFileSize(950000L); // 模拟 ~950KB (< 1MB)
        return image;
    }
}
