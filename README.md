# Hair Accessory E-commerce Image Generator

## 项目简介

一个专为OZON平台设计的发饰套装产品图片自动生成系统，支持从原材料图片到完整电商主图的全流程AI生成。

## 系统特性

### 核心功能
- 🎨 AI生成俄罗斯风格欧洲美女佩戴效果图
- 📸 智能产品细节差异化展示
- 🔧 多部件产品组合展示
- 📊 高信息密度电商图生成
- 🗜️ 自动图片压缩至<1MB（OZON平台要求）

### 技术栈
- **后端**: Spring Cloud 微服务架构
  - Gateway: 网关服务
  - Product-Service: 产品管理服务
  - Image-Service: 图片处理服务
  - AI-Service: AI图片生成服务
  - User-Service: 用户管理服务
- **前端**: Vue 3 + Element Plus
- **数据库**: MySQL 8.0 + Redis
- **存储**: MinIO / OSS
- **AI能力**: 集成图像生成模型

## 项目结构

```
hair-accessory-generator/
├── backend/                    # Spring Cloud 后端
│   ├── gateway/               # 网关服务
│   ├── product-service/       # 产品管理
│   ├── image-service/         # 图片处理
│   ├── ai-service/            # AI生成服务
│   ├── user-service/          # 用户管理
│   └── common/                # 公共模块
├── frontend/                   # Vue 前端
│   ├── src/
│   │   ├── views/            # 页面组件
│   │   ├── components/       # 通用组件
│   │   ├── api/              # API接口
│   │   └── store/            # 状态管理
│   └── public/
└── docs/                       # 文档
    ├── api/                   # API文档
    ├── design/                # 设计文档
    └── deploy/                # 部署文档
```

## 工作流设计

### 1. 输入阶段
- 产品基础信息（名称、描述、价格等）
- 用户痛点分析
- 需求点描述
- 各部件图片上传（支持多组）

### 2. 处理阶段
**前3张 - 主图生成**
- AI生成俄罗斯风格欧洲美女模特
- 自然佩戴发饰套装效果
- 符合俄罗斯女性审美偏好

**中3张 - 细节图生成**
- 产品细节特写
- 差异化卖点展示
- 高信息密度排版

**后4张 - 配件与信息图**
- 全部配件展示图
- 使用场景图
- 产品规格参数图
- 品质保证图

### 3. 输出阶段
- 自动压缩至<1MB
- OZON平台规格适配
- 批量下载/一键上传

## 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis 6+
- Maven 3.8+

### 后端启动
```bash
cd backend
# 启动注册中心
cd eureka-server && mvn spring-boot:run

# 启动各微服务
cd gateway && mvn spring-boot:run
cd product-service && mvn spring-boot:run
cd image-service && mvn spring-boot:run
cd ai-service && mvn spring-boot:run
```

### 前端启动
```bash
cd frontend
npm install
npm run dev
```

## API文档

详见 `docs/api/` 目录

## 扩展性设计

系统支持未来扩展到其他产品类目：
- 服装配饰
- 美妆产品
- 家居用品
- 等等

只需配置新的产品模板和AI提示词即可。

## License

MIT License
