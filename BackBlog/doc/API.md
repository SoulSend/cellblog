# BackBlog API 接口文档

## 基础信息
- 基础URL: http://localhost:8888
- 请求格式: JSON
- 响应格式: JSON
- 认证方式: JWT Token

## 通用响应格式
```json
{
  "success": true,
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

## 1. 用户相关接口

### 1.1 用户注册
- URL: `/api/users/register`
- 方法: POST
- 请求参数:
```json
{
  "account": "用户名",
  "password": "密码",
  "nickname": "昵称",
  "email": "邮箱"
}
```

### 1.2 用户登录
- URL: `/api/users/login`
- 方法: POST
- 请求参数:
```json
{
  "account": "用户名",
  "password": "密码"
}
```

### 1.3 获取用户信息
- URL: `/api/users/currentUser`
- 方法: GET
- 认证: 需要

## 2. 文章相关接口

### 2.1 获取文章列表
- URL: `/api/articles`
- 方法: GET
- 参数: page, size, categoryId, tagId

### 2.2 获取文章详情
- URL: `/api/articles/{id}`
- 方法: GET

### 2.3 发布文章
- URL: `/api/articles`
- 方法: POST
- 认证: 需要

### 2.4 更新文章
- URL: `/api/articles/{id}`
- 方法: PUT
- 认证: 需要

### 2.5 删除文章
- URL: `/api/articles/{id}`
- 方法: DELETE
- 认证: 需要

## 3. 分类相关接口

### 3.1 获取分类列表
- URL: `/api/categories`
- 方法: GET

### 3.2 创建分类
- URL: `/api/categories`
- 方法: POST
- 认证: 需要 (管理员)

## 4. 标签相关接口

### 4.1 获取标签列表
- URL: `/api/tags`
- 方法: GET

### 4.2 创建标签
- URL: `/api/tags`
- 方法: POST
- 认证: 需要 (管理员)

## 5. 评论相关接口

### 5.1 获取文章评论
- URL: `/api/comments/article/{articleId}`
- 方法: GET

### 5.2 发表评论
- URL: `/api/comments`
- 方法: POST
- 认证: 需要

## 6. 活动相关接口

### 6.1 获取活动列表
- URL: `/api/activities`
- 方法: GET

### 6.2 获取活动详情
- URL: `/api/activities/{id}`
- 方法: GET

### 6.3 创建活动
- URL: `/api/activities`
- 方法: POST
- 认证: 需要 (管理员)

## 7. 文件上传接口

### 7.1 上传图片
- URL: `/api/upload`
- 方法: POST
- 认证: 需要
- 格式: multipart/form-data

## 错误码说明
- 200: 成功
- 400: 请求参数错误
- 401: 未认证
- 403: 无权限
- 404: 资源不存在
- 500: 服务器内部错误 