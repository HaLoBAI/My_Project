# 📚 文档导航

## 快速链接

| 文档 | 描述 | 适合人群 |
|------|------|----------|
| [README.md](README.md) | 项目介绍和概览 | 所有人 |
| [QUICKSTART.md](QUICKSTART.md) | 5分钟快速启动指南 | 开发者 |
| [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) | 完整项目总结 | 产品经理、架构师 |

## 📖 详细文档

### 设计文档
- [系统架构设计](docs/design/system-architecture.md)
  - 微服务架构详解
  - 数据库设计（13张核心表）
  - Redis缓存策略
  - 消息队列设计
  - 扩展性考虑

- [工作流设计](docs/design/workflow-design.md)
  - 完整工作流程图
  - 5个阶段详细说明
  - AI提示词构建引擎
  - 图片压缩算法
  - 时间与成本估算

- [前端设计](docs/design/frontend-design.md)
  - Vue 3 技术栈
  - 6步骤式页面设计
  - 核心组件代码示例
  - API接口封装
  - 响应式设计

### API文档
- [API接口文档](docs/api/api-reference.md)
  - 通用响应格式
  - 用户相关接口（注册、登录）
  - 产品管理接口（CRUD）
  - 图片上传与压缩接口
  - AI生成接口（生成、查询、重新生成）
  - Webhook回调
  - SDK示例（JavaScript、Python）

### 部署文档
- [部署指南](docs/deploy/deployment-guide.md)
  - 环境准备（JDK、MySQL、Redis等）
  - 基础软件安装
  - MinIO对象存储部署
  - 后端微服务部署
  - 前端Nginx配置
  - HTTPS配置（Let's Encrypt）
  - 监控与日志
  - 备份策略
  - 性能优化
  - 故障排查

## 🗂️ 配置文件

| 文件 | 用途 |
|------|------|
| [.env.example](.env.example) | 后端环境变量配置示例 |
| [frontend/.env.example](frontend/.env.example) | 前端环境变量配置示例 |
| [docker-compose.yml](docker-compose.yml) | Docker Compose一键部署配置 |
| [backend/init-database.sql](backend/init-database.sql) | 数据库初始化SQL脚本 |
| [.gitignore](.gitignore) | Git忽略文件配置 |
| [LICENSE](LICENSE) | MIT开源许可证 |

## 📋 项目结构

```
hair-accessory-generator/
│
├── 📄 README.md                    # 项目介绍
├── 📄 QUICKSTART.md                # 快速启动指南
├── 📄 PROJECT_SUMMARY.md           # 项目总结
├── 📄 DOCUMENTATION.md             # 本文件：文档导航
├── 📄 LICENSE                      # MIT许可证
├── 📄 .gitignore                   # Git忽略配置
├── 📄 .env.example                 # 环境变量示例
├── 📄 docker-compose.yml           # Docker部署配置
│
├── 📁 backend/                     # 后端微服务
│   ├── 📁 gateway/                # 网关服务 (8080)
│   ├── 📁 user-service/           # 用户服务 (8081)
│   ├── 📁 product-service/        # 产品服务 (8082)
│   ├── 📁 image-service/          # 图片服务 (8083)
│   ├── 📁 ai-service/             # AI服务 (8084)
│   ├── 📁 common/                 # 公共模块
│   └── 📄 init-database.sql       # 数据库初始化脚本
│
├── 📁 frontend/                    # Vue 3 前端
│   ├── 📁 src/
│   │   ├── 📁 views/             # 页面组件
│   │   ├── 📁 components/        # 通用组件
│   │   ├── 📁 api/               # API接口
│   │   ├── 📁 store/             # 状态管理
│   │   └── 📁 utils/             # 工具函数
│   ├── 📁 public/
│   └── 📄 .env.example           # 前端环境变量示例
│
└── 📁 docs/                        # 文档目录
    ├── 📁 api/                    # API文档
    │   └── 📄 api-reference.md
    ├── 📁 design/                 # 设计文档
    │   ├── 📄 system-architecture.md
    │   ├── 📄 workflow-design.md
    │   └── 📄 frontend-design.md
    └── 📁 deploy/                 # 部署文档
        └── 📄 deployment-guide.md
```

## 🎯 按角色查找文档

### 👨‍💼 产品经理 / 业务人员
1. 先看 [README.md](README.md) 了解项目概况
2. 阅读 [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) 理解完整业务场景
3. 查看 [工作流设计](docs/design/workflow-design.md) 了解用户操作流程

### 👨‍💻 后端开发
1. 阅读 [系统架构设计](docs/design/system-architecture.md) 理解微服务架构
2. 查看 [API文档](docs/api/api-reference.md) 了解接口规范
3. 使用 [数据库脚本](backend/init-database.sql) 初始化数据库
4. 参考 [QUICKSTART.md](QUICKSTART.md) 启动开发环境

### 🎨 前端开发
1. 阅读 [前端设计](docs/design/frontend-design.md) 理解页面结构
2. 查看 [API文档](docs/api/api-reference.md) 了解接口调用
3. 使用 [QUICKSTART.md](QUICKSTART.md) 启动前端开发服务器

### 🏗️ 架构师
1. 深入阅读 [系统架构设计](docs/design/system-architecture.md)
2. 了解 [工作流设计](docs/design/workflow-design.md) 中的技术细节
3. 评估扩展性和性能指标

### 🚀 运维人员
1. 阅读 [部署指南](docs/deploy/deployment-guide.md)
2. 使用 [docker-compose.yml](docker-compose.yml) 快速部署
3. 配置环境变量（参考 .env.example）
4. 设置监控和备份策略

### 🧪 测试人员
1. 查看 [API文档](docs/api/api-reference.md) 了解接口规范
2. 参考 [工作流设计](docs/design/workflow-design.md) 编写测试用例
3. 使用 [QUICKSTART.md](QUICKSTART.md) 中的测试账户

## 📞 获取帮助

### 文档问题
如果文档有不清楚的地方，请：
1. 检查相关文档的"常见问题"部分
2. 搜索 GitHub Issues
3. 创建新的 Issue

### 技术支持
- 📧 邮箱: support@example.com
- 💬 讨论区: GitHub Discussions
- 🐛 问题反馈: GitHub Issues

## 🔄 文档更新

文档会持续更新，请关注：
- [CHANGELOG.md](CHANGELOG.md) - 查看更新历史
- GitHub Releases - 获取最新版本

## ⭐ 推荐阅读顺序

### 新手入门（第1天）
1. README.md
2. QUICKSTART.md
3. 工作流设计文档（前半部分）

### 深入理解（第2-3天）
1. 系统架构设计
2. 前端设计文档
3. API文档
4. 完整工作流设计

### 生产部署（第4-5天）
1. 部署指南
2. 数据库初始化
3. Docker配置
4. 监控和备份

## 📊 文档统计

| 文档类型 | 数量 | 总字数 |
|---------|------|--------|
| 核心文档 | 4个 | ~10,000 |
| 设计文档 | 3个 | ~25,000 |
| API文档 | 1个 | ~5,000 |
| 部署文档 | 1个 | ~8,000 |
| 配置文件 | 4个 | ~500 |
| **总计** | **13个** | **~48,500** |

## 🎉 开始使用

准备好了吗？从这里开始：

1. **快速体验**: [QUICKSTART.md](QUICKSTART.md)
2. **深入学习**: [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)
3. **开始开发**: [系统架构设计](docs/design/system-architecture.md)

---

**文档最后更新**: 2026-04-08  
**项目版本**: v1.0.0  
**维护团队**: Hair Accessory Generator Team
