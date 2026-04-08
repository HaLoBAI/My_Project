# 🚀 部署指南

本文档提供多种部署方案，选择最适合你的方式。

## 📋 部署方案对比

| 方案 | 费用 | 难度 | 数据库 | 推荐度 |
|------|------|------|--------|--------|
| **Render.com** | 免费 | ⭐ 简单 | PostgreSQL (免费) | ⭐⭐⭐⭐⭐ |
| **Railway.app** | $5/月免费额度 | ⭐ 简单 | MySQL (付费) | ⭐⭐⭐⭐ |
| **Heroku** | 免费 | ⭐⭐ 中等 | PostgreSQL (免费) | ⭐⭐⭐ |
| **自建服务器** | $5-50/月 | ⭐⭐⭐ 复杂 | 任意 | ⭐⭐ |

---

## 🎯 方案一：Render.com 部署（推荐）

### 优势
- ✅ 完全免费
- ✅ 自动从GitHub部署
- ✅ 提供免费PostgreSQL数据库
- ✅ 免费SSL证书
- ✅ 提供域名

### 步骤

#### 1. 注册Render账号
访问 https://render.com 并注册账号（可以用GitHub账号登录）

#### 2. 连接GitHub仓库
- 点击 "New +" → "Blueprint"
- 选择你的GitHub仓库: `HaLoBAI/My_Project`
- Render会自动识别 `render.yaml` 配置文件

#### 3. 配置环境变量
在Render Dashboard中，为每个服务配置：

**Product Service 环境变量**:
```
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8082
```

**AI Service 环境变量**:
```
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8084
AI_API_KEY=你的AI服务API密钥
```

#### 4. 部署
- 点击 "Create Blueprint Instance"
- Render会自动：
  1. 创建PostgreSQL数据库
  2. 构建Spring Boot应用
  3. 部署两个服务

#### 5. 初始化数据库
部署完成后，需要初始化数据库：

```bash
# 获取数据库连接信息
# 在Render Dashboard → Databases → 点击你的数据库

# 使用psql连接（需要修改为PostgreSQL语法）
psql -h <hostname> -U <username> -d <database_name> -f init-database-postgres.sql
```

#### 6. 访问应用
- Product Service: `https://hair-product-service.onrender.com`
- AI Service: `https://hair-ai-service.onrender.com`

---

## 🚂 方案二：Railway.app 部署

### 优势
- ✅ $5免费额度/月
- ✅ 支持MySQL数据库
- ✅ 简单易用
- ✅ 自动部署

### 步骤

#### 1. 注册Railway账号
访问 https://railway.app 并注册

#### 2. 新建项目
- 点击 "New Project"
- 选择 "Deploy from GitHub repo"
- 选择 `HaLoBAI/My_Project`

#### 3. 添加MySQL数据库
- 点击 "+ New"
- 选择 "Database" → "MySQL"
- Railway会自动创建MySQL实例

#### 4. 部署Product Service
- 点击 "+ New" → "GitHub Repo"
- 选择仓库
- 设置构建路径: `backend/product-service`
- 添加环境变量：
  ```
  SPRING_PROFILES_ACTIVE=production
  SPRING_DATASOURCE_URL=${{MySQL.DATABASE_URL}}
  SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQL_USER}}
  SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQL_PASSWORD}}
  SERVER_PORT=8082
  ```

#### 5. 部署AI Service
- 重复步骤4
- 构建路径改为: `backend/ai-service`
- 端口改为: `8084`
- 添加 `AI_API_KEY` 环境变量

#### 6. 初始化数据库
```bash
# 在Railway Dashboard获取数据库连接信息
mysql -h <host> -u <user> -p<password> <database> < backend/init-database.sql
```

#### 7. 访问
Railway会提供域名，例如：
- `https://hair-product-service.up.railway.app`
- `https://hair-ai-service.up.railway.app`

---

## 🐳 方案三：Docker Compose本地部署

适合本地开发和测试。

### 步骤

#### 1. 确保安装Docker和Docker Compose
```bash
docker --version
docker-compose --version
```

#### 2. 启动所有服务
```bash
cd /home/user/webapp
docker-compose up -d
```

这会启动：
- MySQL数据库
- Redis缓存
- Product Service (8082)
- AI Service (8084)

#### 3. 初始化数据库
```bash
# 等待MySQL启动（约30秒）
sleep 30

# 初始化数据库
docker exec -i hair-mysql mysql -uroot -proot123456 hair_accessory_db < backend/init-database.sql
```

#### 4. 访问
- Product Service: http://localhost:8082
- AI Service: http://localhost:8084

#### 5. 查看日志
```bash
docker-compose logs -f product-service
docker-compose logs -f ai-service
```

#### 6. 停止服务
```bash
docker-compose down
```

---

## 🌐 方案四：使用内网穿透（临时演示）

如果只是临时演示，可以使用ngrok。

### 使用Ngrok

#### 1. 安装ngrok
```bash
# 下载并安装
# https://ngrok.com/download

# 或使用npm
npm install -g ngrok
```

#### 2. 本地启动服务
```bash
# 启动Product Service
cd backend/product-service
mvn spring-boot:run

# 启动AI Service
cd backend/ai-service
mvn spring-boot:run
```

#### 3. 创建隧道
```bash
# 新终端窗口 - Product Service
ngrok http 8082

# 新终端窗口 - AI Service
ngrok http 8084
```

#### 4. 获取公网URL
ngrok会提供临时公网URL，例如：
- `https://abc123.ngrok.io` → localhost:8082
- `https://xyz789.ngrok.io` → localhost:8084

**注意**: 免费版ngrok每次重启URL都会变化。

---

## 📝 部署后配置

### 1. 配置CORS（如果前端在不同域名）

在 `application.yml` 添加：

```yaml
spring:
  web:
    cors:
      allowed-origins:
        - https://your-frontend-domain.com
        - http://localhost:5173
      allowed-methods:
        - GET
        - POST
        - PUT
        - DELETE
      allowed-headers: "*"
```

### 2. 配置AI API密钥

在部署平台的环境变量中设置：
```
AI_API_KEY=your_actual_api_key
AI_SERVICE_PROVIDER=openai # 或 stability, replicate
```

### 3. 健康检查

访问以下URL确认服务运行：
```
https://your-domain.com/actuator/health
```

应该返回：
```json
{
  "status": "UP"
}
```

---

## 🔧 故障排查

### 问题1: 数据库连接失败
**解决**:
- 检查数据库是否已启动
- 确认数据库连接字符串正确
- 检查用户名密码

### 问题2: 服务启动失败
**解决**:
- 查看日志: `docker logs <container_name>`
- 检查端口是否被占用
- 确认Java版本 >= 17

### 问题3: AI API调用失败
**解决**:
- 确认API密钥正确
- 检查网络连接
- 查看AI服务日志

---

## 📊 监控和日志

### Render.com
- 在Dashboard查看实时日志
- 查看CPU/内存使用情况

### Railway.app
- 在项目页面查看Logs
- 查看部署历史

### Docker
```bash
# 查看所有容器状态
docker-compose ps

# 查看特定服务日志
docker-compose logs -f product-service

# 查看所有日志
docker-compose logs -f
```

---

## 🎉 部署完成检查清单

- [ ] 数据库已创建并初始化
- [ ] Product Service可以访问
- [ ] AI Service可以访问
- [ ] 健康检查接口返回正常
- [ ] 可以创建产品
- [ ] 可以生成AI图片
- [ ] 环境变量已正确配置
- [ ] AI API密钥已设置

---

## 💡 推荐流程

**对于快速演示**：
1. 使用 Docker Compose 本地部署
2. 使用 ngrok 临时公开

**对于长期使用**：
1. 使用 Render.com 免费部署
2. 或使用 Railway.app（如果需要MySQL）

---

## 📞 需要帮助？

- Render文档: https://render.com/docs
- Railway文档: https://docs.railway.app
- Docker文档: https://docs.docker.com

---

**更新日期**: 2026-04-08  
**版本**: 1.0.0
