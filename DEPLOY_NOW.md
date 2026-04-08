# 🚀 立即部署指南

本指南将帮助你在 5-10 分钟内完成在线部署。

---

## ⚡ 快速选择

### 方案 A：Render.com（推荐 - 完全免费）
- ✅ 完全免费
- ✅ 最简单
- ✅ 自动部署
- ✅ 免费数据库
- ⚠️ 使用 PostgreSQL（需转换 MySQL 脚本）

### 方案 B：Railway.app（有免费额度）
- ✅ $5/月免费额度
- ✅ 支持 MySQL
- ✅ 简单易用
- ⚠️ 需要绑定信用卡

---

## 🎯 方案 A：Render.com 部署（最简单）

### 步骤 1：注册 Render 账号

1. 访问：https://render.com
2. 点击 **"Get Started"** 或 **"Sign Up"**
3. 使用 GitHub 账号登录（推荐）

### 步骤 2：创建 Blueprint

1. 登录后，点击顶部的 **"New +"** 按钮
2. 选择 **"Blueprint"**
3. 点击 **"Connect GitHub"**（如果未连接）
4. 在弹出的窗口中授权 Render 访问你的 GitHub
5. 搜索并选择仓库：**HaLoBAI/My_Project**
6. 分支选择：**main**

### 步骤 3：配置 Blueprint

Render 会自动检测到 `render.yaml` 文件，你会看到：

**检测到的服务：**
- ✅ hair-product-service (Product Service)
- ✅ hair-ai-service (AI Service)
- ✅ hair-db (PostgreSQL Database)

点击 **"Apply"** 继续。

### 步骤 4：等待部署

Render 会自动：
1. 创建 PostgreSQL 数据库（约 1 分钟）
2. 构建 Product Service（约 3-5 分钟）
3. 构建 AI Service（约 3-5 分钟）

你可以在 Dashboard 中看到实时日志。

### 步骤 5：获取服务 URL

部署完成后，你会得到两个 URL：

- **Product Service**: `https://hair-product-service.onrender.com`
- **AI Service**: `https://hair-ai-service.onrender.com`

### 步骤 6：配置 AI API 密钥（可选）

1. 在 Render Dashboard 中，点击 **hair-ai-service**
2. 进入 **"Environment"** 标签
3. 添加环境变量：
   - Key: `AI_API_KEY`
   - Value: `你的AI服务API密钥`（如果暂时没有，可以跳过）
4. 点击 **"Save Changes"**
5. 服务会自动重启

### 步骤 7：测试服务

**健康检查：**
```bash
curl https://hair-product-service.onrender.com/actuator/health
curl https://hair-ai-service.onrender.com/actuator/health
```

**创建产品测试：**
```bash
curl -X POST https://hair-product-service.onrender.com/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试发饰套装",
    "description": "这是一个测试产品",
    "targetMarket": "Russia",
    "painPoints": [{"content": "头发容易散"}],
    "requirements": [{"content": "需要牢固"}],
    "components": [{"name": "发夹", "imageUrl": "https://example.com/1.jpg"}]
  }'
```

### ⚠️ 重要提示

**Render 免费版限制：**
- 服务闲置 15 分钟后会休眠
- 首次访问需要等待 30-60 秒唤醒
- 每月有 750 小时免费时长（足够个人使用）

---

## 🚂 方案 B：Railway.app 部署

### 步骤 1：注册 Railway 账号

1. 访问：https://railway.app
2. 点击 **"Login"** 或 **"Start a New Project"**
3. 使用 GitHub 账号登录

### 步骤 2：创建新项目

1. 点击 **"New Project"**
2. 选择 **"Deploy from GitHub repo"**
3. 连接 GitHub（如果未连接）
4. 选择仓库：**HaLoBAI/My_Project**

### 步骤 3：添加 MySQL 数据库

1. 在项目页面，点击 **"+ New"**
2. 选择 **"Database"**
3. 选择 **"Add MySQL"**
4. Railway 会自动创建 MySQL 实例

### 步骤 4：部署 Product Service

1. 点击 **"+ New"**
2. 选择 **"GitHub Repo"**（选择同一个仓库）
3. 在 **Settings** 中：
   - **Root Directory**: `backend/product-service`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/product-service-1.0.0.jar`

4. 在 **Variables** 标签添加环境变量：
   ```
   SPRING_PROFILES_ACTIVE=production
   SPRING_DATASOURCE_URL=jdbc:mysql://${{MySQL.MYSQL_URL}}/hair_accessory_db
   SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQL_USER}}
   SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQL_PASSWORD}}
   SERVER_PORT=8082
   ```

5. 点击 **"Deploy"**

### 步骤 5：部署 AI Service

重复步骤 4，但修改：
- **Root Directory**: `backend/ai-service`
- **Port**: `8084`
- 添加额外环境变量：`AI_API_KEY=你的密钥`

### 步骤 6：初始化数据库

1. 在 Railway Dashboard，点击 MySQL 数据库
2. 点击 **"Connect"** 标签，获取连接命令
3. 在本地执行：
   ```bash
   mysql -h <host> -u <user> -p<password> -P <port> hair_accessory_db < backend/init-database.sql
   ```

### 步骤 7：获取服务 URL

1. 点击 Product Service
2. 进入 **"Settings"** → **"Domains"**
3. 点击 **"Generate Domain"**
4. 复制生成的 URL

重复此步骤获取 AI Service URL。

---

## 🧪 部署后测试

### 1. 健康检查

```bash
# Render.com
curl https://hair-product-service.onrender.com/actuator/health
curl https://hair-ai-service.onrender.com/actuator/health

# Railway.app
curl https://your-product-service.up.railway.app/actuator/health
curl https://your-ai-service.up.railway.app/actuator/health
```

### 2. 创建产品

```bash
curl -X POST <YOUR_PRODUCT_SERVICE_URL>/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "优雅发饰套装",
    "description": "适合俄罗斯市场的精美发饰",
    "targetMarket": "Russia",
    "painPoints": [
      {"content": "头发容易散落"},
      {"content": "发饰不够牢固"}
    ],
    "requirements": [
      {"content": "需要超强固定力"},
      {"content": "符合俄罗斯审美"}
    ],
    "components": [
      {"name": "大号发夹", "description": "主固定件", "imageUrl": "https://example.com/clip.jpg"},
      {"name": "装饰发带", "description": "装饰件", "imageUrl": "https://example.com/band.jpg"}
    ]
  }'
```

### 3. 查询产品列表

```bash
curl <YOUR_PRODUCT_SERVICE_URL>/api/products
```

### 4. 生成 AI 图片

```bash
curl -X POST <YOUR_AI_SERVICE_URL>/api/ai/generate \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "productName": "优雅发饰套装",
    "description": "适合俄罗斯市场",
    "targetMarket": "Russia",
    "painPoints": ["头发容易散落"],
    "requirements": ["超强固定力"],
    "componentImages": ["https://example.com/1.jpg"]
  }'
```

---

## 📊 部署对比

| 特性 | Render.com | Railway.app |
|------|-----------|------------|
| 💰 **费用** | 完全免费 | $5/月免费额度 |
| 🗄️ **数据库** | PostgreSQL (免费) | MySQL ($5计入额度) |
| 🚀 **部署速度** | 5-8 分钟 | 5-8 分钟 |
| 📦 **配置难度** | ⭐ 超简单 (Blueprint) | ⭐⭐ 需手动配置 |
| 🌐 **域名** | 自动提供 | 自动提供 |
| 🔒 **SSL** | 自动 HTTPS | 自动 HTTPS |
| 💤 **休眠** | 15 分钟后休眠 | 不休眠 |
| 📈 **免费时长** | 750 小时/月 | $5 使用量 |

### 推荐选择

- **个人学习/演示** → Render.com（完全免费）
- **小型项目/需要MySQL** → Railway.app（前期免费够用）
- **需要长期稳定运行** → Railway.app（不休眠）

---

## ❓ 常见问题

### Q1: Render 部署失败怎么办？

**A**: 检查日志：
1. 在 Render Dashboard 点击服务名称
2. 查看 **"Logs"** 标签
3. 常见问题：
   - Maven 构建失败 → 检查 `pom.xml`
   - 端口冲突 → 确认 `SERVER_PORT` 环境变量
   - 数据库连接失败 → 等待数据库初始化完成（约 2 分钟）

### Q2: 第一次访问很慢？

**A**: Render 免费版会在闲置后休眠，首次访问需要 30-60 秒唤醒。解决方法：
- 使用 UptimeRobot 等服务定期 ping
- 升级到付费版（$7/月）

### Q3: Railway 用完免费额度怎么办？

**A**: Railway 每月提供 $5 免费额度：
- 两个 Web Service：约 $3-4/月
- MySQL 数据库：约 $1-2/月
- 超出后需要绑定信用卡付费

### Q4: 如何对接真实 AI API？

**A**: 修改 `backend/ai-service/src/.../AIGenerationService.java`：
```java
// 当前是占位符实现
// 替换为真实 API 调用
// 例如：Stability AI, Replicate, DALL-E 3
```

---

## 🎉 完成检查清单

- [ ] 选择部署平台（Render.com 或 Railway.app）
- [ ] 注册账号并连接 GitHub
- [ ] 部署成功（服务状态为 "Live" 或 "Active"）
- [ ] 获取服务 URL
- [ ] 健康检查通过
- [ ] 创建测试产品成功
- [ ] AI 生成接口可访问
- [ ] （可选）配置 AI API 密钥

---

## 🆘 需要帮助？

如果遇到问题，请提供：
1. 选择的部署平台
2. 错误日志截图
3. 服务 URL（如果可以访问）

我会帮你解决！

---

**文档版本**: 1.0.0  
**更新日期**: 2026-04-08
