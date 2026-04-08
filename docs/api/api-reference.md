# API 接口文档

## 基础信息

- **Base URL**: `https://api.yourdomain.com`
- **版本**: v1
- **认证方式**: JWT Bearer Token
- **数据格式**: JSON

## 通用响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": "2026-04-08T12:00:00Z"
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "错误描述",
  "error": "详细错误信息",
  "timestamp": "2026-04-08T12:00:00Z"
}
```

## 错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 429 | 请求过于频繁 |
| 500 | 服务器内部错误 |

---

## 1. 用户相关接口

### 1.1 用户注册

**接口**: `POST /api/user/register`

**请求参数**:
```json
{
  "username": "zhangsan",
  "password": "password123",
  "email": "zhangsan@example.com",
  "phone": "+7 123 456 7890"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": "user_123456",
    "username": "zhangsan"
  }
}
```

### 1.2 用户登录

**接口**: `POST /api/user/login`

**请求参数**:
```json
{
  "username": "zhangsan",
  "password": "password123"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 604800,
    "userInfo": {
      "userId": "user_123456",
      "username": "zhangsan",
      "email": "zhangsan@example.com",
      "role": "USER"
    }
  }
}
```

### 1.3 获取用户信息

**接口**: `GET /api/user/info`

**请求头**:
```
Authorization: Bearer <token>
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "userId": "user_123456",
    "username": "zhangsan",
    "email": "zhangsan@example.com",
    "phone": "+7 123 456 7890",
    "role": "USER",
    "createdAt": "2026-01-01T00:00:00Z"
  }
}
```

---

## 2. 产品相关接口

### 2.1 创建产品

**接口**: `POST /api/product`

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**:
```json
{
  "name": "俄罗斯风格发饰套装",
  "category": "发饰",
  "targetMarket": "俄罗斯",
  "description": "优雅的蝴蝶结发饰套装，包含多个配件",
  "price": 599.00,
  "keywords": ["发饰", "蝴蝶结", "俄罗斯风格"],
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
      "images": [
        {
          "url": "https://cdn.example.com/image1.jpg",
          "thumbnail": "https://cdn.example.com/image1_thumb.jpg"
        }
      ]
    }
  ]
}
```

**响应**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "productId": "prod_123456",
    "name": "俄罗斯风格发饰套装",
    "status": "DRAFT",
    "createdAt": "2026-04-08T12:00:00Z"
  }
}
```

### 2.2 获取产品详情

**接口**: `GET /api/product/{productId}`

**请求头**:
```
Authorization: Bearer <token>
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "productId": "prod_123456",
    "name": "俄罗斯风格发饰套装",
    "category": "发饰",
    "targetMarket": "俄罗斯",
    "description": "优雅的蝴蝶结发饰套装",
    "price": 599.00,
    "status": "PUBLISHED",
    "painPoints": [...],
    "requirements": [...],
    "components": [...],
    "generatedImages": [...],
    "createdAt": "2026-04-08T12:00:00Z",
    "updatedAt": "2026-04-08T13:00:00Z"
  }
}
```

### 2.3 获取产品列表

**接口**: `GET /api/product/list`

**请求头**:
```
Authorization: Bearer <token>
```

**查询参数**:
```
page=1
size=20
category=发饰
status=PUBLISHED
keyword=蝴蝶结
sortBy=createdAt
sortOrder=desc
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "total": 100,
    "page": 1,
    "size": 20,
    "items": [
      {
        "productId": "prod_123456",
        "name": "俄罗斯风格发饰套装",
        "category": "发饰",
        "price": 599.00,
        "status": "PUBLISHED",
        "thumbnail": "https://cdn.example.com/thumb.jpg",
        "createdAt": "2026-04-08T12:00:00Z"
      }
    ]
  }
}
```

### 2.4 更新产品

**接口**: `PUT /api/product/{productId}`

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**: 同创建产品

**响应**:
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "productId": "prod_123456",
    "updatedAt": "2026-04-08T14:00:00Z"
  }
}
```

### 2.5 删除产品

**接口**: `DELETE /api/product/{productId}`

**请求头**:
```
Authorization: Bearer <token>
```

**响应**:
```json
{
  "code": 200,
  "message": "删除成功"
}
```

---

## 3. 图片上传接口

### 3.1 上传图片

**接口**: `POST /api/image/upload`

**请求头**:
```
Authorization: Bearer <token>
Content-Type: multipart/form-data
```

**请求参数**:
```
file: <binary>
productId: prod_123456 (optional)
componentId: comp_123 (optional)
```

**响应**:
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "imageId": "img_123456",
    "url": "https://cdn.example.com/uploads/image1.jpg",
    "thumbnail": "https://cdn.example.com/uploads/image1_thumb.jpg",
    "fileSize": 1024000,
    "width": 1920,
    "height": 1080,
    "uploadedAt": "2026-04-08T12:00:00Z"
  }
}
```

### 3.2 批量上传图片

**接口**: `POST /api/image/batch-upload`

**请求头**:
```
Authorization: Bearer <token>
Content-Type: multipart/form-data
```

**请求参数**:
```
files: <binary[]>
productId: prod_123456 (optional)
```

**响应**:
```json
{
  "code": 200,
  "message": "批量上传成功",
  "data": {
    "successCount": 5,
    "failCount": 0,
    "images": [
      {
        "imageId": "img_123456",
        "url": "https://cdn.example.com/uploads/image1.jpg",
        "thumbnail": "https://cdn.example.com/uploads/image1_thumb.jpg"
      }
    ]
  }
}
```

### 3.3 压缩图片

**接口**: `POST /api/image/compress`

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**:
```json
{
  "imageUrl": "https://cdn.example.com/uploads/image1.jpg",
  "maxSizeBytes": 1048576,
  "quality": 85
}
```

**响应**:
```json
{
  "code": 200,
  "message": "压缩成功",
  "data": {
    "originalSize": 2048000,
    "compressedSize": 950000,
    "compressionRatio": 0.464,
    "compressedUrl": "https://cdn.example.com/compressed/image1.jpg"
  }
}
```

### 3.4 批量压缩图片

**接口**: `POST /api/image/batch-compress`

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**:
```json
{
  "imageUrls": [
    "https://cdn.example.com/uploads/image1.jpg",
    "https://cdn.example.com/uploads/image2.jpg"
  ],
  "maxSizeBytes": 1048576
}
```

**响应**:
```json
{
  "code": 200,
  "message": "批量压缩成功",
  "data": {
    "results": [
      {
        "originalUrl": "https://cdn.example.com/uploads/image1.jpg",
        "compressedUrl": "https://cdn.example.com/compressed/image1.jpg",
        "originalSize": 2048000,
        "compressedSize": 950000
      }
    ]
  }
}
```

---

## 4. AI生成接口

### 4.1 开始AI生成任务

**接口**: `POST /api/ai/generate`

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**:
```json
{
  "productId": "prod_123456",
  "productInfo": {
    "name": "俄罗斯风格发饰套装",
    "category": "发饰",
    "targetMarket": "俄罗斯",
    "description": "优雅的蝴蝶结发饰套装"
  },
  "painPoints": [
    {
      "content": "现有发饰容易滑落",
      "priority": "high"
    }
  ],
  "requirements": [
    {
      "content": "希望可以多场合使用",
      "priority": "high"
    }
  ],
  "components": [
    {
      "name": "主发夹",
      "description": "大号蝴蝶结发夹",
      "images": [
        {
          "url": "https://cdn.example.com/component1.jpg"
        }
      ]
    }
  ]
}
```

**响应**:
```json
{
  "code": 200,
  "message": "任务已创建",
  "data": {
    "taskId": "task_123456",
    "status": "PENDING",
    "estimatedTime": 300,
    "createdAt": "2026-04-08T12:00:00Z"
  }
}
```

### 4.2 查询任务状态

**接口**: `GET /api/ai/task/{taskId}`

**请求头**:
```
Authorization: Bearer <token>
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "taskId": "task_123456",
    "productId": "prod_123456",
    "status": "PROCESSING",
    "progress": 45,
    "statusMessage": "正在生成细节图 2/3",
    "logs": [
      {
        "time": "12:00:05",
        "message": "任务开始",
        "type": "info"
      },
      {
        "time": "12:01:20",
        "message": "主图 1/3 完成",
        "type": "success"
      }
    ],
    "images": [],
    "createdAt": "2026-04-08T12:00:00Z",
    "updatedAt": "2026-04-08T12:02:30Z"
  }
}
```

**状态值说明**:
- `PENDING`: 待处理
- `PROCESSING`: 处理中
- `COMPLETED`: 已完成
- `FAILED`: 失败

### 4.3 获取生成结果

**接口**: `GET /api/ai/result/{taskId}`

**请求头**:
```
Authorization: Bearer <token>
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "taskId": "task_123456",
    "status": "COMPLETED",
    "images": [
      {
        "imageId": "gen_img_001",
        "url": "https://cdn.example.com/generated/image1.jpg",
        "compressedUrl": "https://cdn.example.com/generated/image1_compressed.jpg",
        "type": "MAIN",
        "index": 1,
        "fileSize": 980000,
        "width": 1920,
        "height": 1080,
        "prompt": "Professional e-commerce product photography..."
      },
      {
        "imageId": "gen_img_002",
        "url": "https://cdn.example.com/generated/image2.jpg",
        "compressedUrl": "https://cdn.example.com/generated/image2_compressed.jpg",
        "type": "MAIN",
        "index": 2,
        "fileSize": 950000,
        "width": 1920,
        "height": 1080
      }
      // ... 总共10张图片
    ],
    "completedAt": "2026-04-08T12:05:00Z"
  }
}
```

### 4.4 重新生成单张图片

**接口**: `POST /api/ai/regenerate`

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**:
```json
{
  "productId": "prod_123456",
  "imageIndex": 1,
  "imageType": "MAIN",
  "customPrompt": "可选的自定义提示词"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "重新生成成功",
  "data": {
    "imageId": "gen_img_new_001",
    "url": "https://cdn.example.com/generated/image1_new.jpg",
    "compressedUrl": "https://cdn.example.com/generated/image1_new_compressed.jpg",
    "type": "MAIN",
    "index": 1,
    "fileSize": 960000
  }
}
```

### 4.5 下载生成的图片

**接口**: `POST /api/ai/download`

**请求头**:
```
Authorization: Bearer <token>
Content-Type: application/json
```

**请求参数**:
```json
{
  "taskId": "task_123456",
  "imageUrls": [
    "https://cdn.example.com/generated/image1_compressed.jpg",
    "https://cdn.example.com/generated/image2_compressed.jpg"
  ],
  "format": "zip"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "打包成功",
  "data": {
    "zipUrl": "https://cdn.example.com/downloads/product_images_123456.zip",
    "fileSize": 8500000,
    "expiresAt": "2026-04-09T12:00:00Z"
  }
}
```

---

## 5. 统计与分析接口

### 5.1 获取仪表盘数据

**接口**: `GET /api/dashboard/stats`

**请求头**:
```
Authorization: Bearer <token>
```

**查询参数**:
```
startDate=2026-04-01
endDate=2026-04-08
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "totalProducts": 150,
    "publishedProducts": 120,
    "draftProducts": 30,
    "totalGenerations": 1500,
    "successRate": 0.98,
    "averageGenerationTime": 285,
    "storageUsed": 5368709120,
    "recentActivities": [
      {
        "action": "PRODUCT_CREATED",
        "productName": "俄罗斯风格发饰套装",
        "timestamp": "2026-04-08T12:00:00Z"
      }
    ]
  }
}
```

---

## 6. 限流策略

| 接口类型 | 限流规则 |
|---------|----------|
| 图片上传 | 100次/小时/用户 |
| AI生成 | 20次/小时/用户 |
| 查询接口 | 1000次/小时/用户 |
| 其他接口 | 500次/小时/用户 |

---

## 7. Webhook 回调（可选）

当AI生成任务完成时，可以配置Webhook回调通知。

### 配置Webhook

**接口**: `POST /api/user/webhook/config`

**请求参数**:
```json
{
  "url": "https://your-domain.com/webhook/ai-generation",
  "secret": "your_webhook_secret",
  "events": ["GENERATION_COMPLETED", "GENERATION_FAILED"]
}
```

### Webhook 回调格式

**POST** 到配置的URL

**请求头**:
```
Content-Type: application/json
X-Webhook-Signature: sha256=...
```

**请求体**:
```json
{
  "event": "GENERATION_COMPLETED",
  "taskId": "task_123456",
  "productId": "prod_123456",
  "timestamp": "2026-04-08T12:05:00Z",
  "data": {
    "status": "COMPLETED",
    "imageCount": 10,
    "images": [...]
  }
}
```

---

## 8. 测试环境

- **测试Base URL**: `https://api-test.yourdomain.com`
- **测试账号**: test_user / Test123456

## 9. SDK 示例

### JavaScript/TypeScript
```javascript
import axios from 'axios'

const client = axios.create({
  baseURL: 'https://api.yourdomain.com',
  headers: {
    'Authorization': `Bearer ${token}`
  }
})

// 开始生成
const response = await client.post('/api/ai/generate', {
  productId: 'prod_123456',
  productInfo: {...}
})

const taskId = response.data.data.taskId

// 轮询状态
const pollStatus = async () => {
  const result = await client.get(`/api/ai/task/${taskId}`)
  if (result.data.data.status === 'COMPLETED') {
    console.log('Generation completed!', result.data.data.images)
  } else {
    setTimeout(pollStatus, 2000)
  }
}

pollStatus()
```

### Python
```python
import requests
import time

client = requests.Session()
client.headers.update({
    'Authorization': f'Bearer {token}'
})

# 开始生成
response = client.post(
    'https://api.yourdomain.com/api/ai/generate',
    json={
        'productId': 'prod_123456',
        'productInfo': {...}
    }
)

task_id = response.json()['data']['taskId']

# 轮询状态
while True:
    result = client.get(
        f'https://api.yourdomain.com/api/ai/task/{task_id}'
    )
    status = result.json()['data']['status']
    
    if status == 'COMPLETED':
        print('Generation completed!')
        print(result.json()['data']['images'])
        break
    
    time.sleep(2)
```

---

## 10. 更新日志

### v1.0.0 (2026-04-08)
- 初始版本发布
- 支持产品管理
- 支持AI图片生成
- 支持图片压缩优化
