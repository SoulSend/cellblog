# BackBlog 数据库设计文档

## 数据库概述
- 数据库: MySQL 8.0
- 字符集: utf8mb4
- 数据库名: blog
- 表前缀: ms_

## 表结构

### 1. 用户表 (ms_sys_user)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 用户ID，主键 |
| account | varchar(64) | 用户账号，唯一 |
| admin | int | 是否管理员 0-否 1-是 |
| avatar | varchar(255) | 用户头像 |
| create_date | bigint | 创建时间戳 |
| deleted | int | 是否删除 0-否 1-是 |
| email | varchar(128) | 邮箱地址 |
| last_login | bigint | 最后登录时间戳 |
| mobile_phone | varchar(20) | 手机号码 |
| nickname | varchar(64) | 用户昵称 |
| password | varchar(64) | 密码（加密） |
| salt | varchar(64) | 密码盐值 |
| status | varchar(20) | 用户状态 |
| is_ban | tinyint(1) | 是否被禁用 |

### 2. 分类表 (ms_category)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 分类ID，主键 |
| avatar | varchar(255) | 分类头像 |
| category_name | varchar(64) | 分类名称，唯一 |
| description | varchar(255) | 分类描述 |

### 3. 文章表 (ms_article)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 文章ID，主键 |
| title | varchar(255) | 文章标题 |
| summary | varchar(500) | 文章摘要 |
| comment_counts | int | 评论数量 |
| view_counts | int | 浏览量 |
| author_id | bigint | 作者ID，外键 |
| body_id | bigint | 文章内容ID，外键 |
| category_id | bigint | 分类ID，外键 |
| weight | int | 置顶权重 0-普通 1-置顶 |
| create_date | bigint | 创建时间戳 |

### 4. 文章内容表 (ms_article_body)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 内容ID，主键 |
| content | longtext | 文章原始内容 |
| content_html | longtext | HTML格式内容 |
| article_id | bigint | 文章ID，外键 |

### 5. 标签表 (ms_tag)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 标签ID，主键 |
| avatar | varchar(255) | 标签头像 |
| tag_name | varchar(64) | 标签名称，唯一 |

### 6. 文章标签关联表 (ms_article_tag)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 关联ID，主键 |
| article_id | bigint | 文章ID，外键 |
| tag_id | bigint | 标签ID，外键 |

### 7. 评论表 (ms_comment)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 评论ID，主键 |
| content | varchar(500) | 评论内容 |
| create_date | bigint | 创建时间戳 |
| article_id | bigint | 文章ID，外键 |
| author_id | bigint | 评论作者ID，外键 |
| parent_id | bigint | 父评论ID，外键 |
| to_uid | bigint | 回复用户ID，外键 |
| level | int | 评论层级 |
| reported | tinyint(1) | 是否被举报 |

### 8. 管理员表 (ms_admin)
| 字段 | 类型 | 说明 |
|------|------|------|
| username | varchar(64) | 用户名，主键 |
| password | varchar(64) | 密码（加密） |
| avatar | varchar(255) | 头像 |

### 9. 活动表 (ms_activity)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 活动ID，主键 |
| title | varchar(255) | 活动标题 |
| description | varchar(500) | 活动描述 |
| poster | varchar(255) | 活动海报 |
| publisher | varchar(64) | 发布者 |
| start_time | datetime | 开始时间 |
| end_time | datetime | 结束时间 |
| participants | int | 参与人数 |
| views | int | 浏览量 |
| detail_introduction | text | 详细介绍 |
| participation_url | varchar(255) | 参与链接 |

## 表关系
- 用户表 ←→ 文章表 (1:N)
- 文章表 ←→ 文章内容表 (1:1)
- 文章表 ←→ 分类表 (N:1)
- 文章表 ←→ 标签表 (N:N 通过关联表)
- 文章表 ←→ 评论表 (1:N)
- 评论表 ←→ 评论表 (1:N 自关联)
- 用户表 ←→ 评论表 (1:N)

## 索引设计
- 主键索引: 所有表的 id 字段
- 唯一索引: account, category_name, tag_name
- 普通索引: author_id, category_id, article_id, parent_id 