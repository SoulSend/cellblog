# BackBlog 初始化数据说明

## 概述

本目录包含了BackBlog项目的数据库初始化数据，用于快速搭建开发环境和演示系统功能。

## 文件说明

### 1. database.sql
- **用途**: 数据库表结构创建脚本
- **内容**: 包含所有表的CREATE TABLE语句
- **执行顺序**: 首先执行此文件创建数据库和表结构

### 2. init_data.sql
- **用途**: 基础数据初始化脚本
- **内容**: 用户、管理员、分类、标签等基础数据
- **执行顺序**: 在database.sql之后执行

### 3. init_articles.sql
- **用途**: 文章相关数据初始化脚本
- **内容**: 文章内容、文章信息、文章标签关联等数据
- **执行顺序**: 在init_data.sql之后执行

### 4. init_comments_activities.sql
- **用途**: 评论和活动数据初始化脚本
- **内容**: 评论数据、活动信息等
- **执行顺序**: 在init_articles.sql之后执行

### 5. init_data_simple.sql
- **用途**: 简化版初始化脚本（推荐用于快速测试）
- **内容**: 包含所有表的简化数据
- **执行顺序**: 在database.sql之后执行

### 6. init_all_data.sql
- **用途**: 完整版初始化脚本
- **内容**: 包含所有表的完整数据
- **执行顺序**: 在database.sql之后执行

## 使用方法

### 方法一：使用简化版脚本（推荐）

```bash
# 1. 创建数据库和表结构
mysql -u root -p < doc/database.sql

# 2. 初始化数据
mysql -u root -p < doc/init_data_simple.sql
```

### 方法二：使用完整版脚本

```bash
# 1. 创建数据库和表结构
mysql -u root -p < doc/database.sql

# 2. 初始化基础数据
mysql -u root -p < doc/init_data.sql

# 3. 初始化文章数据
mysql -u root -p < doc/init_articles.sql

# 4. 初始化评论和活动数据
mysql -u root -p < doc/init_comments_activities.sql
```

### 方法三：使用完整版单文件脚本

```bash
# 1. 创建数据库和表结构
mysql -u root -p < doc/database.sql

# 2. 初始化所有数据
mysql -u root -p < doc/init_all_data.sql
```

## 初始化数据内容

### 用户数据
- **管理员账号**: admin / 123456
- **普通用户**: zhangsan / 123456
- **其他用户**: lisi, wangwu, zhaoliu / 123456

### 分类数据
- Java开发
- Python开发
- 前端开发
- 后端开发
- 数据库
- DevOps
- 人工智能
- 移动开发

### 标签数据
- Java, Spring, SpringBoot
- MySQL, Redis
- JavaScript, Vue, React
- Docker, Kubernetes
- AI, 机器学习, 微服务, 云计算

### 文章数据
- Spring Boot 入门指南
- Vue.js 3.0 新特性详解
- Docker 容器化部署指南
- MySQL 性能优化实践
- 微服务架构设计原则

### 活动数据
- Spring Boot 技术分享会
- 前端技术发展趋势讨论
- DevOps实践分享
- 数据库性能优化实战
- 人工智能技术应用

## 注意事项

1. **密码说明**: 所有用户的密码都是 `123456`（MD5加密后的值）
2. **时间戳**: 使用毫秒级时间戳，对应2024年1月1日
3. **图片路径**: 静态资源路径需要确保对应的图片文件存在
4. **数据完整性**: 所有外键关系都已正确建立
5. **字符集**: 使用utf8mb4字符集，支持emoji等特殊字符

## 验证数据

执行初始化后，可以通过以下SQL验证数据是否正确插入：

```sql
-- 查看各表数据量
SELECT '用户表' as table_name, COUNT(*) as count FROM ms_sys_user
UNION ALL
SELECT '管理员表', COUNT(*) FROM ms_admin
UNION ALL
SELECT '分类表', COUNT(*) FROM ms_category
UNION ALL
SELECT '标签表', COUNT(*) FROM ms_tag
UNION ALL
SELECT '文章表', COUNT(*) FROM ms_article
UNION ALL
SELECT '文章内容表', COUNT(*) FROM ms_article_body
UNION ALL
SELECT '文章标签关联表', COUNT(*) FROM ms_article_tag
UNION ALL
SELECT '评论表', COUNT(*) FROM ms_comment
UNION ALL
SELECT '活动表', COUNT(*) FROM ms_activity;
```

## 清理数据

如果需要清理所有数据重新开始，可以使用以下SQL：

```sql
-- 注意：这会删除所有数据，请谨慎使用
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE ms_activity;
TRUNCATE TABLE ms_comment;
TRUNCATE TABLE ms_article_tag;
TRUNCATE TABLE ms_article_body;
TRUNCATE TABLE ms_article;
TRUNCATE TABLE ms_tag;
TRUNCATE TABLE ms_category;
TRUNCATE TABLE ms_admin;
TRUNCATE TABLE ms_sys_user;
SET FOREIGN_KEY_CHECKS = 1;
```

## 技术支持

如果在使用过程中遇到问题，请检查：

1. MySQL版本是否支持utf8mb4字符集
2. 数据库连接权限是否正确
3. 表结构是否与当前代码版本匹配
4. 外键约束是否正确设置 