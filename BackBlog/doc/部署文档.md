# BackBlog 部署文档

## 环境要求
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

## 部署步骤

### 1. 安装依赖
- 安装 JDK 17
- 安装 MySQL 8.0
- 安装 Maven 3.6+

### 2. 配置数据库
```sql
CREATE DATABASE blog DEFAULT CHARACTER SET utf8mb4;
USE blog;
-- 执行 doc/database.sql 中的建表语句
```

### 3. 修改配置
编辑 `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
    username: root
    password: 你的MySQL密码

qiniu:
  accessKey: 你的七牛云AccessKey
  accessSecretKey: 你的七牛云SecretKey
```

### 4. 编译运行
```bash
mvn clean compile
mvn spring-boot:run
```

### 5. 验证部署
访问: http://localhost:8888

## Docker 部署
```bash
docker build -t backblog .
docker run -d -p 8888:8888 backblog
```

## 常见问题
- 端口被占用: 修改 application.yml 中的端口配置
- 数据库连接失败: 检查 MySQL 服务和连接配置
- 内存不足: 增加 JVM 堆内存参数 