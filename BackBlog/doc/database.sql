-- BackBlog 数据库建表脚本
-- 数据库名称: blog
-- 字符集: utf8mb4

CREATE DATABASE IF NOT EXISTS `blog` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `blog`;

-- 用户表
CREATE TABLE `ms_sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `account` varchar(64) NOT NULL COMMENT '账号',
  `admin` int NOT NULL DEFAULT '0' COMMENT '是否管理员',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `create_date` bigint NOT NULL COMMENT '创建时间',
  `deleted` int NOT NULL DEFAULT '0' COMMENT '是否删除',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `last_login` bigint DEFAULT NULL COMMENT '最后登录时间',
  `mobile_phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `salt` varchar(64) DEFAULT NULL COMMENT '密码盐值',
  `status` varchar(20) DEFAULT 'normal' COMMENT '状态',
  `is_ban` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否被禁用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE `ms_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `avatar` varchar(255) DEFAULT NULL COMMENT '分类头像',
  `category_name` varchar(64) NOT NULL COMMENT '分类名称',
  `description` varchar(255) DEFAULT NULL COMMENT '分类描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_name` (`category_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 文章表
CREATE TABLE `ms_article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(255) NOT NULL COMMENT '文章标题',
  `summary` varchar(500) DEFAULT NULL COMMENT '文章摘要',
  `comment_counts` int NOT NULL DEFAULT '0' COMMENT '评论数量',
  `view_counts` int NOT NULL DEFAULT '0' COMMENT '浏览量',
  `author_id` bigint NOT NULL COMMENT '作者ID',
  `body_id` bigint  NULL COMMENT '文章内容ID',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `weight` int NOT NULL DEFAULT '0' COMMENT '置顶权重',
  `create_date` bigint NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_author_id` (`author_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 文章内容表
CREATE TABLE `ms_article_body` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '内容ID',
  `content` longtext COMMENT '文章内容',
  `content_html` longtext COMMENT 'HTML格式内容',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章内容表';

-- 标签表
CREATE TABLE `ms_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `avatar` varchar(255) DEFAULT NULL COMMENT '标签头像',
  `tag_name` varchar(64) NOT NULL COMMENT '标签名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tag_name` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- 文章标签关联表
CREATE TABLE `ms_article_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_tag` (`article_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章标签关联表';

-- 评论表
CREATE TABLE `ms_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `content` varchar(500) NOT NULL COMMENT '评论内容',
  `create_date` bigint NOT NULL COMMENT '创建时间',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `author_id` bigint NOT NULL COMMENT '评论作者ID',
  `parent_id` bigint DEFAULT NULL COMMENT '父评论ID',
  `to_uid` bigint DEFAULT NULL COMMENT '回复用户ID',
  `level` int NOT NULL DEFAULT '1' COMMENT '评论层级',
  `reported` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否被举报',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_author_id` (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 管理员表
CREATE TABLE `ms_admin` (
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 活动表
CREATE TABLE `ms_activity` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `title` varchar(255) NOT NULL COMMENT '活动标题',
  `description` varchar(500) DEFAULT NULL COMMENT '活动描述',
  `poster` varchar(255) DEFAULT NULL COMMENT '活动海报',
  `publisher` varchar(64) NOT NULL COMMENT '发布者',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `participants` int NOT NULL DEFAULT '0' COMMENT '参与人数',
  `views` int NOT NULL DEFAULT '0' COMMENT '浏览量',
  `detail_introduction` text COMMENT '详细介绍',
  `participation_url` varchar(255) DEFAULT NULL COMMENT '参与链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表'; 


-- ==================== 用户表初始化数据 ====================
INSERT INTO `ms_sys_user` (`id`, `account`, `admin`, `avatar`, `create_date`, `deleted`, `email`, `last_login`, `mobile_phone`, `nickname`, `password`, `salt`, `status`, `is_ban`) VALUES
(1, 'admin', 1, '/static/img/userAvatar.png', 1704067200000, 0, 'admin@backblog.com', 1704067200000, '13800138000', '系统管理员', 'e10adc3949ba59abbe56e057f20f883e', '123456', 'normal', 0),
(2, 'zhangsan', 0, '/static/img/userAvatar.png', 1704067200000, 0, 'zhangsan@example.com', 1704067200000, '13800138001', '张三', 'e10adc3949ba59abbe56e057f20f883e', '123456', 'normal', 0),
(3, 'lisi', 0, '/static/img/userAvatar.png', 1704067200000, 0, 'lisi@example.com', 1704067200000, '13800138002', '李四', 'e10adc3949ba59abbe56e057f20f883e', '123456', 'normal', 0),
(4, 'wangwu', 0, '/static/img/userAvatar.png', 1704067200000, 0, 'wangwu@example.com', 1704067200000, '13800138003', '王五', 'e10adc3949ba59abbe56e057f20f883e', '123456', 'normal', 0),
(5, 'zhaoliu', 0, '/static/img/userAvatar.png', 1704067200000, 0, 'zhaoliu@example.com', 1704067200000, '13800138004', '赵六', 'e10adc3949ba59abbe56e057f20f883e', '123456', 'normal', 0);

-- ==================== 管理员表初始化数据 ====================
INSERT INTO `ms_admin` (`username`, `password`, `avatar`) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '/static/img/userAvatar.png');

-- ==================== 分类表初始化数据 ====================
INSERT INTO `ms_category` (`id`, `avatar`, `category_name`, `description`) VALUES
(1, '/static/img/category/java.png', 'Java开发', 'Java编程语言相关技术文章'),
(2, '/static/img/category/python.png', 'Python开发', 'Python编程语言相关技术文章'),
(3, '/static/img/category/frontend.png', '前端开发', 'HTML、CSS、JavaScript等前端技术'),
(4, '/static/img/category/backend.png', '后端开发', '服务器端开发技术'),
(5, '/static/img/category/database.png', '数据库', 'MySQL、Redis、MongoDB等数据库技术'),
(6, '/static/img/category/devops.png', 'DevOps', '持续集成、部署、运维相关技术'),
(7, '/static/img/category/ai.png', '人工智能', '机器学习、深度学习、AI应用'),
(8, '/static/img/category/mobile.png', '移动开发', 'Android、iOS移动应用开发');

-- ==================== 标签表初始化数据 ====================
INSERT INTO `ms_tag` (`id`, `avatar`, `tag_name`) VALUES
(1, '/static/img/tag/java.png', 'Java'),
(2, '/static/img/tag/spring.png', 'Spring'),
(3, '/static/img/tag/springboot.png', 'SpringBoot'),
(4, '/static/img/tag/mysql.png', 'MySQL'),
(5, '/static/img/tag/redis.png', 'Redis'),
(6, '/static/img/tag/python.png', 'Python'),
(7, '/static/img/tag/javascript.png', 'JavaScript'),
(8, '/static/img/tag/vue.png', 'Vue'),
(9, '/static/img/tag/react.png', 'React'),
(10, '/static/img/tag/docker.png', 'Docker'),
(11, '/static/img/tag/kubernetes.png', 'Kubernetes'),
(12, '/static/img/tag/ai.png', 'AI'),
(13, '/static/img/tag/machine-learning.png', '机器学习'),
(14, '/static/img/tag/microservice.png', '微服务'),
(15, '/static/img/tag/cloud.png', '云计算');

-- ==================== 文章内容表初始化数据 ====================
INSERT INTO `ms_article_body` (`id`, `content`, `content_html`, `article_id`) VALUES
(1, '# Spring Boot 入门指南\n\n## 什么是Spring Boot？\n\nSpring Boot是一个基于Spring框架的快速开发框架，它简化了Spring应用的初始搭建以及开发过程。\n\n## 主要特性\n\n- 自动配置\n- 起步依赖\n- 内嵌服务器\n- 生产就绪\n\n## 快速开始\n\n### 1. 创建项目\n\n```xml\n<parent>\n    <groupId>org.springframework.boot</groupId>\n    <artifactId>spring-boot-starter-parent</artifactId>\n    <version>2.7.0</version>\n</parent>\n```\n\n### 2. 添加依赖\n\n```xml\n<dependency>\n    <groupId>org.springframework.boot</groupId>\n    <artifactId>spring-boot-starter-web</artifactId>\n</dependency>\n```\n\n### 3. 编写启动类\n\n```java\n@SpringBootApplication\npublic class Application {\n    public static void main(String[] args) {\n        SpringApplication.run(Application.class, args);\n    }\n}\n```\n\n## 总结\n\nSpring Boot大大简化了Spring应用的开发，是Java企业级开发的首选框架。', 
'<h1>Spring Boot 入门指南</h1>\n<h2>什么是Spring Boot？</h2>\n<p>Spring Boot是一个基于Spring框架的快速开发框架，它简化了Spring应用的初始搭建以及开发过程。</p>\n<h2>主要特性</h2>\n<ul>\n<li>自动配置</li>\n<li>起步依赖</li>\n<li>内嵌服务器</li>\n<li>生产就绪</li>\n</ul>\n<h2>快速开始</h2>\n<h3>1. 创建项目</h3>\n<pre><code>&lt;parent&gt;\n    &lt;groupId&gt;org.springframework.boot&lt;/groupId&gt;\n    &lt;artifactId&gt;spring-boot-starter-parent&lt;/artifactId&gt;\n    &lt;version&gt;2.7.0&lt;/version&gt;\n&lt;/parent&gt;\n</code></pre>\n<h3>2. 添加依赖</h3>\n<pre><code>&lt;dependency&gt;\n    &lt;groupId&gt;org.springframework.boot&lt;/groupId&gt;\n    &lt;artifactId&gt;spring-boot-starter-web&lt;/artifactId&gt;\n&lt;/dependency&gt;\n</code></pre>\n<h3>3. 编写启动类</h3>\n<pre><code>@SpringBootApplication\npublic class Application {\n    public static void main(String[] args) {\n        SpringApplication.run(Application.class, args);\n    }\n}\n</code></pre>\n<h2>总结</h2>\n<p>Spring Boot大大简化了Spring应用的开发，是Java企业级开发的首选框架。</p>', 1),

(2, '# Vue.js 3.0 新特性详解\n\n## 组合式API\n\nVue 3.0引入了组合式API，这是对选项式API的补充。\n\n### 基本用法\n\n```javascript\nimport { ref, reactive, onMounted } from \'vue\'\n\nexport default {\n  setup() {\n    const count = ref(0)\n    const state = reactive({\n      name: \'Vue 3\',\n      version: \'3.0.0\'\n    })\n    \n    onMounted(() => {\n      console.log(\'组件已挂载\')\n    })\n    \n    return {\n      count,\n      state\n    }\n  }\n}\n```\n\n## 性能提升\n\n- 更小的包体积\n- 更好的Tree-shaking\n- 更快的渲染速度\n\n## TypeScript支持\n\nVue 3.0是用TypeScript重写的，提供了更好的类型支持。\n\n## 总结\n\nVue 3.0带来了更好的性能和开发体验。', 
'<h1>Vue.js 3.0 新特性详解</h1>\n<h2>组合式API</h2>\n<p>Vue 3.0引入了组合式API，这是对选项式API的补充。</p>\n<h3>基本用法</h3>\n<pre><code>import { ref, reactive, onMounted } from \'vue\'\n\nexport default {\n  setup() {\n    const count = ref(0)\n    const state = reactive({\n      name: \'Vue 3\',\n      version: \'3.0.0\'\n    })\n    \n    onMounted(() => {\n      console.log(\'组件已挂载\')\n    })\n    \n    return {\n      count,\n      state\n    }\n  }\n}\n</code></pre>\n<h2>性能提升</h2>\n<ul>\n<li>更小的包体积</li>\n<li>更好的Tree-shaking</li>\n<li>更快的渲染速度</li>\n</ul>\n<h2>TypeScript支持</h2>\n<p>Vue 3.0是用TypeScript重写的，提供了更好的类型支持。</p>\n<h2>总结</h2>\n<p>Vue 3.0带来了更好的性能和开发体验。</p>', 2),

(3, '# Docker 容器化部署指南\n\n## 什么是Docker？\n\nDocker是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的容器中。\n\n## Docker的优势\n\n- 一致性环境\n- 快速部署\n- 资源隔离\n- 版本控制\n\n## 基本命令\n\n```bash\n# 构建镜像\ndocker build -t myapp .\n\n# 运行容器\ndocker run -d -p 8080:8080 myapp\n\n# 查看容器\ndocker ps\n\n# 停止容器\ndocker stop <container_id>\n```\n\n## Dockerfile示例\n\n```dockerfile\nFROM openjdk:11-jre-slim\nCOPY target/myapp.jar app.jar\nEXPOSE 8080\nENTRYPOINT ["java", "-jar", "/app.jar"]\n```\n\n## 总结\n\nDocker是现代应用部署的标准工具。', 
'<h1>Docker 容器化部署指南</h1>\n<h2>什么是Docker？</h2>\n<p>Docker是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的容器中。</p>\n<h2>Docker的优势</h2>\n<ul>\n<li>一致性环境</li>\n<li>快速部署</li>\n<li>资源隔离</li>\n<li>版本控制</li>\n</ul>\n<h2>基本命令</h2>\n<pre><code># 构建镜像\ndocker build -t myapp .\n\n# 运行容器\ndocker run -d -p 8080:8080 myapp\n\n# 查看容器\ndocker ps\n\n# 停止容器\ndocker stop &lt;container_id&gt;\n</code></pre>\n<h2>Dockerfile示例</h2>\n<pre><code>FROM openjdk:11-jre-slim\nCOPY target/myapp.jar app.jar\nEXPOSE 8080\nENTRYPOINT ["java", "-jar", "/app.jar"]\n</code></pre>\n<h2>总结</h2>\n<p>Docker是现代应用部署的标准工具。</p>', 3),

(4, '# MySQL 性能优化实践\n\n## 索引优化\n\n### 创建合适的索引\n\n```sql\n-- 为常用查询字段创建索引\nCREATE INDEX idx_user_email ON users(email);\nCREATE INDEX idx_article_author ON articles(author_id);\n```\n\n### 复合索引\n\n```sql\n-- 创建复合索引，注意字段顺序\nCREATE INDEX idx_user_status_created ON users(status, created_at);\n```\n\n## 查询优化\n\n### 避免SELECT *\n\n```sql\n-- 不推荐\nSELECT * FROM users;\n\n-- 推荐\nSELECT id, name, email FROM users;\n```\n\n### 使用LIMIT\n\n```sql\n-- 分页查询\nSELECT * FROM articles ORDER BY created_at DESC LIMIT 10 OFFSET 20;\n```\n\n## 配置优化\n\n### my.cnf配置\n\n```ini\n[mysqld]\ninnodb_buffer_pool_size = 1G\ninnodb_log_file_size = 256M\nmax_connections = 200\n```\n\n## 总结\n\n合理的索引设计和查询优化是MySQL性能的关键。', 
'<h1>MySQL 性能优化实践</h1>\n<h2>索引优化</h2>\n<h3>创建合适的索引</h3>\n<pre><code>-- 为常用查询字段创建索引\nCREATE INDEX idx_user_email ON users(email);\nCREATE INDEX idx_article_author ON articles(author_id);\n</code></pre>\n<h3>复合索引</h3>\n<pre><code>-- 创建复合索引，注意字段顺序\nCREATE INDEX idx_user_status_created ON users(status, created_at);\n</code></pre>\n<h2>查询优化</h2>\n<h3>避免SELECT *</h3>\n<pre><code>-- 不推荐\nSELECT * FROM users;\n\n-- 推荐\nSELECT id, name, email FROM users;\n</code></pre>\n<h3>使用LIMIT</h3>\n<pre><code>-- 分页查询\nSELECT * FROM articles ORDER BY created_at DESC LIMIT 10 OFFSET 20;\n</code></pre>\n<h2>配置优化</h2>\n<h3>my.cnf配置</h3>\n<pre><code>[mysqld]\ninnodb_buffer_pool_size = 1G\ninnodb_log_file_size = 256M\nmax_connections = 200\n</code></pre>\n<h2>总结</h2>\n<p>合理的索引设计和查询优化是MySQL性能的关键。</p>', 4),

(5, '# 微服务架构设计原则\n\n## 什么是微服务？\n\n微服务是一种将应用程序构建为一组小型自治服务的架构风格。\n\n## 设计原则\n\n### 1. 单一职责\n\n每个服务应该只负责一个业务功能。\n\n### 2. 服务自治\n\n服务之间应该松耦合，可以独立部署和扩展。\n\n### 3. 数据隔离\n\n每个服务拥有自己的数据库。\n\n## 技术栈\n\n- **服务注册与发现**: Eureka, Consul\n- **API网关**: Zuul, Gateway\n- **负载均衡**: Ribbon, LoadBalancer\n- **熔断器**: Hystrix, Resilience4j\n- **配置中心**: Config Server\n\n## 示例架构\n\n```\n┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐\n│   API Gateway   │    │   User Service  │    │  Order Service  │\n└─────────────────┘    └─────────────────┘    └─────────────────┘\n         │                       │                       │\n         └───────────────────────┼───────────────────────┘\n                                 │\n                    ┌─────────────────┐\n                    │  Product Service│\n                    └─────────────────┘\n```\n\n## 总结\n\n微服务架构提供了更好的可扩展性和维护性。', 
'<h1>微服务架构设计原则</h1>\n<h2>什么是微服务？</h2>\n<p>微服务是一种将应用程序构建为一组小型自治服务的架构风格。</p>\n<h2>设计原则</h2>\n<h3>1. 单一职责</h3>\n<p>每个服务应该只负责一个业务功能。</p>\n<h3>2. 服务自治</h3>\n<p>服务之间应该松耦合，可以独立部署和扩展。</p>\n<h3>3. 数据隔离</h3>\n<p>每个服务拥有自己的数据库。</p>\n<h2>技术栈</h2>\n<ul>\n<li><strong>服务注册与发现</strong>: Eureka, Consul</li>\n<li><strong>API网关</strong>: Zuul, Gateway</li>\n<li><strong>负载均衡</strong>: Ribbon, LoadBalancer</li>\n<li><strong>熔断器</strong>: Hystrix, Resilience4j</li>\n<li><strong>配置中心</strong>: Config Server</li>\n</ul>\n<h2>示例架构</h2>\n<pre><code>┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐\n│   API Gateway   │    │   User Service  │    │  Order Service  │\n└─────────────────┘    └─────────────────┘    └─────────────────┘\n         │                       │                       │\n         └───────────────────────┼───────────────────────┘\n                                 │\n                    ┌─────────────────┐\n                    │  Product Service│\n                    └─────────────────┘\n</code></pre>\n<h2>总结</h2>\n<p>微服务架构提供了更好的可扩展性和维护性。</p>', 5);

-- ==================== 文章表初始化数据 ====================
INSERT INTO `ms_article` (`id`, `title`, `summary`, `comment_counts`, `view_counts`, `author_id`, `body_id`, `category_id`, `weight`, `create_date`) VALUES
(1, 'Spring Boot 入门指南', 'Spring Boot是一个基于Spring框架的快速开发框架，它简化了Spring应用的初始搭建以及开发过程。本文详细介绍Spring Boot的主要特性和快速开始方法。', 5, 1200, 1, 1, 1, 1, 1704067200000),
(2, 'Vue.js 3.0 新特性详解', 'Vue 3.0带来了组合式API、更好的TypeScript支持等新特性。本文深入解析Vue 3.0的变化和优势。', 3, 800, 2, 2, 3, 0, 1704153600000),
(3, 'Docker 容器化部署指南', 'Docker是现代应用部署的标准工具。本文介绍Docker的基本概念、优势以及实际应用中的最佳实践。', 8, 1500, 1, 3, 6, 0, 1704240000000),
(4, 'MySQL 性能优化实践', '数据库性能优化是系统性能的关键。本文分享MySQL索引优化、查询优化和配置优化的实践经验。', 12, 2000, 3, 4, 5, 0, 1704326400000),
(5, '微服务架构设计原则', '微服务架构提供了更好的可扩展性和维护性。本文介绍微服务的设计原则和技术栈选择。', 6, 1000, 1, 5, 4, 0, 1704412800000);

-- ==================== 文章标签关联表初始化数据 ====================
INSERT INTO `ms_article_tag` (`id`, `article_id`, `tag_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 2, 7),
(5, 2, 8),
(6, 3, 10),
(7, 4, 4),
(8, 5, 14),
(9, 5, 2),
(10, 5, 3);

-- ==================== 评论表初始化数据 ====================
INSERT INTO `ms_comment` (`id`, `content`, `create_date`, `article_id`, `author_id`, `parent_id`, `to_uid`, `level`, `reported`) VALUES
(1, '这篇文章写得很好，对Spring Boot的介绍很详细！', 1704067200000, 1, 2, NULL, NULL, 1, 0),
(2, '感谢分享，对我学习Spring Boot很有帮助', 1704067200000, 1, 3, NULL, NULL, 1, 0),
(3, '请问如何配置数据库连接？', 1704067200000, 1, 4, NULL, NULL, 1, 0),
(4, '在application.yml中配置即可，具体配置如下...', 1704067200000, 1, 1, 3, 4, 2, 0),
(5, 'Vue 3.0的组合式API确实很强大', 1704153600000, 2, 3, NULL, NULL, 1, 0),
(6, 'Docker确实简化了部署流程', 1704240000000, 3, 2, NULL, NULL, 1, 0),
(7, 'MySQL优化这块讲得很实用', 1704326400000, 4, 5, NULL, NULL, 1, 0),
(8, '微服务架构确实是大趋势', 1704412800000, 5, 4, NULL, NULL, 1, 0);

-- ==================== 活动表初始化数据 ====================
INSERT INTO `ms_activity` (`id`, `title`, `description`, `poster`, `publisher`, `start_time`, `end_time`, `participants`, `views`, `detail_introduction`, `participation_url`) VALUES
(1, 'Spring Boot 技术分享会', '邀请资深Java开发工程师分享Spring Boot在企业级应用中的最佳实践，包括性能优化、安全配置、微服务集成等内容。', '/static/img/activity/spring-boot.png', 'admin', '2024-01-15 14:00:00', '2024-01-15 16:00:00', 25, 150, '本次分享会将深入探讨Spring Boot在企业级开发中的应用，包括但不限于：\n\n1. Spring Boot自动配置原理\n2. 性能优化技巧\n3. 安全配置最佳实践\n4. 微服务架构集成\n5. 生产环境部署策略\n\n适合有一定Java基础的开发者参加。', 'https://meeting.example.com/spring-boot'),
(2, '前端技术发展趋势讨论', '探讨2024年前端技术的发展趋势，包括Vue 3.0、React 18、TypeScript等热门技术的应用场景。', '/static/img/activity/frontend.png', 'admin', '2024-01-20 19:00:00', '2024-01-20 21:00:00', 18, 120, '前端技术日新月异，本次讨论将围绕以下话题展开：\n\n1. Vue 3.0组合式API的实践应用\n2. React 18新特性解析\n3. TypeScript在企业项目中的推广\n4. 前端工程化工具链\n5. 跨平台开发技术对比\n\n欢迎前端开发者踊跃参与讨论！', 'https://meeting.example.com/frontend'),
(3, 'DevOps实践分享', '分享DevOps在企业中的实践案例，包括CI/CD流水线搭建、容器化部署、监控告警等关键环节。', '/static/img/activity/devops.png', 'admin', '2024-01-25 15:00:00', '2024-01-25 17:00:00', 30, 200, 'DevOps是现代软件开发的核心理念，本次分享将涵盖：\n\n1. CI/CD流水线设计与实现\n2. Docker容器化最佳实践\n3. Kubernetes集群管理\n4. 监控告警体系建设\n5. 自动化测试策略\n6. 安全扫描与合规检查\n\n适合运维工程师和开发工程师参加。', 'https://meeting.example.com/devops'),
(4, '数据库性能优化实战', '资深DBA分享MySQL、Redis等数据库的性能优化实战经验，包括索引设计、查询优化、缓存策略等。', '/static/img/activity/database.png', 'admin', '2024-01-30 14:30:00', '2024-01-30 16:30:00', 22, 180, '数据库性能直接影响系统整体性能，本次实战分享包括：\n\n1. MySQL索引优化策略\n2. 慢查询分析与优化\n3. Redis缓存设计模式\n4. 分库分表实践\n5. 数据库监控与告警\n6. 备份恢复策略\n\n适合后端开发工程师和DBA参加。', 'https://meeting.example.com/database'),
(5, '人工智能技术应用', '探讨AI技术在实际项目中的应用，包括机器学习、深度学习、自然语言处理等技术的落地实践。', '/static/img/activity/ai.png', 'admin', '2024-02-05 19:00:00', '2024-02-05 21:00:00', 35, 250, 'AI技术正在改变各行各业，本次分享将探讨：\n\n1. 机器学习算法选择\n2. 深度学习框架对比\n3. 自然语言处理应用\n4. 计算机视觉实践\n5. AI模型部署策略\n6. 数据标注与质量控制\n\n适合对AI技术感兴趣的开发者参加。', 'https://meeting.example.com/ai');
