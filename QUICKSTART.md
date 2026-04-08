# 快速启动指南

本指南帮助您快速启动并运行发饰产品图片生成系统。

## 🚀 5分钟快速启动（开发环境）

### 前置要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.8+

### 步骤1: 克隆项目

```bash
git clone <repository-url>
cd hair-accessory-generator
```

### 步骤2: 初始化数据库

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库和表
source backend/init-database.sql

# 验证
USE hair_accessory_db;
SHOW TABLES;
EXIT;
```

### 步骤3: 配置后端

编辑 `backend/product-service/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hair_accessory_db
    username: hair_app
    password: your_strong_password  # 修改为你的密码
  redis:
    host: localhost
    password: your_redis_password   # 修改为你的密码（如果有）
```

对其他微服务做相同配置。

### 步骤4: 启动后端服务

```bash
cd backend

# 方式1: 使用Maven直接运行（开发）
# 终端1 - Gateway
cd gateway
mvn spring-boot:run

# 终端2 - User Service
cd user-service
mvn spring-boot:run

# 终端3 - Product Service
cd product-service
mvn spring-boot:run

# 终端4 - Image Service
cd image-service
mvn spring-boot:run

# 终端5 - AI Service
cd ai-service
mvn spring-boot:run
```

或者

```bash
# 方式2: 构建后运行
mvn clean package -DskipTests
./start-all.sh
```

### 步骤5: 启动前端

```bash
cd frontend

# 安装依赖（首次）
npm install

# 配置环境变量
cp .env.example .env.development
# 编辑 .env.development，设置API地址

# 启动开发服务器
npm run dev

# 访问 http://localhost:5173
```

### 步骤6: 访问系统

- **前端地址**: http://localhost:5173
- **API网关**: http://localhost:8080
- **默认账户**: admin / Admin123456

## 📦 Docker Compose 快速启动（推荐）

### 准备工作

确保安装了 Docker 和 Docker Compose。

### 一键启动

```bash
# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down

# 停止并删除数据
docker-compose down -v
```

系统会自动启动：
- MySQL
- Redis
- MinIO
- 所有后端微服务
- 前端Nginx服务

访问 http://localhost

## 🔧 开发工具推荐

### IDE
- **后端**: IntelliJ IDEA / Eclipse
- **前端**: VS Code

### VS Code 扩展
- Volar (Vue 3)
- ESLint
- Prettier
- REST Client

### IntelliJ IDEA 插件
- Lombok
- Spring Boot Assistant
- MyBatis X

## 📱 测试账户

系统预置了以下测试账户：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | Admin123456 | 管理员 | 完全权限 |
| test_user | Test123456 | 普通用户 | 基础功能（需手动创建） |

## 🎯 快速测试工作流

### 1. 登录系统
使用 admin / Admin123456 登录

### 2. 创建产品
1. 点击"创建产品"
2. 填写产品信息：
   - 名称：俄罗斯风格发饰套装测试
   - 类目：发饰
   - 目标市场：俄罗斯
   - 描述：优雅的蝴蝶结发饰套装

### 3. 添加痛点
添加2-3个用户痛点，例如：
- 现有发饰容易滑落
- 材质容易过敏

### 4. 添加需求
添加2-3个用户需求，例如：
- 希望可以多场合使用
- 价格实惠

### 5. 上传部件图片
1. 添加部件"主发夹"
2. 上传2-3张不同角度的图片
3. 可以添加更多部件

### 6. 开始生成
点击"开始生成"，等待3-5分钟

### 7. 预览和下载
- 预览10张生成的图片
- 可以拖拽排序
- 可以重新生成单张
- 点击"下载全部"获取ZIP文件

## 🐛 常见问题

### Q1: 后端服务启动失败
**A**: 检查端口是否被占用
```bash
netstat -tlnp | grep 8080
netstat -tlnp | grep 8081
# ... 其他端口
```

### Q2: 数据库连接失败
**A**: 
1. 确认MySQL服务运行：`sudo systemctl status mysql`
2. 检查配置文件中的数据库密码
3. 测试连接：`mysql -u hair_app -p -h localhost`

### Q3: Redis连接失败
**A**: 
1. 确认Redis服务运行：`sudo systemctl status redis`
2. 测试连接：`redis-cli ping`

### Q4: 前端页面空白
**A**: 
1. 检查浏览器控制台错误
2. 确认后端API可访问：访问 http://localhost:8080/actuator/health
3. 检查 .env 配置

### Q5: AI生成失败
**A**: 
1. 检查AI服务日志：`tail -f backend/ai-service/logs/ai-service.log`
2. 确认AI API配置正确
3. 检查网络连接

### Q6: 图片上传失败
**A**: 
1. 检查MinIO/存储服务是否运行
2. 确认存储配置正确
3. 检查文件大小限制（默认5MB）

## 📖 下一步

- 阅读 [系统架构文档](docs/design/system-architecture.md)
- 查看 [API文档](docs/api/api-reference.md)
- 学习 [工作流设计](docs/design/workflow-design.md)
- 了解 [前端开发](docs/design/frontend-design.md)
- 准备 [生产部署](docs/deploy/deployment-guide.md)

## 💬 获取帮助

- 查看文档：`docs/` 目录
- 提交Issue：GitHub Issues
- 联系支持：support@example.com

## 📝 开发检查清单

开发前确认：
- [ ] 数据库已初始化
- [ ] 配置文件已修改
- [ ] 所有依赖已安装
- [ ] 端口未被占用
- [ ] Redis和MySQL已启动

开发完成确认：
- [ ] 代码已测试
- [ ] 日志无错误
- [ ] API响应正常
- [ ] 前端功能正常
- [ ] 文档已更新

## 🎉 完成

现在您已经成功启动了发饰产品图片生成系统！

试着创建第一个产品并生成电商主图吧。
