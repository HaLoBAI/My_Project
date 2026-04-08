# Spring Boot 后端实现

## 🎯 已实现的功能

### ✅ Product Service (产品服务) - 端口 8082

**功能**:
- ✅ 创建产品（包含痛点、需求、部件）
- ✅ 获取产品详情
- ✅ 获取用户产品列表
- ✅ 删除产品

**API接口**:
```bash
# 创建产品
POST http://localhost:8082/api/product
Content-Type: application/json

{
  "name": "俄罗斯风格发饰套装",
  "category": "发饰",
  "targetMarket": "俄罗斯",
  "description": "优雅的蝴蝶结发饰套装",
  "price": 599.00,
  "keywords": ["发饰", "蝴蝶结", "俄罗斯"],
  "painPoints": [
    {
      "content": "现有发饰容易滑落",
      "source": "竞品分析",
      "priority": "high"
    }
  ],
  "requirements": [
    {
      "content": "希望可以多场合使用",
      "category": "功能性",
      "priority": "high"
    }
  ],
  "components": [
    {
      "name": "主发夹",
      "description": "大号蝴蝶结发夹",
      "imageUrls": ["http://example.com/image1.jpg"]
    }
  ]
}

# 获取产品详情
GET http://localhost:8082/api/product/{productId}

# 获取用户产品列表
GET http://localhost:8082/api/product/user

# 删除产品
DELETE http://localhost:8082/api/product/{productId}
```

### ✅ AI Service (AI生成服务) - 端口 8084

**功能**:
- ✅ AI图片生成（10张图：3主图+3细节+4信息图）
- ✅ 智能提示词构建
- ✅ 生成任务状态查询

**API接口**:
```bash
# 开始AI图片生成
POST http://localhost:8084/api/ai/generate
Content-Type: application/json

{
  "productId": "产品ID",
  "productName": "俄罗斯风格发饰套装",
  "category": "发饰",
  "targetMarket": "俄罗斯",
  "description": "优雅的蝴蝶结发饰套装",
  "painPoints": [
    {
      "content": "现有发饰容易滑落",
      "priority": "high"
    }
  ],
  "requirements": [
    {
      "content": "希望可以多场合使用",
      "category": "功能性",
      "priority": "high"
    }
  ],
  "components": [
    {
      "name": "主发夹",
      "description": "大号蝴蝶结发夹",
      "imageUrls": ["http://example.com/image1.jpg"]
    }
  ]
}

# 查询任务状态
GET http://localhost:8084/api/ai/task/{taskId}
```

## 🚀 快速启动

### 1. 前置准备

```bash
# 确保已安装
- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+

# 初始化数据库
mysql -u root -p < ../init-database.sql
```

### 2. 启动Product Service

```bash
cd product-service
mvn clean install
mvn spring-boot:run
```

访问: http://localhost:8082

### 3. 启动AI Service

```bash
cd ai-service
mvn clean install
mvn spring-boot:run
```

访问: http://localhost:8084

## 📝 测试示例

### 完整流程测试

```bash
# 1. 创建产品
curl -X POST http://localhost:8082/api/product \
  -H "Content-Type: application/json" \
  -H "X-User-Id: test-user-001" \
  -d '{
    "name": "俄罗斯风格发饰套装",
    "category": "发饰",
    "targetMarket": "俄罗斯",
    "description": "优雅的蝴蝶结发饰套装，包含主发夹、迷你发夹、发圈",
    "price": 599.00,
    "keywords": ["发饰", "蝴蝶结", "俄罗斯风格"],
    "painPoints": [
      {
        "content": "现有发饰容易滑落，固定不牢",
        "source": "竞品分析",
        "priority": "high"
      },
      {
        "content": "材质容易过敏，不够亲肤",
        "source": "用户评论",
        "priority": "high"
      }
    ],
    "requirements": [
      {
        "content": "希望发饰可以多场合使用（日常/派对/正式场合）",
        "category": "功能性",
        "priority": "high"
      }
    ],
    "components": [
      {
        "name": "主发夹",
        "description": "大号蝴蝶结发夹",
        "imageUrls": ["http://example.com/component1.jpg"]
      },
      {
        "name": "迷你发夹",
        "description": "迷你珍珠发夹",
        "imageUrls": ["http://example.com/component2.jpg"]
      }
    ]
  }'

# 2. 获取产品ID（从上面的响应中）
PRODUCT_ID="刚创建的产品ID"

# 3. 调用AI生成图片
curl -X POST http://localhost:8084/api/ai/generate \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "'$PRODUCT_ID'",
    "productName": "俄罗斯风格发饰套装",
    "category": "发饰",
    "targetMarket": "俄罗斯",
    "description": "优雅的蝴蝶结发饰套装",
    "painPoints": [
      {
        "content": "现有发饰容易滑落",
        "priority": "high"
      }
    ],
    "requirements": [
      {
        "content": "希望可以多场合使用",
        "category": "功能性",
        "priority": "high"
      }
    ],
    "components": [
      {
        "name": "主发夹",
        "description": "大号蝴蝶结发夹",
        "imageUrls": ["http://example.com/component1.jpg"]
      }
    ]
  }'

# 4. 查看生成结果（会返回10张图片的URL）
```

## ⚙️ 配置说明

### Product Service 配置

编辑 `product-service/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hair_accessory_db
    username: hair_app
    password: 你的密码
```

### AI Service 配置

编辑 `ai-service/src/main/resources/application.yml`:

```yaml
ai:
  service:
    provider: mock  # 当前是模拟版本
    # 实际使用时改为: openai, stability, replicate等
    api-key: 你的AI_API_KEY
```

## 🔧 当前实现说明

### ✅ 已实现
- Product Service完整功能
- AI Service基础框架
- 智能提示词构建引擎
- 统一响应格式
- JPA实体映射
- MySQL数据持久化

### 🚧 待完善（需要实际对接）
- 真实的AI图片生成API调用
- 图片压缩服务
- 图片存储（MinIO/OSS）
- Gateway网关服务
- User Service用户服务
- Redis缓存
- 认证授权

## 📚 项目结构

```
backend/
├── common/                  # 公共模块
│   └── src/main/java/
│       └── com/hairaccent/common/
│           └── dto/
│               └── Result.java    # 统一响应
│
├── product-service/         # 产品服务 (8082)
│   └── src/main/java/
│       └── com/hairaccent/product/
│           ├── controller/   # REST API
│           ├── service/      # 业务逻辑
│           ├── repository/   # 数据访问
│           ├── entity/       # 实体类
│           └── dto/          # 数据传输对象
│
└── ai-service/              # AI服务 (8084)
    └── src/main/java/
        └── com/hairaccent/ai/
            ├── controller/   # REST API
            ├── service/      # AI生成逻辑
            └── dto/          # 数据传输对象
```

## 🎉 下一步

1. **测试当前功能**: 启动两个服务，测试API
2. **对接真实AI**: 修改AI Service，对接OpenAI/Stability AI
3. **添加图片服务**: 实现图片上传、压缩、存储
4. **完善其他服务**: Gateway、User Service等
5. **前端集成**: 对接Vue前端

## ❓ 常见问题

**Q: 如何更换AI服务提供商？**

A: 修改 `ai-service/src/main/resources/application.yml` 中的配置，然后实现对应的AI Client

**Q: 当前是真实生成图片吗？**

A: 当前是模拟版本，返回占位符URL。需要对接真实AI API（OpenAI/Stability AI等）

**Q: 数据库密码在哪里改？**

A: 修改各服务的 `application.yml` 文件中的 `spring.datasource.password`
