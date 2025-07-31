# BackBlog 后端项目文档

## 项目概述

BackBlog 是一个基于 Spring Boot 的博客系统后端项目，采用前后端分离的架构设计。该项目提供了完整的博客功能，包括用户管理、文章管理、分类管理、标签管理、评论系统等核心功能。

## 技术栈

- **框架**: Spring Boot 3.3.4
- **数据库**: MySQL 8.0+
- **ORM框架**: MyBatis Plus 3.5.7
- **安全认证**: JWT (JSON Web Token)
- **文件存储**: 七牛云对象存储
- **日志**: Log4j
- **工具库**: 
  - Lombok (代码简化)
  - FastJSON (JSON处理)
  - Commons Codec (MD5加密)
  - Joda Time (时间处理)

## 项目结构

```
BackBlog/
├── src/main/java/org/hrc/backblog/
│   ├── BackBlogApplication.java          # 主启动类
│   ├── common/                          # 公共组件
│   │   └── aop/                        # 切面编程
│   ├── config/                          # 配置类
│   ├── controller/                      # 控制器层
│   ├── dao/                            # 数据访问层
│   │   ├── dos/                        # 数据传输对象
│   │   ├── mapper/                     # MyBatis映射器
│   │   └── pojo/                       # 实体类
│   ├── handler/                        # 处理器
│   ├── service/                        # 服务层
│   │   └── Impl/                       # 服务实现类
│   ├── utils/                          # 工具类
│   └── vo/                             # 视图对象
│       └── params/                     # 参数对象
├── src/main/resources/
│   ├── application.yml                  # 应用配置文件
│   ├── org/hrc/backblog/dao/mapper/    # MyBatis映射文件
│   └── static/                         # 静态资源
└── doc/                                # 项目文档
```

## 核心功能模块

### 1. 用户管理模块
- 用户注册、登录
- 用户信息管理
- 权限控制

### 2. 文章管理模块
- 文章的增删改查
- 文章分类管理
- 文章标签管理
- 文章内容存储

### 3. 评论系统
- 多级评论支持
- 评论举报功能
- 评论管理

### 4. 活动管理
- 活动发布
- 活动参与
- 活动统计

### 5. 文件上传
- 图片上传
- 七牛云存储集成

## 数据库设计

项目使用 MySQL 数据库，主要包含以下表：

- `ms_sys_user`: 用户表
- `ms_article`: 文章表
- `ms_article_body`: 文章内容表
- `ms_category`: 分类表
- `ms_tag`: 标签表
- `ms_article_tag`: 文章标签关联表
- `ms_comment`: 评论表
- `ms_admin`: 管理员表
- `ms_activity`: 活动表

详细的数据库设计请参考 `database.sql` 文件。

## 部署说明

### 环境要求
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 配置说明
1. 修改 `application.yml` 中的数据库连接信息
2. 配置七牛云存储密钥
3. 创建数据库并执行 `database.sql` 脚本

### 启动方式
```bash
mvn spring-boot:run
```

## API 文档

项目提供了 RESTful API 接口，主要包括：

- 用户相关接口 (`/api/users/`)
- 文章相关接口 (`/api/articles/`)
- 分类相关接口 (`/api/categories/`)
- 标签相关接口 (`/api/tags/`)
- 评论相关接口 (`/api/comments/`)
- 活动相关接口 (`/api/activities/`)

详细的 API 文档请参考 `API.md` 文件。

## 开发团队

- 项目名称: BackBlog
- 版本: 0.0.1-SNAPSHOT
- 开发语言: Java 17
- 构建工具: Maven 