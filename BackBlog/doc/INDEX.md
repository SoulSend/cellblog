# BackBlog 项目文档索引

## 📚 文档目录

### 1. [README.md](./README.md) - 项目概述
- 项目介绍和技术栈
- 项目结构说明
- 核心功能模块介绍
- 部署说明

### 2. [database.sql](./database.sql) - 数据库建表脚本
- 完整的 MySQL 建表语句
- 包含所有 9 个核心表
- 索引和约束定义
- 字符集和排序规则配置

### 3. [DATABASE_DESIGN.md](./DATABASE_DESIGN.md) - 数据库设计文档
- 详细的表结构设计
- 字段说明和数据类型
- 表关系图
- 索引设计说明

### 4. [API.md](./API.md) - API 接口文档
- RESTful API 接口说明
- 请求和响应格式
- 认证机制说明
- 错误码定义

### 5. [ARCHITECTURE.md](./ARCHITECTURE.md) - 项目架构文档
- 技术栈详细介绍
- 项目结构分析
- 分层架构说明
- 设计模式应用

### 6. [DEPLOYMENT.md](./DEPLOYMENT.md) - 部署文档
- 环境要求说明
- 部署步骤指南
- Docker 部署方案
- 常见问题解决

### 7. [PROJECT_SUMMARY.md](./PROJECT_SUMMARY.md) - 项目总结
- 项目特点总结
- 技术优势分析
- 功能模块概述
- 扩展建议

## 🚀 快速开始

### 1. 环境准备
- 安装 JDK 17+
- 安装 MySQL 8.0+
- 安装 Maven 3.6+

### 2. 数据库配置
```sql
CREATE DATABASE blog DEFAULT CHARACTER SET utf8mb4;
USE blog;
-- 执行 database.sql 中的建表语句
```

### 3. 应用配置
修改 `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
    username: root
    password: 你的MySQL密码
```

### 4. 启动应用
```bash
mvn spring-boot:run
```

### 5. 验证部署
访问: http://localhost:8888

## 📋 核心功能

### 用户管理
- 用户注册、登录
- JWT 认证
- 用户信息管理

### 文章管理
- 文章发布、编辑、删除
- 分类和标签管理
- 富文本内容支持

### 评论系统
- 多级评论支持
- 评论举报功能
- 评论管理

### 活动管理
- 活动发布
- 活动参与统计
- 活动详情展示

### 文件上传
- 图片上传
- 七牛云存储集成

## 🔧 技术栈

- **后端框架**: Spring Boot 3.3.4
- **ORM 框架**: MyBatis Plus 3.5.7
- **数据库**: MySQL 8.0
- **认证机制**: JWT
- **文件存储**: 七牛云
- **构建工具**: Maven

## 📁 项目结构

```
BackBlog/
├── doc/                    # 项目文档
│   ├── README.md          # 项目概述
│   ├── database.sql       # 数据库脚本
│   ├── DATABASE_DESIGN.md # 数据库设计
│   ├── API.md             # API 文档
│   ├── ARCHITECTURE.md    # 架构文档
│   ├── DEPLOYMENT.md      # 部署文档
│   ├── PROJECT_SUMMARY.md # 项目总结
│   └── INDEX.md           # 文档索引
├── src/main/java/         # 源代码
├── src/main/resources/    # 配置文件
└── pom.xml               # Maven 配置
```

## 📞 联系方式

如有问题或建议，请联系开发团队。

---

**BackBlog** - 一个现代化的博客系统后端项目 