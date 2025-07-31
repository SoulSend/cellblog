# CellBlog接口文档

# 1. 登录

## 1.1 接口说明

接口url：/login

请求方式：POST

请求参数：

| 参数名称 | 参数类型 | 说明 |
| -------- | -------- | ---- |
| account  | string   | 账号 |
| password | string   | 密码 |
|          |          |      |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": "token"
}
~~~

# 2. 获取用户信息

## 2.1 接口说明

接口url：/users/currentUser

请求方式：GET

请求参数：

| 参数名称      | 参数类型 | 说明            |
| ------------- | -------- | --------------- |
| Authorization | string   | 头部信息(TOKEN) |
|               |          |                 |
|               |          |                 |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": {
        "id":1,
        "account":"1",
        "nickaname":"1",
        "avatar":"ss"
    }
}
~~~

# 3. 注册

## 3.1 接口说明

接口url：/register

请求方式：POST

请求参数：

| 参数名称 | 参数类型 | 说明 |
| -------- | -------- | ---- |
| account  | string   | 账号 |
| password | string   | 密码 |
| nickname | string   | 昵称 |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": "token"
}
~~~

# 4. 首页-文章列表

## 4.1 接口说明

接口url：/articles

请求方式：POST

请求参数：

| 参数名称   | 参数类型 | 说明           |
| ---------- | -------- | -------------- |
| page       | int      | 当前页数       |
| pageSize   | int      | 每页显示的数量 |
| categoryId | int      | 文章分类id     |
| tagId      | int      | 文章标签id     |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": [
        {
            "id": 1,
            "title": "springboot介绍以及入门案例",
            "summary": "通过Spring Boot实现的服务，只需要依靠一个Java类，把它打包成jar，并通过`java -jar`命令就可以运行起来。\r\n\r\n这一切相较于传统Spring应用来说，已经变得非常的轻便、简单。",
            "commentCounts": 2,
            "viewCounts": 54,
            "weight": 1,
            "createDate": "2609-06-26 15:58",
            "author": "12",
            "body": null,
            "tags": [
                {
                    "id": 5,
                    "avatar": null,
                    "tagName": "444"
                },
                {
                    "id": 7,
                    "avatar": null,
                    "tagName": "22"
                },
                {
                    "id": 8,
                    "avatar": null,
                    "tagName": "11"
                }
            ],
            "categorys": null
        },
        {
            "id": 9,
            "title": "Vue.js 是什么",
            "summary": "Vue (读音 /vjuː/，类似于 view) 是一套用于构建用户界面的渐进式框架。",
            "commentCounts": 0,
            "viewCounts": 3,
            "weight": 0,
            "createDate": "2609-06-27 11:25",
            "author": "12",
            "body": null,
            "tags": [
                {
                    "id": 7,
                    "avatar": null,
                    "tagName": "22"
                }
            ],
            "categorys": null
        },
        {
            "id": 10,
            "title": "Element相关",
            "summary": "本节将介绍如何在项目中使用 Element。",
            "commentCounts": 0,
            "viewCounts": 3,
            "weight": 0,
            "createDate": "2609-06-27 11:25",
            "author": "12",
            "body": null,
            "tags": [
                {
                    "id": 5,
                    "avatar": null,
                    "tagName": "444"
                },
                {
                    "id": 6,
                    "avatar": null,
                    "tagName": "33"
                },
                {
                    "id": 7,
                    "avatar": null,
                    "tagName": "22"
                },
                {
                    "id": 8,
                    "avatar": null,
                    "tagName": "11"
                }
            ],
            "categorys": null
        }
    ]
}
~~~

# 5. 首页-最热标签

## 5.1 接口说明

接口url：/tags/hot

请求方式：GET

请求参数：无

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": [
        {
            "id":1,
            "tagName":"4444"
        }
    ]
}
~~~

# 6. 首页-最热文章

## 6.1 接口说明

接口url：/articles/hot

请求方式：POST

请求参数：

| 参数名称 | 参数类型 | 说明 |
| -------- | -------- | ---- |
|          |          |      |
|          |          |      |
|          |          |      |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": [
        {
            "id": 1,
            "title": "springboot介绍以及入门案例",
        },
        {
            "id": 9,
            "title": "Vue.js 是什么",
        },
        {
            "id": 10,
            "title": "Element相关",
            
        }
    ]
}
~~~

# 7. 首页-最新文章

## 7.1 接口说明

接口url：/articles/new

请求方式：POST

请求参数：

| 参数名称 | 参数类型 | 说明 |
| -------- | -------- | ---- |
|          |          |      |
|          |          |      |
|          |          |      |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": [
        {
            "id": 1,
            "title": "springboot介绍以及入门案例",
        },
        {
            "id": 9,
            "title": "Vue.js 是什么",
        },
        {
            "id": 10,
            "title": "Element相关",
            
        }
    ]
}

~~~

# 8. 首页-文章归档

## 8.1接口说明

接口url：/articles/listArchives

请求方式：POST

请求参数：

| 参数名称 | 参数类型 | 说明 |
| -------- | -------- | ---- |
|          |          |      |
|          |          |      |
|          |          |      |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": [
        {
            "year": "2021",
            "month": "6",
            "count": 2
        }
            
    ]
}
~~~

# 9. 文章详情

## 9.1 接口说明

接口url：/articles/view/{id}

请求方式：POST

请求参数：

| 参数名称 | 参数类型 | 说明               |
| -------- | -------- | ------------------ |
| id       | long     | 文章id（路径参数） |
|          |          |                    |
|          |          |                    |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": "token"
}
~~~

# 10.评论列表

## 10.1 接口说明

接口url：/comments/article/{id}

请求方式：GET

请求参数：

| 参数名称 | 参数类型 | 说明               |
| -------- | -------- | ------------------ |
| id       | long     | 文章id（路径参数） |
|          |          |                    |
|          |          |                    |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": [
        {
            "id": 53,
            "author": {
                "nickname": "李四",
                "avatar": "http://localhost:8080/static/img/logo.b3a48c0.png",
                "id": 1
            },
            "content": "写的好",
            "childrens": [
                {
                    "id": 54,
                    "author": {
                        "nickname": "李四",
                        "avatar": "http://localhost:8080/static/img/logo.b3a48c0.png",
                        "id": 1
                    },
                    "content": "111",
                    "childrens": [],
                    "createDate": "1973-11-26 08:52",
                    "level": 2,
                    "toUser": {
                        "nickname": "李四",
                        "avatar": "http://localhost:8080/static/img/logo.b3a48c0.png",
                        "id": 1
                    }
                }
            ],
            "createDate": "1973-11-27 09:53",
            "level": 1,
            "toUser": null
        }
    ]
}
~~~

# 11. 评论

## 11.1 接口说明

接口url：/comments/create/change

请求方式：POST

请求参数：

| 参数名称  | 参数类型 | 说明           |
| --------- | -------- | -------------- |
| articleId | long     | 文章id         |
| content   | string   | 评论内容       |
| parent    | long     | 父评论id       |
| toUserId  | long     | 被评论的用户id |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": null
}
~~~

# 12.写文章

写文章需要 三个接口：

1. 获取所有文章类别

2. 获取所有标签
3. 发布文章

## 12. 1所有文章分类

### 12.1.1 接口说明

接口url：/categorys

请求方式：GET

请求参数：

| 参数名称 | 参数类型 | 说明 |
| -------- | -------- | ---- |
|          |          |      |
|          |          |      |
|          |          |      |

返回数据：

~~~json
{
    "success":true,
 	"code":200,
    "msg":"success",
    "data":
    [
        {"id":1,"avatar":"/category/front.png","categoryName":"前端"},	
        {"id":2,"avatar":"/category/back.png","categoryName":"后端"},
        {"id":3,"avatar":"/category/lift.jpg","categoryName":"生活"},
        {"id":4,"avatar":"/category/database.png","categoryName":"数据库"},
        {"id":5,"avatar":"/category/language.png","categoryName":"编程语言"}
    ]
}
~~~

## 12. 2所有文章标签

### 12.2.1 接口说明

接口url：/tags

请求方式：GET

请求参数：

| 参数名称 | 参数类型 | 说明 |
| -------- | -------- | ---- |
|          |          |      |
|          |          |      |
|          |          |      |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": [
        {
            "id": 5,
            "tagName": "springboot"
        },
        {
            "id": 6,
            "tagName": "spring"
        },
        {
            "id": 7,
            "tagName": "springmvc"
        },
        {
            "id": 8,
            "tagName": "11"
        }
    ]
}
~~~

## 12.3 发布文章

### 12.3.1 接口说明

接口url：/articles/publish

请求方式：POST

请求参数：

| 参数名称 | 参数类型                                                    | 说明               |
| -------- | ----------------------------------------------------------- | ------------------ |
| title    | string                                                      | 文章标题           |
| id       | long                                                        | 文章id（编辑有值） |
| body     | object（{content: "ww", contentHtml: "<p>ww</p>↵"}）        | 文章内容           |
| category | {id: 2, avatar: "/category/back.png", categoryName: "后端"} | 文章类别           |
| summary  | string                                                      | 文章概述           |
| tags     | [{id: 5}, {id: 6}]                                          | 文章标签           |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": {"id":12232323}
}
~~~

# 13. 文章图片上传

## 13.1 接口说明

接口url：/upload

请求方式：POST

请求参数：

| 参数名称 | 参数类型 | 说明           |
| -------- | -------- | -------------- |
| image    | file     | 上传的文件名称 |
|          |          |                |
|          |          |                |

返回数据：

~~~json
{
    "success":true,
 	"code":200,
    "msg":"success",
    "data":"https://static.mszlu.com/aa.png"
}
~~~

~~~xml
<dependency>
  <groupId>com.qiniu</groupId>
  <artifactId>qiniu-java-sdk</artifactId>
  <version>[7.7.0, 7.7.99]</version>
</dependency>
~~~

*使用七牛云对象存储服务，好像是免费的，反正没收我钱

# 14. 导航-文章分类

## 14.1 查询所有的文章分类

### 14.1.1 接口说明

接口url：/categorys/detail

请求方式：GET

请求参数：

| 参数名称 | 参数类型 | 说明 |
| -------- | -------- | ---- |
|          |          |      |
|          |          |      |
|          |          |      |

返回数据：

~~~json
{
    "success": true, 
    "code": 200, 
    "msg": "success", 
    "data": [
        {
            "id": 1, 
            "avatar": "/static/category/front.png", 
            "categoryName": "前端", 
            "description": "前端是什么，大前端"
        }, 
        {
            "id": 2, 
            "avatar": "/static/category/back.png", 
            "categoryName": "后端", 
            "description": "后端最牛叉"
        }, 
        {
            "id": 3, 
            "avatar": "/static/category/lift.jpg", 
            "categoryName": "生活", 
            "description": "生活趣事"
        }, 
        {
            "id": 4, 
            "avatar": "/static/category/database.png", 
            "categoryName": "数据库", 
            "description": "没数据库，啥也不管用"
        }, 
        {
            "id": 5, 
            "avatar": "/static/category/language.png", 
            "categoryName": "编程语言", 
            "description": "好多语言，该学哪个？"
        }
    ]
}

~~~

~~~java
package com.mszlu.blog.vo;

import lombok.Data;

@Data
public class CategoryVo {

    private Long id;

    private String avatar;

    private String categoryName;

    private String description;
}

~~~

## 14.2 查询所有的标签

### 14.2.1 接口说明

接口url：/tags/detail

请求方式：GET

请求参数：

| 参数名称 | 参数类型 | 说明 |
| -------- | -------- | ---- |
|          |          |      |
|          |          |      |
|          |          |      |

返回数据：

~~~json
{
    "success": true, 
    "code": 200, 
    "msg": "success", 
    "data": [
        {
            "id": 5, 
            "tagName": "springboot", 
            "avatar": "/static/tag/java.png"
        }, 
        {
            "id": 6, 
            "tagName": "spring", 
            "avatar": "/static/tag/java.png"
        }, 
        {
            "id": 7, 
            "tagName": "springmvc", 
            "avatar": "/static/tag/java.png"
        }, 
        {
            "id": 8, 
            "tagName": "11", 
            "avatar": "/static/tag/css.png"
        }
    ]
}
~~~

# 15. 分类文章列表

## 15.1 接口说明

接口url：/category/detail/{id}

请求方式：GET

请求参数：

| 参数名称 | 参数类型 | 说明     |
| -------- | -------- | -------- |
| id       | 分类id   | 路径参数 |
|          |          |          |
|          |          |          |

返回数据：

~~~json
{
    "success": true, 
    "code": 200, 
    "msg": "success", 
    "data": 
        {
            "id": 1, 
            "avatar": "/static/category/front.png", 
            "categoryName": "前端", 
            "description": "前端是什么，大前端"
        }
}

~~~

# 16. 标签文章列表

## 16.1 接口说明

接口url：/tags/detail/{id}

请求方式：GET

请求参数：

| 参数名称 | 参数类型 | 说明     |
| -------- | -------- | -------- |
| id       | 标签id   | 路径参数 |
|          |          |          |
|          |          |          |

返回数据：

~~~json
{
    "success": true, 
    "code": 200, 
    "msg": "success", 
    "data": 
        {
            "id": 5, 
            "tagName": "springboot", 
            "avatar": "/static/tag/java.png"
        }
}

~~~

## 

# 17.管理员

### 17.1管理员登录

### 17.2用户管理

### 17.3文章管理

### 17.4评论管理

### 17.5分类管理

# *. 登录拦截器

​	每次访问需要登录的资源的时候，都需要在代码中进行判断，一旦登录的逻辑有所改变，代码都得进行变动，非常不合适。可以使用拦截器，进行登录拦截，如果遇到需要登录才能访问的接口，如果未登录，拦截器直接返回，并跳转登录页面。

# *. ThreadLocal保存用户信息

​	ThreadLocal存储当前线程的一些信息，其他线程无法访问

# *.AOP日志

​	面向函数编程，将日志切面插入到要记录日志的方法上，就可以生成代理类打印日志