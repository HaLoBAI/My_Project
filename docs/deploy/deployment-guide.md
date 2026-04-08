# 部署文档

## 1. 环境准备

### 1.1 服务器要求

#### 生产环境推荐配置
- **应用服务器**: 
  - CPU: 8核
  - 内存: 16GB
  - 硬盘: 500GB SSD
  - 操作系统: Ubuntu 22.04 LTS 或 CentOS 8+
  
- **数据库服务器**:
  - CPU: 4核
  - 内存: 8GB
  - 硬盘: 200GB SSD
  
- **Redis服务器**:
  - CPU: 2核
  - 内存: 4GB
  
- **MinIO/OSS存储**: 1TB起步

### 1.2 软件依赖

```bash
# Java环境
JDK 17+

# Node.js环境
Node.js 16+ LTS
npm 8+

# 数据库
MySQL 8.0+
Redis 6.0+

# 其他工具
Docker 20+
Docker Compose 2+
Nginx 1.20+
Git 2.30+
```

## 2. 基础软件安装

### 2.1 安装JDK 17

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk -y

# 验证安装
java -version

# 配置环境变量
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### 2.2 安装MySQL 8.0

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install mysql-server -y

# 启动MySQL
sudo systemctl start mysql
sudo systemctl enable mysql

# 安全配置
sudo mysql_secure_installation

# 创建数据库和用户
sudo mysql -u root -p
```

```sql
-- 创建数据库
CREATE DATABASE hair_accessory_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户
CREATE USER 'hair_app'@'%' IDENTIFIED BY 'your_strong_password';

-- 授权
GRANT ALL PRIVILEGES ON hair_accessory_db.* TO 'hair_app'@'%';
FLUSH PRIVILEGES;

EXIT;
```

### 2.3 安装Redis

```bash
# Ubuntu/Debian
sudo apt install redis-server -y

# 启动Redis
sudo systemctl start redis-server
sudo systemctl enable redis-server

# 配置Redis（可选）
sudo vim /etc/redis/redis.conf
# 修改：
# bind 0.0.0.0
# requirepass your_redis_password
# maxmemory 2gb
# maxmemory-policy allkeys-lru

# 重启Redis
sudo systemctl restart redis-server
```

### 2.4 安装Node.js

```bash
# 使用nvm安装
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
source ~/.bashrc

nvm install 18
nvm use 18

# 验证
node -v
npm -v
```

### 2.5 安装Nginx

```bash
# Ubuntu/Debian
sudo apt install nginx -y

# 启动Nginx
sudo systemctl start nginx
sudo systemctl enable nginx

# 验证
sudo systemctl status nginx
```

### 2.6 安装Docker (可选，用于MinIO)

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install apt-transport-https ca-certificates curl software-properties-common -y

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"

sudo apt update
sudo apt install docker-ce docker-ce-cli containerd.io -y

# 启动Docker
sudo systemctl start docker
sudo systemctl enable docker

# 安装Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 验证
docker --version
docker-compose --version
```

## 3. 部署MinIO对象存储

### 3.1 使用Docker部署MinIO

创建 `docker-compose.yml`:

```yaml
version: '3.8'

services:
  minio:
    image: minio/minio:latest
    container_name: minio
    ports:
      - "9000:9000"
      - "9001:9001"
    environment:
      MINIO_ROOT_USER: admin
      MINIO_ROOT_PASSWORD: admin123456
    volumes:
      - minio_data:/data
    command: server /data --console-address ":9001"
    restart: always

volumes:
  minio_data:
    driver: local
```

```bash
# 启动MinIO
docker-compose up -d

# 检查状态
docker-compose ps

# 访问管理界面
# http://your-server-ip:9001
# 用户名: admin
# 密码: admin123456
```

### 3.2 配置MinIO

1. 登录MinIO控制台 (http://your-server-ip:9001)
2. 创建Bucket: `hair-accessory-images`
3. 设置访问策略为 `public` 或 `download`
4. 创建Access Key和Secret Key

## 4. 后端部署

### 4.1 克隆代码

```bash
cd /opt
sudo git clone https://github.com/your-org/hair-accessory-generator.git
cd hair-accessory-generator/backend
```

### 4.2 配置文件

编辑每个微服务的 `application.yml`:

**gateway/src/main/resources/application.yml**:
```yaml
server:
  port: 8080

spring:
  application:
    name: gateway-service
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/user/**
        - id: product-service
          uri: lb://product-service
          predicates:
            - Path=/api/product/**
        - id: image-service
          uri: lb://image-service
          predicates:
            - Path=/api/image/**
        - id: ai-service
          uri: lb://ai-service
          predicates:
            - Path=/api/ai/**

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

**product-service/src/main/resources/application.yml**:
```yaml
server:
  port: 8082

spring:
  application:
    name: product-service
  datasource:
    url: jdbc:mysql://localhost:3306/hair_accessory_db?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
    username: hair_app
    password: your_strong_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
    database: 0

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

logging:
  level:
    com.hairaccent: INFO
  file:
    name: logs/product-service.log
```

### 4.3 构建后端

```bash
cd /opt/hair-accessory-generator/backend

# 构建所有微服务
mvn clean package -DskipTests

# 或者分别构建
cd gateway && mvn clean package -DskipTests
cd ../product-service && mvn clean package -DskipTests
cd ../image-service && mvn clean package -DskipTests
cd ../ai-service && mvn clean package -DskipTests
cd ../user-service && mvn clean package -DskipTests
```

### 4.4 创建启动脚本

**启动所有服务**: `start-all.sh`

```bash
#!/bin/bash

echo "Starting Hair Accessory Generator Services..."

# Eureka Server (如果有的话)
# cd eureka-server
# nohup java -jar target/eureka-server-1.0.0.jar > logs/eureka.log 2>&1 &
# echo "Eureka Server started"
# cd ..

# Gateway
cd gateway
nohup java -jar target/gateway-1.0.0.jar > logs/gateway.log 2>&1 &
echo "Gateway started on port 8080"
cd ..

# User Service
cd user-service
nohup java -jar target/user-service-1.0.0.jar > logs/user-service.log 2>&1 &
echo "User Service started on port 8081"
cd ..

# Product Service
cd product-service
nohup java -jar target/product-service-1.0.0.jar > logs/product-service.log 2>&1 &
echo "Product Service started on port 8082"
cd ..

# Image Service
cd image-service
nohup java -jar target/image-service-1.0.0.jar > logs/image-service.log 2>&1 &
echo "Image Service started on port 8083"
cd ..

# AI Service
cd ai-service
nohup java -jar target/ai-service-1.0.0.jar > logs/ai-service.log 2>&1 &
echo "AI Service started on port 8084"
cd ..

echo "All services started successfully!"
echo "Check logs in respective logs/ directories"
```

```bash
chmod +x start-all.sh
./start-all.sh
```

**停止所有服务**: `stop-all.sh`

```bash
#!/bin/bash

echo "Stopping Hair Accessory Generator Services..."

pkill -f "gateway-1.0.0.jar"
pkill -f "user-service-1.0.0.jar"
pkill -f "product-service-1.0.0.jar"
pkill -f "image-service-1.0.0.jar"
pkill -f "ai-service-1.0.0.jar"

echo "All services stopped!"
```

```bash
chmod +x stop-all.sh
```

### 4.5 使用Systemd管理服务（推荐）

创建服务文件 `/etc/systemd/system/hair-gateway.service`:

```ini
[Unit]
Description=Hair Accessory Generator - Gateway Service
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/hair-accessory-generator/backend/gateway
ExecStart=/usr/bin/java -jar /opt/hair-accessory-generator/backend/gateway/target/gateway-1.0.0.jar
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
```

同样创建其他服务的systemd文件，然后：

```bash
# 重新加载systemd
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start hair-gateway
sudo systemctl start hair-user-service
sudo systemctl start hair-product-service
sudo systemctl start hair-image-service
sudo systemctl start hair-ai-service

# 设置开机自启
sudo systemctl enable hair-gateway
sudo systemctl enable hair-user-service
sudo systemctl enable hair-product-service
sudo systemctl enable hair-image-service
sudo systemctl enable hair-ai-service

# 查看状态
sudo systemctl status hair-gateway
```

## 5. 前端部署

### 5.1 构建前端

```bash
cd /opt/hair-accessory-generator/frontend

# 安装依赖
npm install

# 配置环境变量
cat > .env.production << EOF
VITE_API_BASE_URL=https://api.yourdomain.com
VITE_MINIO_ENDPOINT=https://cdn.yourdomain.com
EOF

# 构建
npm run build

# 构建产物在 dist/ 目录
```

### 5.2 配置Nginx

创建 `/etc/nginx/sites-available/hair-accessory`:

```nginx
# 前端服务
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;
    
    root /opt/hair-accessory-generator/frontend/dist;
    index index.html;
    
    # Gzip压缩
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
    
    # API代理
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # WebSocket支持
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        
        # 超时设置
        proxy_connect_timeout 600s;
        proxy_send_timeout 600s;
        proxy_read_timeout 600s;
    }
}

# API服务（可选，如果需要单独域名）
server {
    listen 80;
    server_name api.yourdomain.com;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        proxy_connect_timeout 600s;
        proxy_send_timeout 600s;
        proxy_read_timeout 600s;
    }
}

# CDN/MinIO代理
server {
    listen 80;
    server_name cdn.yourdomain.com;
    
    location / {
        proxy_pass http://localhost:9000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        
        # 静态资源缓存
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

```bash
# 启用站点
sudo ln -s /etc/nginx/sites-available/hair-accessory /etc/nginx/sites-enabled/

# 测试配置
sudo nginx -t

# 重启Nginx
sudo systemctl restart nginx
```

### 5.3 配置HTTPS（使用Let's Encrypt）

```bash
# 安装Certbot
sudo apt install certbot python3-certbot-nginx -y

# 获取证书
sudo certbot --nginx -d yourdomain.com -d www.yourdomain.com
sudo certbot --nginx -d api.yourdomain.com
sudo certbot --nginx -d cdn.yourdomain.com

# 自动续期
sudo certbot renew --dry-run
```

## 6. 监控与日志

### 6.1 日志管理

```bash
# 查看应用日志
tail -f /opt/hair-accessory-generator/backend/gateway/logs/gateway.log
tail -f /opt/hair-accessory-generator/backend/product-service/logs/product-service.log

# 查看Nginx日志
tail -f /var/log/nginx/access.log
tail -f /var/log/nginx/error.log

# 查看MySQL日志
sudo tail -f /var/log/mysql/error.log
```

### 6.2 系统监控（可选）

使用Prometheus + Grafana:

```bash
# 安装Prometheus
docker run -d \
  --name prometheus \
  -p 9090:9090 \
  -v /opt/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml \
  prom/prometheus

# 安装Grafana
docker run -d \
  --name grafana \
  -p 3000:3000 \
  grafana/grafana
```

## 7. 备份策略

### 7.1 数据库备份

创建备份脚本 `/opt/scripts/backup-db.sh`:

```bash
#!/bin/bash

BACKUP_DIR="/opt/backups/mysql"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/hair_accessory_db_$DATE.sql"

mkdir -p $BACKUP_DIR

mysqldump -u hair_app -p'your_strong_password' hair_accessory_db > $BACKUP_FILE

# 压缩
gzip $BACKUP_FILE

# 删除7天前的备份
find $BACKUP_DIR -name "*.sql.gz" -mtime +7 -delete

echo "Backup completed: $BACKUP_FILE.gz"
```

```bash
chmod +x /opt/scripts/backup-db.sh

# 添加到crontab (每天凌晨2点备份)
crontab -e
# 添加：
0 2 * * * /opt/scripts/backup-db.sh
```

### 7.2 MinIO数据备份

```bash
# 使用mc (MinIO Client) 同步到其他存储
mc alias set myminio http://localhost:9000 admin admin123456
mc mirror myminio/hair-accessory-images /backup/minio/
```

## 8. 性能优化

### 8.1 MySQL优化

编辑 `/etc/mysql/mysql.conf.d/mysqld.cnf`:

```ini
[mysqld]
max_connections = 500
innodb_buffer_pool_size = 4G
innodb_log_file_size = 256M
query_cache_size = 0
query_cache_type = 0
```

### 8.2 Redis优化

编辑 `/etc/redis/redis.conf`:

```ini
maxmemory 2gb
maxmemory-policy allkeys-lru
save 900 1
save 300 10
save 60 10000
```

### 8.3 Nginx优化

编辑 `/etc/nginx/nginx.conf`:

```nginx
worker_processes auto;
worker_connections 10000;

http {
    # 启用缓存
    proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=my_cache:10m max_size=1g inactive=60m;
    
    # 连接优化
    keepalive_timeout 65;
    keepalive_requests 100;
    
    # 文件上传大小限制
    client_max_body_size 100M;
}
```

## 9. 故障排查

### 9.1 服务无法启动

```bash
# 检查端口占用
netstat -tlnp | grep 8080

# 检查Java进程
ps aux | grep java

# 检查日志
tail -f logs/application.log
```

### 9.2 数据库连接失败

```bash
# 测试MySQL连接
mysql -u hair_app -p -h localhost

# 检查MySQL状态
sudo systemctl status mysql

# 检查防火墙
sudo ufw status
```

### 9.3 Redis连接失败

```bash
# 测试Redis连接
redis-cli -h localhost -p 6379 -a your_redis_password ping

# 检查Redis状态
sudo systemctl status redis-server
```

## 10. 更新部署

```bash
# 拉取最新代码
cd /opt/hair-accessory-generator
git pull origin main

# 停止服务
./backend/stop-all.sh

# 重新构建
cd backend
mvn clean package -DskipTests

cd ../frontend
npm run build

# 启动服务
cd ../backend
./start-all.sh

# 重启Nginx
sudo systemctl restart nginx
```

## 11. 安全加固

- 配置防火墙规则
- 定期更新系统补丁
- 使用强密码
- 启用SSH密钥登录
- 关闭不必要的端口
- 配置fail2ban防止暴力破解
- 定期审计日志

---

**部署完成后访问**:
- 前端: https://yourdomain.com
- API: https://api.yourdomain.com
- MinIO控制台: http://your-server-ip:9001
