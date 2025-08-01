# CELL Blog 部署指南

## 🚀 生产环境部署

### 1. 环境要求

#### 服务器配置
- **操作系统**: Linux (推荐 Ubuntu 20.04+)
- **Node.js**: 16.0.0 或更高版本
- **内存**: 最少 1GB RAM
- **存储**: 最少 10GB 可用空间
- **网络**: 稳定的网络连接

#### 软件依赖
- **Nginx**: 1.18.0 或更高版本
- **PM2**: 用于进程管理 (可选)
- **Git**: 用于代码部署

### 2. 部署步骤

#### 步骤 1: 服务器准备
```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装 Node.js
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# 安装 Nginx
sudo apt install nginx -y

# 安装 Git
sudo apt install git -y
```

#### 步骤 2: 项目部署
```bash
# 克隆项目 (如果使用Git)
git clone <your-repository-url>
cd FrontUser

# 或者直接上传项目文件到服务器

# 安装依赖
npm install

# 构建生产版本
npm run build
```

#### 步骤 3: Nginx 配置
```bash
# 创建 Nginx 配置文件
sudo nano /etc/nginx/sites-available/cellblog
```

配置文件内容：
```nginx
server {
    listen 80;
    server_name your-domain.com;  # 替换为你的域名
    
    root /var/www/cellblog/dist;
    index index.html;
    
    # 启用 gzip 压缩
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types text/plain text/css text/xml text/javascript application/javascript application/xml+rss application/json;
    
    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
    
    # SPA 路由支持
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # API 代理 (如果需要)
    location /api/ {
        proxy_pass http://localhost:8888/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

启用配置：
```bash
# 创建符号链接
sudo ln -s /etc/nginx/sites-available/cellblog /etc/nginx/sites-enabled/

# 测试配置
sudo nginx -t

# 重启 Nginx
sudo systemctl restart nginx
```

#### 步骤 4: 文件权限设置
```bash
# 设置项目目录权限
sudo chown -R www-data:www-data /var/www/cellblog
sudo chmod -R 755 /var/www/cellblog
```

### 3. HTTPS 配置 (推荐)

#### 使用 Let's Encrypt
```bash
# 安装 Certbot
sudo apt install certbot python3-certbot-nginx -y

# 获取 SSL 证书
sudo certbot --nginx -d your-domain.com

# 自动续期
sudo crontab -e
# 添加以下行：
# 0 12 * * * /usr/bin/certbot renew --quiet
```

### 4. 环境变量配置

创建生产环境配置文件：
```bash
# 在项目根目录创建 .env.production
nano .env.production
```

配置内容：
```env
# API 基础URL
VITE_API_BASE_URL=https://your-api-domain.com

# 应用标题
VITE_APP_TITLE=CELL Blog

# 是否启用调试模式
VITE_DEBUG=false
```

### 5. 性能优化

#### 启用 Nginx 缓存
```nginx
# 在 Nginx 配置中添加
location ~* \.(js|css)$ {
    expires 1y;
    add_header Cache-Control "public, immutable";
    add_header X-Content-Type-Options nosniff;
}

location ~* \.(png|jpg|jpeg|gif|ico|svg)$ {
    expires 1y;
    add_header Cache-Control "public, immutable";
}
```

#### 启用 Brotli 压缩
```bash
# 安装 Brotli 模块
sudo apt install libbrotli-dev -y
sudo apt install nginx-module-brotli -y
```

### 6. 监控和日志

#### 设置日志轮转
```bash
# 创建日志配置
sudo nano /etc/logrotate.d/cellblog
```

配置内容：
```
/var/log/nginx/cellblog.log {
    daily
    missingok
    rotate 52
    compress
    delaycompress
    notifempty
    create 644 www-data www-data
}
```

#### 设置监控
```bash
# 安装 PM2 (可选)
npm install -g pm2

# 创建 PM2 配置文件
nano ecosystem.config.js
```

PM2 配置：
```javascript
module.exports = {
  apps: [{
    name: 'cellblog',
    script: 'npm',
    args: 'run dev',
    cwd: '/var/www/cellblog',
    instances: 1,
    autorestart: true,
    watch: false,
    max_memory_restart: '1G',
    env: {
      NODE_ENV: 'production'
    }
  }]
};
```

### 7. 安全配置

#### 防火墙设置
```bash
# 安装 UFW
sudo apt install ufw -y

# 配置防火墙
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow ssh
sudo ufw allow 'Nginx Full'
sudo ufw enable
```

#### 安全头设置
```nginx
# 在 Nginx 配置中添加安全头
add_header X-Frame-Options "SAMEORIGIN" always;
add_header X-XSS-Protection "1; mode=block" always;
add_header X-Content-Type-Options "nosniff" always;
add_header Referrer-Policy "no-referrer-when-downgrade" always;
add_header Content-Security-Policy "default-src 'self' http: https: data: blob: 'unsafe-inline'" always;
```

### 8. 备份策略

#### 自动备份脚本
```bash
#!/bin/bash
# 创建备份脚本
nano /usr/local/bin/backup-cellblog.sh
```

备份脚本内容：
```bash
#!/bin/bash
BACKUP_DIR="/backup/cellblog"
DATE=$(date +%Y%m%d_%H%M%S)

# 创建备份目录
mkdir -p $BACKUP_DIR

# 备份项目文件
tar -czf $BACKUP_DIR/cellblog_$DATE.tar.gz /var/www/cellblog

# 删除7天前的备份
find $BACKUP_DIR -name "cellblog_*.tar.gz" -mtime +7 -delete

echo "Backup completed: cellblog_$DATE.tar.gz"
```

设置执行权限：
```bash
chmod +x /usr/local/bin/backup-cellblog.sh

# 添加到 crontab
crontab -e
# 添加：0 2 * * * /usr/local/bin/backup-cellblog.sh
```

### 9. 故障排除

#### 常见问题

1. **Nginx 502 错误**
   ```bash
   # 检查 Nginx 错误日志
   sudo tail -f /var/log/nginx/error.log
   ```

2. **权限问题**
   ```bash
   # 修复文件权限
   sudo chown -R www-data:www-data /var/www/cellblog
   sudo chmod -R 755 /var/www/cellblog
   ```

3. **SSL 证书问题**
   ```bash
   # 检查证书状态
   sudo certbot certificates
   ```

### 10. 部署检查清单

- [ ] 服务器环境配置完成
- [ ] 项目构建成功
- [ ] Nginx 配置正确
- [ ] SSL 证书安装
- [ ] 防火墙配置
- [ ] 备份策略设置
- [ ] 监控系统配置
- [ ] 性能测试通过
- [ ] 安全扫描通过

## 🎉 部署完成

恭喜！你的 CELL Blog 项目已经成功部署到生产环境。

### 访问地址
- **网站**: https://your-domain.com
- **管理面板**: 通过你的域名访问

### 维护建议
1. 定期更新系统和依赖
2. 监控服务器资源使用
3. 定期检查安全更新
4. 备份重要数据

---

*部署指南最后更新: 2024年12月* 