# 工作流详细设计

## 1. 整体工作流

```
┌─────────────────────────────────────────────────────────────────────┐
│                         Step 1: 信息录入                             │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐  ┌────────────┐   │
│  │ 产品基础   │  │ 用户痛点   │  │ 用户需求   │  │ 部件图片   │   │
│  │ 信息录入   │→ │ 需求录入   │→ │ 点录入     │→ │ 上传       │   │
│  └────────────┘  └────────────┘  └────────────┘  └────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────┐
│                    Step 2: AI智能分析与生成                          │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  2.1 提示词智能构建                                           │  │
│  │  - 分析产品特性                                               │  │
│  │  - 整合用户痛点                                               │  │
│  │  - 结合需求点                                                 │  │
│  │  - 参考部件图片                                               │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                ↓                                     │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  2.2 图片生成（并行）                                         │  │
│  │                                                               │  │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │  │
│  │  │ 主图生成    │  │ 细节图生成  │  │ 信息图生成  │         │  │
│  │  │ (前3张)     │  │ (中3张)     │  │ (后4张)     │         │  │
│  │  │             │  │             │  │             │         │  │
│  │  │ ┌─────────┐ │  │ ┌─────────┐ │  │ ┌─────────┐ │         │  │
│  │  │ │ 图1:    │ │  │ │ 图4:    │ │  │ │ 图7:    │ │         │  │
│  │  │ │ 正面    │ │  │ │ 材质细节│ │  │ │ 全配件图│ │         │  │
│  │  │ └─────────┘ │  │ └─────────┘ │  │ └─────────┘ │         │  │
│  │  │ ┌─────────┐ │  │ ┌─────────┐ │  │ ┌─────────┐ │         │  │
│  │  │ │ 图2:    │ │  │ │ 图5:    │ │  │ │ 图8:    │ │         │  │
│  │  │ │ 侧面    │ │  │ │ 做工细节│ │  │ │ 场景图  │ │         │  │
│  │  │ └─────────┘ │  │ └─────────┘ │  │ └─────────┘ │         │  │
│  │  │ ┌─────────┐ │  │ ┌─────────┐ │  │ ┌─────────┐ │         │  │
│  │  │ │ 图3:    │ │  │ │ 图6:    │ │  │ │ 图9:    │ │         │  │
│  │  │ │ 背面    │ │  │ │ 差异化  │ │  │ │ 规格图  │ │         │  │
│  │  │ └─────────┘ │  │ └─────────┘ │  │ └─────────┘ │         │  │
│  │  │             │  │             │  │ ┌─────────┐ │         │  │
│  │  │             │  │             │  │ │ 图10:   │ │         │  │
│  │  │             │  │             │  │ │ 品质图  │ │         │  │
│  │  │             │  │             │  │ └─────────┘ │         │  │
│  │  └─────────────┘  └─────────────┘  └─────────────┘         │  │
│  └──────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────┐
│                    Step 3: 图片处理与优化                            │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐  ┌────────────┐   │
│  │ 质量检查   │→ │ 智能压缩   │→ │ 格式优化   │→ │ 尺寸适配   │   │
│  │ AI审核     │  │ <1MB       │  │ WebP/JPG   │  │ OZON规格   │   │
│  └────────────┘  └────────────┘  └────────────┘  └────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────┐
│                    Step 4: 预览与调整                                │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │  10张图片预览（可拖拽排序）                                 │    │
│  │  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐                  │    │
│  │  │ 图1 │ │ 图2 │ │ 图3 │ │ 图4 │ │ 图5 │                  │    │
│  │  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘                  │    │
│  │  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐                  │    │
│  │  │ 图6 │ │ 图7 │ │ 图8 │ │ 图9 │ │图10 │                  │    │
│  │  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘                  │    │
│  │                                                             │    │
│  │  操作：[重新生成单张] [批量调整] [下载全部]                │    │
│  └────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────┐
│                    Step 5: 导出与上传                                │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐                    │
│  │ 打包下载   │  │ OZON直传   │  │ 历史保存   │                    │
│  │ ZIP文件    │  │ (可选)     │  │ 可复用     │                    │
│  └────────────┘  └────────────┘  └────────────┘                    │
└─────────────────────────────────────────────────────────────────────┘
```

## 2. 详细步骤说明

### Step 1: 信息录入阶段

#### 1.1 产品基础信息录入
**前端页面**: `ProductInfoForm.vue`

**字段清单**:
```javascript
{
  productName: "",           // 产品名称（必填）
  productCategory: "发饰",    // 产品类目（必填，可选：发饰/项链/手链等）
  targetMarket: "俄罗斯",     // 目标市场（必填）
  productDescription: "",     // 产品描述
  price: 0,                   // 价格
  keywords: [],              // 关键词标签
  seoTitle: "",              // SEO标题
  seoDescription: ""         // SEO描述
}
```

#### 1.2 用户痛点录入
**前端页面**: `PainPointsForm.vue`

**功能特性**:
- 支持添加多条痛点
- 每条痛点可标注来源（竞品分析/用户评论/市场调研）
- 可设置优先级（高/中/低）
- 支持批量导入（Excel/CSV）

**数据结构**:
```javascript
{
  painPoints: [
    {
      content: "现有发饰容易滑落，不够牢固",
      source: "竞品分析",
      priority: "high"
    },
    {
      content: "材质容易过敏，不够亲肤",
      source: "用户评论",
      priority: "high"
    },
    {
      content: "款式单一，不够时尚",
      source: "市场调研",
      priority: "medium"
    }
  ]
}
```

#### 1.3 用户需求点录入
**前端页面**: `RequirementsForm.vue`

**数据结构**:
```javascript
{
  requirements: [
    {
      content: "希望发饰可以多场合使用（日常/派对/正式场合）",
      category: "功能性",
      priority: "high"
    },
    {
      content: "希望产品高性价比，价格实惠",
      category: "价格",
      priority: "medium"
    },
    {
      content: "希望包装精美，适合送礼",
      category: "包装",
      priority: "low"
    }
  ]
}
```

#### 1.4 部件图片上传
**前端页面**: `ComponentImagesUpload.vue`

**功能特性**:
- 支持拖拽上传
- 支持批量上传
- 每个部件可上传多张图片（不同角度）
- 实时预览
- 图片裁剪功能
- 自动去背景（可选）

**数据结构**:
```javascript
{
  components: [
    {
      name: "主发夹",
      description: "大号蝴蝶结发夹",
      images: [
        { url: "xxx.jpg", thumbnail: "xxx_thumb.jpg" },
        { url: "xxx2.jpg", thumbnail: "xxx2_thumb.jpg" }
      ]
    },
    {
      name: "小发夹",
      description: "迷你珍珠发夹",
      images: [
        { url: "yyy.jpg", thumbnail: "yyy_thumb.jpg" }
      ]
    },
    {
      name: "发圈",
      description: "弹性丝绒发圈",
      images: [
        { url: "zzz.jpg", thumbnail: "zzz_thumb.jpg" }
      ]
    }
  ]
}
```

### Step 2: AI智能分析与生成

#### 2.1 提示词构建引擎
**后端服务**: `AIPromptBuilder.java`

```java
public class AIPromptBuilder {
    
    /**
     * 构建主图提示词（前3张）
     */
    public String buildMainImagePrompt(ProductInfo product, int imageIndex) {
        StringBuilder prompt = new StringBuilder();
        
        // 基础描述
        prompt.append("Professional e-commerce product photography. ");
        prompt.append("A beautiful European woman with elegant Russian aesthetic. ");
        
        // 年龄和风格（根据目标市场调整）
        prompt.append("Age 25-35, natural makeup, long flowing hair. ");
        
        // 佩戴场景
        prompt.append(String.format("Wearing %s hair accessory set. ", 
            product.getName()));
        
        // 产品描述整合
        prompt.append(product.getDescription()).append(". ");
        
        // 根据图片序号调整角度
        switch (imageIndex) {
            case 1:
                prompt.append("Front view, direct eye contact with camera. ");
                break;
            case 2:
                prompt.append("Side profile view, showcasing hair accessories from side angle. ");
                break;
            case 3:
                prompt.append("Three-quarter back view, showing accessories in hair from behind. ");
                break;
        }
        
        // 风格设定
        prompt.append("Style: Natural, elegant, feminine, soft romantic. ");
        prompt.append("Lighting: Soft window light, professional studio setup. ");
        prompt.append("Background: Soft gradient, neutral tones (cream, light gray, soft pink). ");
        prompt.append("Mood: Confident, elegant, relatable to Russian women. ");
        
        // 质量要求
        prompt.append("High quality, 4K, detailed, sharp focus on accessories, ");
        prompt.append("professional commercial photography, suitable for e-commerce platform. ");
        
        return prompt.toString();
    }
    
    /**
     * 构建细节图提示词（中3张）
     */
    public String buildDetailImagePrompt(ProductInfo product, 
                                        List<PainPoint> painPoints,
                                        List<Requirement> requirements,
                                        int imageIndex) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("High-density information e-commerce detail image. ");
        prompt.append("Professional product photography layout. ");
        
        switch (imageIndex) {
            case 4:
                // 材质细节图
                prompt.append("Close-up material details. ");
                prompt.append("Show: fabric texture, metal finish, pearl luster. ");
                prompt.append("Multiple close-up shots arranged in grid layout. ");
                
                // 解决的痛点：材质过敏问题
                prompt.append("Highlight: hypoallergenic materials, skin-friendly, premium quality. ");
                break;
                
            case 5:
                // 做工细节图
                prompt.append("Craftsmanship and construction details. ");
                prompt.append("Show: stitching quality, secure clips, reinforced points. ");
                
                // 解决的痛点：容易滑落
                prompt.append("Highlight: strong grip, non-slip design, durable construction. ");
                break;
                
            case 6:
                // 差异化卖点图
                prompt.append("Comparison and unique selling points. ");
                prompt.append("Before/after or comparison layout. ");
                
                // 整合需求点
                for (Requirement req : requirements) {
                    if (req.getPriority().equals("high")) {
                        prompt.append(String.format("Feature: %s. ", req.getContent()));
                    }
                }
                break;
        }
        
        // 通用样式
        prompt.append("Style: Clean, modern, informative. ");
        prompt.append("Layout: Multiple images combined, high information density. ");
        prompt.append("Text elements: Bilingual labels (English and Russian), icons, arrows. ");
        prompt.append("Background: White or light gray. ");
        prompt.append("Quality: Sharp, detailed, professional product photography. ");
        
        return prompt.toString();
    }
    
    /**
     * 构建信息图提示词（后4张）
     */
    public String buildInfoImagePrompt(ProductInfo product, 
                                      List<Component> components,
                                      int imageIndex) {
        StringBuilder prompt = new StringBuilder();
        
        switch (imageIndex) {
            case 7:
                // 全配件平铺图
                prompt.append("Flat lay product photography. ");
                prompt.append("All hair accessory set components arranged neatly. ");
                prompt.append(String.format("Items: %s. ", 
                    components.stream()
                        .map(Component::getName)
                        .collect(Collectors.joining(", "))));
                prompt.append("Layout: Organized grid or circular arrangement. ");
                prompt.append("Background: Pure white. ");
                prompt.append("Lighting: Even, shadowless, top-down. ");
                prompt.append("Show quantities clearly. ");
                break;
                
            case 8:
                // 使用场景图
                prompt.append("Lifestyle scene showcasing usage scenarios. ");
                prompt.append("Multiple mini scenes in one image: ");
                prompt.append("- Daily casual wear, ");
                prompt.append("- Party/event styling, ");
                prompt.append("- Formal occasion, ");
                prompt.append("- Gift giving scene. ");
                prompt.append("Style: Collage layout, modern, aspirational. ");
                break;
                
            case 9:
                // 规格参数图
                prompt.append("Product specification infographic. ");
                prompt.append("Information: ");
                prompt.append("- Dimensions (with ruler/measurement), ");
                prompt.append("- Materials breakdown, ");
                prompt.append("- Color options, ");
                prompt.append("- Package contents, ");
                prompt.append("- Care instructions. ");
                prompt.append("Layout: Clean infographic style, icons and text. ");
                prompt.append("Language: Bilingual (English and Russian). ");
                break;
                
            case 10:
                // 品质保证图
                prompt.append("Quality assurance and trust-building image. ");
                prompt.append("Elements: ");
                prompt.append("- Quality certification badges, ");
                prompt.append("- Satisfaction guarantee, ");
                prompt.append("- Customer reviews/ratings visualization, ");
                prompt.append("- Return policy, ");
                prompt.append("- Packaging quality showcase. ");
                prompt.append("Style: Professional, trustworthy, modern design. ");
                break;
        }
        
        prompt.append("High quality, detailed, professional e-commerce design. ");
        
        return prompt.toString();
    }
}
```

#### 2.2 AI生成任务调度
**后端服务**: `AIGenerationScheduler.java`

```java
@Service
public class AIGenerationScheduler {
    
    @Autowired
    private AIPromptBuilder promptBuilder;
    
    @Autowired
    private ImageGenerationClient aiClient;
    
    @Autowired
    private TaskRepository taskRepository;
    
    /**
     * 启动完整生成任务
     */
    public String startGeneration(String productId) {
        // 创建任务
        AIGenerationTask task = new AIGenerationTask();
        task.setId(UUID.randomUUID().toString());
        task.setProductId(productId);
        task.setStatus("PROCESSING");
        taskRepository.save(task);
        
        // 异步执行生成
        CompletableFuture.runAsync(() -> {
            try {
                generateAllImages(task);
            } catch (Exception e) {
                handleError(task, e);
            }
        });
        
        return task.getId();
    }
    
    /**
     * 生成所有图片
     */
    private void generateAllImages(AIGenerationTask task) {
        ProductInfo product = productService.getById(task.getProductId());
        List<PainPoint> painPoints = painPointService.getByProductId(task.getProductId());
        List<Requirement> requirements = requirementService.getByProductId(task.getProductId());
        List<Component> components = componentService.getByProductId(task.getProductId());
        
        List<String> generatedUrls = new ArrayList<>();
        
        // 生成主图（前3张）- 可并行
        updateProgress(task, 10, "生成主图中...");
        for (int i = 1; i <= 3; i++) {
            String prompt = promptBuilder.buildMainImagePrompt(product, i);
            String imageUrl = aiClient.generateImage(prompt);
            generatedUrls.add(imageUrl);
            updateProgress(task, 10 + i * 10, String.format("主图 %d/3 完成", i));
        }
        
        // 生成细节图（中3张）
        updateProgress(task, 40, "生成细节图中...");
        for (int i = 4; i <= 6; i++) {
            String prompt = promptBuilder.buildDetailImagePrompt(product, painPoints, requirements, i);
            String imageUrl = aiClient.generateImage(prompt);
            generatedUrls.add(imageUrl);
            updateProgress(task, 40 + (i-3) * 10, String.format("细节图 %d/3 完成", i-3));
        }
        
        // 生成信息图（后4张）
        updateProgress(task, 70, "生成信息图中...");
        for (int i = 7; i <= 10; i++) {
            String prompt = promptBuilder.buildInfoImagePrompt(product, components, i);
            String imageUrl = aiClient.generateImage(prompt);
            generatedUrls.add(imageUrl);
            updateProgress(task, 70 + (i-6) * 5, String.format("信息图 %d/4 完成", i-6));
        }
        
        // 保存结果
        saveGeneratedImages(task, generatedUrls);
        
        // 标记完成
        task.setStatus("COMPLETED");
        task.setProgress(100);
        task.setCompletedAt(LocalDateTime.now());
        taskRepository.save(task);
    }
}
```

### Step 3: 图片处理与优化

#### 3.1 智能压缩算法
**后端服务**: `ImageCompressionService.java`

```java
@Service
public class ImageCompressionService {
    
    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1MB for OZON
    
    /**
     * 智能压缩至目标大小
     */
    public byte[] compressToTargetSize(byte[] originalImage, long maxSize) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(originalImage));
            
            // 1. 尝试不同质量等级
            for (int quality = 95; quality >= 60; quality -= 5) {
                byte[] compressed = compressWithQuality(img, quality);
                if (compressed.length <= maxSize) {
                    return compressed;
                }
            }
            
            // 2. 如果还是太大，降低分辨率
            double scale = Math.sqrt((double) maxSize / originalImage.length);
            int newWidth = (int) (img.getWidth() * scale);
            int newHeight = (int) (img.getHeight() * scale);
            
            BufferedImage resized = resize(img, newWidth, newHeight);
            
            // 3. 再次压缩
            for (int quality = 90; quality >= 60; quality -= 5) {
                byte[] compressed = compressWithQuality(resized, quality);
                if (compressed.length <= maxSize) {
                    return compressed;
                }
            }
            
            // 4. 最后尝试更小的尺寸
            return compressWithQuality(resize(resized, newWidth * 9 / 10, newHeight * 9 / 10), 75);
            
        } catch (Exception e) {
            throw new ImageProcessingException("图片压缩失败", e);
        }
    }
    
    /**
     * 按质量压缩
     */
    private byte[] compressWithQuality(BufferedImage img, int quality) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality / 100f);
        
        writer.setOutput(ImageIO.createImageOutputStream(baos));
        writer.write(null, new IIOImage(img, null, null), param);
        writer.dispose();
        
        return baos.toByteArray();
    }
    
    /**
     * 调整尺寸
     */
    private BufferedImage resize(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }
}
```

### Step 4: 预览与调整

#### 4.1 前端预览组件
**前端组件**: `ImagePreviewGrid.vue`

**功能特性**:
- 10张图片网格展示
- 拖拽排序
- 单张重新生成
- 批量操作（全部重新生成/删除）
- 大图预览
- 下载单张/全部

### Step 5: 导出与上传

#### 5.1 批量导出
- ZIP打包下载
- 按序号命名（1.jpg - 10.jpg）
- 包含产品信息JSON文件

#### 5.2 OZON直传（可选）
- 集成OZON API
- 自动上传到OZON商品
- 返回上传状态

## 3. 时间估算

| 步骤 | 预计耗时 |
|------|----------|
| 信息录入 | 5-10分钟 |
| AI生成（10张图） | 3-5分钟 |
| 图片压缩优化 | 30秒-1分钟 |
| 预览调整 | 2-5分钟 |
| 导出下载 | 10秒 |
| **总计** | **约10-20分钟** |

## 4. 成本估算（按API调用）

假设使用某AI图片生成服务：
- 单张图片生成成本：$0.02 - $0.10
- 10张图片总成本：$0.20 - $1.00
- 月生成100个产品（1000张图）：$20 - $100

## 5. 质量保证

### 5.1 自动质量检查
- 分辨率检查（不低于1000px）
- 文件大小检查（<1MB）
- 图片清晰度检测
- 人脸识别（主图必须包含人脸）
- 产品识别（必须包含发饰元素）

### 5.2 人工审核
- 生成后人工确认
- 不满意可单张或全部重新生成
- 保存历史版本

## 6. 优化建议

### 6.1 短期优化
- 增加提示词模板库
- 支持用户自定义提示词
- 增加图片后期编辑功能

### 6.2 长期优化
- 训练定制化AI模型
- 增加视频生成功能
- 支持多语言文案生成
- 集成更多电商平台
