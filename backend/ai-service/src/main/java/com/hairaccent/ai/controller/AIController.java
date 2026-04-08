package com.hairaccent.ai.controller;

import com.hairaccent.ai.dto.GenerateRequest;
import com.hairaccent.ai.dto.GenerateResponse;
import com.hairaccent.ai.service.AIGenerationService;
import com.hairaccent.common.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {
    
    private final AIGenerationService aiGenerationService;
    
    /**
     * 开始AI图片生成
     */
    @PostMapping("/generate")
    public Result<GenerateResponse> generateImages(@RequestBody GenerateRequest request) {
        try {
            log.info("Received generation request for product: {}", request.getProductId());
            GenerateResponse response = aiGenerationService.generateProductImages(request);
            return Result.success("图片生成完成", response);
        } catch (Exception e) {
            log.error("Failed to generate images", e);
            return Result.error("图片生成失败: " + e.getMessage());
        }
    }
    
    /**
     * 查询生成任务状态（简化版，直接返回完成）
     */
    @GetMapping("/task/{taskId}")
    public Result<GenerateResponse> getTaskStatus(@PathVariable String taskId) {
        GenerateResponse response = new GenerateResponse();
        response.setTaskId(taskId);
        response.setStatus("COMPLETED");
        response.setProgress(100);
        response.setMessage("任务已完成");
        return Result.success(response);
    }
}
