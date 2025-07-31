# BackBlog 项目架构文档

## 架构概述

BackBlog 采用经典的三层架构设计，基于 Spring Boot 框架构建，使用 MyBatis Plus 作为 ORM 框架，MySQL 作为数据库。

## 技术栈

### 核心框架
- **Spring Boot 3.3.4**: 主框架
- **MyBatis Plus 3.5.7**: ORM 框架
- **MySQL 8.0**: 数据库

### 安全认证
- **JWT**: JSON Web Token 认证
- **MD5**: 密码加密

### 文件存储
- **七牛云**: 对象存储服务

### 工具库
- **Lombok**: 代码简化
- **FastJSON**: JSON 处理
- **Commons Codec**: MD5 加密
- **Joda Time**: 时间处理

## 项目结构

```
src/main/java/org/hrc/backblog/
├── BackBlogApplication.java          # 主启动类
├── common/                          # 公共组件
│   └── aop/                        # 切面编程
│       ├── LogAnnotation.java       # 日志注解
│       └── LogAspect.java          # 日志切面
├── config/                          # 配置类
│   ├── MybatisPlusConfig.java      # MyBatis Plus 配置
│   └── WebMVCConfig.java           # Web MVC 配置
├── controller/                      # 控制器层
│   ├── ActivityController.java      # 活动控制器
│   ├── AdminController.java         # 管理员控制器
│   ├── ArticleController.java       # 文章控制器
│   ├── CategoryController.java      # 分类控制器
│   ├── CommentsController.java      # 评论控制器
│   ├── LoginController.java         # 登录控制器
│   ├── RegisterController.java      # 注册控制器
│   ├── SearchController.java        # 搜索控制器
│   ├── TagsController.java          # 标签控制器
│   ├── TestController.java          # 测试控制器
│   ├── UploadController.java        # 上传控制器
│   └── UsersController.java         # 用户控制器
├── dao/                            # 数据访问层
│   ├── dos/                        # 数据传输对象
│   │   └── Archives.java           # 归档对象
│   ├── mapper/                     # MyBatis 映射器
│   │   ├── ActivityMapper.java     # 活动映射器
│   │   ├── AdminMapper.java        # 管理员映射器
│   │   ├── ArticleBodyMapper.java  # 文章内容映射器
│   │   ├── ArticleCategoryMapper.java # 文章分类映射器
│   │   ├── ArticleMapper.java      # 文章映射器
│   │   ├── ArticleTagMapper.java   # 文章标签映射器
│   │   ├── CategoryMapper.java     # 分类映射器
│   │   ├── CommentsMapper.java     # 评论映射器
│   │   ├── SysUserMapper.java      # 用户映射器
│   │   └── TagMapper.java          # 标签映射器
│   └── pojo/                       # 实体类
│       ├── Activity.java           # 活动实体
│       ├── Admin.java              # 管理员实体
│       ├── Article.java            # 文章实体
│       ├── ArticleBody.java        # 文章内容实体
│       ├── ArticleTag.java         # 文章标签实体
│       ├── Category.java           # 分类实体
│       ├── Comment.java            # 评论实体
│       ├── SysUser.java            # 用户实体
│       └── Tag.java                # 标签实体
├── handler/                        # 处理器
│   ├── AllExceptionHandler.java    # 全局异常处理器
│   └── LoginInterceptor.java       # 登录拦截器
├── service/                        # 服务层
│   ├── ActivityService.java        # 活动服务接口
│   ├── AdminService.java           # 管理员服务接口
│   ├── ArticleBodyService.java     # 文章内容服务接口
│   ├── ArticleCategory.java        # 文章分类服务接口
│   ├── ArticleService.java         # 文章服务接口
│   ├── CategoryService.java        # 分类服务接口
│   ├── CommentsService.java        # 评论服务接口
│   ├── LoginService.java           # 登录服务接口
│   ├── SysUserService.java         # 用户服务接口
│   ├── TagService.java             # 标签服务接口
│   ├── ThreadService.java          # 线程服务接口
│   ├── TokenService.java           # Token 服务接口
│   └── Impl/                       # 服务实现类
│       ├── ActivityServiceImpl.java # 活动服务实现
│       ├── AdminServiceImpl.java    # 管理员服务实现
│       ├── ArticleBodyServiceImpl.java # 文章内容服务实现
│       ├── ArticleCategoryImpl.java # 文章分类服务实现
│       ├── ArticleServiceImpl.java  # 文章服务实现
│       ├── CategoryServiceImpl.java # 分类服务实现
│       ├── CommentsServiceImpl.java # 评论服务实现
│       ├── LoginServiceImpl.java    # 登录服务实现
│       ├── SysUserServiceImpl.java  # 用户服务实现
│       ├── TagServiceImpl.java      # 标签服务实现
│       ├── ThreadServiceImpl.java   # 线程服务实现
│       └── TokenServiceImpl.java    # Token 服务实现
├── utils/                          # 工具类
│   ├── HttpContextUtils.java       # HTTP 上下文工具
│   ├── IpUtils.java                # IP 工具
│   ├── JwtUtil.java                # JWT 工具
│   ├── QiniuUtils.java             # 七牛云工具
│   └── UserThreadLocal.java        # 用户线程本地变量
└── vo/                             # 视图对象
    ├── ActivityVo.java             # 活动视图对象
    ├── AdminVo.java                # 管理员视图对象
    ├── ArticleBodyVo.java          # 文章内容视图对象
    ├── ArticleVo.java              # 文章视图对象
    ├── CategoryVo.java             # 分类视图对象
    ├── CommentVo.java              # 评论视图对象
    ├── ErrorCode.java              # 错误码
    ├── LoginUserVo.java            # 登录用户视图对象
    ├── Result.java                 # 统一响应对象
    ├── TagVo.java                  # 标签视图对象
    ├── UserInfoVo.java             # 用户信息视图对象
    ├── UserVo.java                 # 用户视图对象
    └── params/                     # 参数对象
        ├── AdminArticleParam.java  # 管理员文章参数
        ├── AdminLoginParam.java    # 管理员登录参数
        ├── ArticleBodyParam.java   # 文章内容参数
        ├── ArticleParam.java       # 文章参数
        ├── CategoryParam.java      # 分类参数
        ├── CommentParam.java       # 评论参数
        ├── LoginParam.java         # 登录参数
        ├── ModifyUserParam.java    # 修改用户参数
        └── PageParams.java         # 分页参数
```

## 分层架构

### 1. 表现层 (Controller)
- 处理 HTTP 请求和响应
- 参数验证和转换
- 调用业务层服务
- 返回统一格式的响应

### 2. 业务层 (Service)
- 实现业务逻辑
- 事务管理
- 数据验证和处理
- 调用数据访问层

### 3. 数据访问层 (DAO)
- 数据库操作
- SQL 映射
- 数据持久化

## 核心功能模块

### 1. 用户管理模块
- 用户注册、登录
- 用户信息管理
- 权限控制
- JWT 认证

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

## 设计模式

### 1. MVC 模式
- Model: 实体类和 DAO 层
- View: 前端页面
- Controller: 控制器层

### 2. 单例模式
- 工具类使用单例模式
- Spring 容器管理的 Bean

### 3. 工厂模式
- Spring 的 Bean 工厂
- MyBatis 的 SqlSessionFactory

### 4. 代理模式
- Spring AOP 切面编程
- MyBatis 的动态代理

## 安全设计

### 1. 认证机制
- JWT Token 认证
- 密码 MD5 加密
- 盐值加密

### 2. 权限控制
- 登录拦截器
- 角色权限控制
- 接口权限验证

### 3. 数据安全
- SQL 注入防护
- XSS 攻击防护
- CSRF 防护

## 性能优化

### 1. 数据库优化
- 索引优化
- 查询优化
- 连接池配置

### 2. 缓存策略
- 热点数据缓存
- 用户信息缓存
- 文章内容缓存

### 3. 异步处理
- 文件上传异步处理
- 邮件发送异步处理
- 日志记录异步处理

## 扩展性设计

### 1. 模块化设计
- 功能模块独立
- 接口与实现分离
- 配置与代码分离

### 2. 插件化架构
- 支持功能插件
- 支持主题插件
- 支持存储插件

### 3. 微服务准备
- 服务接口标准化
- 数据模型标准化
- 配置中心化 