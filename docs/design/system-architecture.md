# 系统架构设计文档

## 1. 总体架构

### 1.1 架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                         用户前端 (Vue 3)                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐       │
│  │产品管理  │  │图片上传  │  │AI生成    │  │预览下载  │       │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘       │
└────────────────────────────┬────────────────────────────────────┘
                             │ HTTPS
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                    API Gateway (Spring Cloud Gateway)            │
│              ┌──────────────────────────────────┐               │
│              │  路由、鉴权、限流、日志、熔断    │               │
│              └──────────────────────────────────┘               │
└───┬──────────────┬──────────────┬──────────────┬───────────────┘
    │              │              │              │
    ↓              ↓              ↓              ↓
┌─────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
│ User    │  │ Product  │  │  Image   │  │   AI     │
│ Service │  │ Service  │  │ Service  │  │ Service  │
└────┬────┘  └────┬─────┘  └────┬─────┘  └────┬─────┘
     │            │             │             │
     └────────────┴─────────────┴─────────────┘
                  │
     ┌────────────┴────────────┐
     ↓                         ↓
┌──────────┐            ┌──────────┐
│  MySQL   │            │  Redis   │
│ Database │            │  Cache   │
└──────────┘            └──────────┘
     ↓
┌──────────────────┐
│  MinIO / OSS     │
│  对象存储        │
└──────────────────┘
```

## 2. 微服务设计

### 2.1 Gateway Service (网关服务)
**端口**: 8080
**职责**:
- 统一入口，路由转发
- JWT令牌验证
- 请求限流（基于IP和用户）
- 日志记录
- 跨域处理

### 2.2 User Service (用户服务)
**端口**: 8081
**职责**:
- 用户注册登录
- 权限管理
- 用户信息管理
- Token生成验证

**主要接口**:
- POST /api/user/register - 用户注册
- POST /api/user/login - 用户登录
- GET /api/user/info - 获取用户信息
- PUT /api/user/profile - 更新用户信息

### 2.3 Product Service (产品服务)
**端口**: 8082
**职责**:
- 产品信息管理
- 产品类目管理
- 痛点需求管理
- 产品模板管理

**核心数据模型**:
```java
@Entity
@Table(name = "product")
public class Product {
    @Id
    private String id;
    private String name;           // 产品名称
    private String category;       // 类目（发饰/服装等）
    private String description;    // 产品描述
    private BigDecimal price;      // 价格
    private String targetMarket;   // 目标市场（俄罗斯等）
    
    @OneToMany
    private List<ProductComponent> components; // 产品部件
    
    @OneToMany
    private List<UserPainPoint> painPoints;    // 用户痛点
    
    @OneToMany
    private List<UserRequirement> requirements; // 用户需求
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Entity
@Table(name = "product_component")
public class ProductComponent {
    @Id
    private String id;
    private String productId;
    private String name;            // 部件名称
    private String description;     // 部件描述
    private Integer sortOrder;      // 排序
    
    @OneToMany
    private List<String> imageUrls; // 部件图片URLs
}

@Entity
@Table(name = "user_pain_point")
public class UserPainPoint {
    @Id
    private String id;
    private String productId;
    private String content;         // 痛点内容
    private String source;          // 来源（竞品分析/用户反馈等）
    private Integer priority;       // 优先级
}

@Entity
@Table(name = "user_requirement")
public class UserRequirement {
    @Id
    private String id;
    private String productId;
    private String content;         // 需求内容
    private String category;        // 需求分类
    private Integer priority;       // 优先级
}
```

**主要接口**:
- POST /api/product - 创建产品
- GET /api/product/{id} - 获取产品详情
- PUT /api/product/{id} - 更新产品
- DELETE /api/product/{id} - 删除产品
- GET /api/product/list - 产品列表
- POST /api/product/{id}/component - 添加部件
- POST /api/product/{id}/painpoint - 添加痛点
- POST /api/product/{id}/requirement - 添加需求

### 2.4 Image Service (图片处理服务)
**端口**: 8083
**职责**:
- 图片上传管理
- 图片压缩处理
- 图片格式转换
- 缩略图生成
- 存储管理（MinIO/OSS）

**核心功能**:
```java
public class ImageProcessingService {
    // 上传部件图片
    public String uploadComponentImage(MultipartFile file, String productId, String componentId);
    
    // 压缩图片至指定大小（<1MB for OZON）
    public byte[] compressImage(byte[] imageData, long maxSizeBytes);
    
    // 批量压缩
    public List<String> batchCompress(List<String> imageUrls, long maxSizeBytes);
    
    // 生成缩略图
    public String generateThumbnail(String imageUrl, int width, int height);
}
```

**主要接口**:
- POST /api/image/upload - 上传图片
- POST /api/image/compress - 压缩图片
- POST /api/image/batch-compress - 批量压缩
- GET /api/image/{id} - 获取图片
- DELETE /api/image/{id} - 删除图片

### 2.5 AI Service (AI生成服务)
**端口**: 8084
**职责**:
- AI图片生成调度
- 提示词工程管理
- 生成任务队列
- 结果管理

**核心工作流**:
```java
public class AIImageGenerationService {
    
    /**
     * 生成完整产品图集（10张图）
     */
    public ProductImageSet generateProductImages(ProductImageRequest request) {
        // 1. 生成前3张主图（美女佩戴效果）
        List<String> mainImages = generateMainImages(request);
        
        // 2. 生成中3张细节图
        List<String> detailImages = generateDetailImages(request);
        
        // 3. 生成后4张信息图
        List<String> infoImages = generateInfoImages(request);
        
        // 4. 批量压缩至<1MB
        List<String> compressedImages = compressAll(
            mainImages, detailImages, infoImages
        );
        
        return new ProductImageSet(compressedImages);
    }
    
    /**
     * 生成主图（俄罗斯风格美女佩戴）
     */
    private List<String> generateMainImages(ProductImageRequest request) {
        // 提示词构建
        String prompt = buildMainImagePrompt(
            request.getProductInfo(),
            request.getComponentImages(),
            "Russian style", // 俄罗斯风格
            "European woman" // 欧洲美女
        );
        
        // 调用AI生成（3张不同角度）
        return aiClient.generateImages(prompt, 3);
    }
    
    /**
     * 生成细节图（高信息密度）
     */
    private List<String> generateDetailImages(ProductImageRequest request) {
        String prompt = buildDetailImagePrompt(
            request.getProductInfo(),
            request.getPainPoints(),
            request.getRequirements(),
            request.getComponentImages()
        );
        
        return aiClient.generateImages(prompt, 3);
    }
    
    /**
     * 生成信息图（配件图+使用场景等）
     */
    private List<String> generateInfoImages(ProductImageRequest request) {
        List<String> images = new ArrayList<>();
        
        // 1. 全配件平铺图
        images.add(generateAllComponentsImage(request));
        
        // 2. 使用场景图
        images.add(generateUsageSceneImage(request));
        
        // 3. 规格参数图
        images.add(generateSpecificationImage(request));
        
        // 4. 品质保证图
        images.add(generateQualityAssuranceImage(request));
        
        return images;
    }
}
```

**提示词模板**:
```java
public class PromptTemplates {
    
    // 主图提示词模板（俄罗斯风格）
    public static final String MAIN_IMAGE_TEMPLATE = """
        Professional e-commerce product photography.
        A beautiful European woman with Russian aesthetic preferences,
        %s, wearing an elegant hair accessory set.
        
        Hair accessories description: %s
        
        Style: Natural, elegant, feminine, appealing to Russian market
        Lighting: Soft, professional studio lighting
        Background: Clean, minimal, gradient background
        Angle: %s
        
        High quality, detailed, 4K resolution, commercial photography
        """;
    
    // 细节图提示词模板
    public static final String DETAIL_IMAGE_TEMPLATE = """
        High-density information e-commerce detail image.
        
        Product: %s
        Key differentiators: %s
        Pain points solved: %s
        
        Layout: Multiple detail shots in one image
        Style: Clean, informative, visually appealing
        Elements: Close-up textures, materials, unique features
        Text overlays: Key selling points (in English and Russian)
        
        High information density, professional design
        """;
    
    // 全配件图提示词模板
    public static final String ALL_COMPONENTS_TEMPLATE = """
        E-commerce flat lay product photography.
        All components of a hair accessory set neatly arranged.
        
        Components: %s
        
        Layout: Grid or organized arrangement
        Background: Pure white or light neutral
        Lighting: Even, shadowless
        Style: Clean, professional, detailed
        
        Show: All items, quantities clearly visible
        """;
}
```

**主要接口**:
- POST /api/ai/generate - 生成产品图集
- GET /api/ai/task/{taskId} - 查询生成任务状态
- GET /api/ai/result/{taskId} - 获取生成结果
- POST /api/ai/regenerate - 重新生成指定图片

## 3. 数据库设计

### 3.1 核心表结构

```sql
-- 产品表
CREATE TABLE product (
    id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(64) NOT NULL,
    description TEXT,
    price DECIMAL(10,2),
    target_market VARCHAR(64),
    status VARCHAR(32) DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id VARCHAR(64),
    INDEX idx_user_id (user_id),
    INDEX idx_category (category)
);

-- 产品部件表
CREATE TABLE product_component (
    id VARCHAR(64) PRIMARY KEY,
    product_id VARCHAR(64) NOT NULL,
    name VARCHAR(255),
    description TEXT,
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id)
);

-- 部件图片表
CREATE TABLE component_image (
    id VARCHAR(64) PRIMARY KEY,
    component_id VARCHAR(64) NOT NULL,
    image_url VARCHAR(512) NOT NULL,
    thumbnail_url VARCHAR(512),
    sort_order INT DEFAULT 0,
    file_size BIGINT,
    width INT,
    height INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (component_id) REFERENCES product_component(id) ON DELETE CASCADE,
    INDEX idx_component_id (component_id)
);

-- 用户痛点表
CREATE TABLE user_pain_point (
    id VARCHAR(64) PRIMARY KEY,
    product_id VARCHAR(64) NOT NULL,
    content TEXT NOT NULL,
    source VARCHAR(128),
    priority INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id)
);

-- 用户需求表
CREATE TABLE user_requirement (
    id VARCHAR(64) PRIMARY KEY,
    product_id VARCHAR(64) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(64),
    priority INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id)
);

-- AI生成任务表
CREATE TABLE ai_generation_task (
    id VARCHAR(64) PRIMARY KEY,
    product_id VARCHAR(64) NOT NULL,
    status VARCHAR(32) DEFAULT 'PENDING', -- PENDING, PROCESSING, COMPLETED, FAILED
    task_type VARCHAR(32), -- MAIN_IMAGE, DETAIL_IMAGE, INFO_IMAGE
    progress INT DEFAULT 0,
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id),
    INDEX idx_status (status)
);

-- 生成结果图片表
CREATE TABLE generated_image (
    id VARCHAR(64) PRIMARY KEY,
    task_id VARCHAR(64) NOT NULL,
    product_id VARCHAR(64) NOT NULL,
    image_url VARCHAR(512) NOT NULL,
    compressed_url VARCHAR(512),
    image_type VARCHAR(32), -- MAIN, DETAIL, INFO
    image_index INT, -- 1-10
    file_size BIGINT,
    width INT,
    height INT,
    prompt TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES ai_generation_task(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    INDEX idx_task_id (task_id),
    INDEX idx_product_id (product_id)
);

-- 用户表
CREATE TABLE user (
    id VARCHAR(64) PRIMARY KEY,
    username VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(32),
    role VARCHAR(32) DEFAULT 'USER',
    status VARCHAR(32) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
);
```

## 4. Redis缓存设计

```
# 用户Token
user:token:{userId} -> JWT String (TTL: 7天)

# 产品信息缓存
product:info:{productId} -> Product JSON (TTL: 1小时)

# 生成任务状态缓存
ai:task:{taskId} -> Task Status JSON (TTL: 24小时)

# 用户限流
rate:limit:user:{userId} -> Counter (TTL: 1分钟)
rate:limit:ip:{ip} -> Counter (TTL: 1分钟)

# 热门产品缓存
product:hot:list -> List of Product IDs (TTL: 1小时)
```

## 5. 消息队列设计（可选）

如果AI生成耗时较长，可以使用RabbitMQ或Kafka：

```
# 队列设计
- image.generation.queue: AI图片生成任务队列
- image.compression.queue: 图片压缩任务队列
- notification.queue: 任务完成通知队列
```

## 6. 扩展性考虑

### 6.1 支持多类目产品
- 通过配置不同的提示词模板
- 不同类目的图片布局配置
- 灵活的产品属性扩展

### 6.2 支持多平台
- OZON（当前）
- Amazon
- AliExpress
- 其他电商平台

每个平台配置：
- 图片尺寸要求
- 文件大小限制
- 图片数量要求
- 特殊标注规则

### 6.3 AI模型可插拔
- 支持切换不同的AI生成模型
- 统一的AI服务接口
- 模型性能监控

## 7. 安全性设计

- JWT Token认证
- 请求签名验证
- 文件上传安全检查
- SQL注入防护
- XSS防护
- CSRF防护
- 敏感信息加密存储

## 8. 监控告警

- 服务健康检查
- 性能监控（响应时间、吞吐量）
- 错误日志监控
- AI生成成功率监控
- 图片压缩质量监控
