# CELL Blog

一个现代化的博客平台，基于 Vue 3 + TypeScript 构建。

## ✨ 特性

- 🎨 **现代化设计** - 简洁优雅的用户界面
- 📱 **响应式布局** - 完美适配各种设备
- ✍️ **富文本编辑** - 支持 Markdown 语法
- 🔍 **智能搜索** - 全文搜索和标签筛选
- 💬 **评论系统** - 完整的评论和回复功能
- 🎯 **活动管理** - 活动发布和报名系统
- 🔐 **用户认证** - 安全的用户注册登录
- ⚡ **高性能** - 优化的构建和加载速度

## 🚀 快速开始

### 环境要求

- Node.js 16.0.0 或更高版本
- npm 或 yarn 包管理器

### 安装依赖

```bash
npm install
```

### 开发模式

```bash
npm run dev
```

访问 http://localhost:9999 查看应用

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## 📁 项目结构

```
src/
├── assets/              # 静态资源
├── components/          # 组件
│   ├── articlelist/     # 文章列表
│   ├── comments/        # 评论系统
│   ├── common/          # 通用组件
│   ├── layout/          # 布局组件
│   ├── login-modal/     # 登录模态框
│   ├── SearchResultsList/ # 搜索结果
│   └── write/           # 富文本编辑器
├── router/              # 路由配置
├── stores/              # 状态管理
├── utils/               # 工具函数
├── views/               # 页面视图
├── App.vue              # 根组件
├── axios.ts             # HTTP配置
└── main.ts              # 入口文件
```

## 🎯 核心功能

### 用户系统
- 用户注册和登录
- 个人中心管理
- 用户信息编辑

### 内容管理
- 文章发布和编辑
- 富文本编辑器（支持 Markdown）
- 文章分类和标签
- 文章搜索和筛选

### 交互功能
- 评论系统
- 评论回复
- 评论举报
- 文章点赞和收藏

### 活动系统
- 活动发布和展示
- 活动详情页面
- 活动报名功能

### 搜索功能
- 全文搜索
- 热门标签
- 搜索结果展示
- 搜索建议

## 🛠️ 技术栈

- **前端框架**: Vue 3
- **开发语言**: TypeScript
- **状态管理**: Pinia
- **路由管理**: Vue Router 4
- **UI组件**: Element Plus
- **HTTP客户端**: Axios
- **富文本编辑器**: WangEditor
- **构建工具**: Vite
- **包管理器**: npm

## 📦 主要依赖

```json
{
  "vue": "^3.4.29",
  "vue-router": "^4.3.3",
  "pinia": "^2.1.7",
  "element-plus": "^2.8.4",
  "axios": "^1.7.7",
  "@wangeditor/editor": "^5.1.23",
  "marked": "^14.1.4",
  "highlight.js": "^11.11.1"
}
```

## 🔧 开发指南

### 代码规范

项目使用 TypeScript 进行类型检查：

```bash
npm run type-check
```

### 构建检查

```bash
npm run build
```

### 环境变量

创建 `.env.local` 文件配置本地环境：

```env
VITE_API_BASE_URL=http://localhost:8888
VITE_APP_TITLE=CELL Blog
```

## 🎨 设计系统

### 色彩主题

- **主色调**: #1fc2b5
- **辅助色**: #f8f9fa
- **文字色**: #333333
- **边框色**: #e1e5e9

### 组件规范

- 统一的圆角设计
- 一致的间距系统
- 响应式布局
- 无障碍访问支持

## 📱 响应式设计

项目支持多种设备尺寸：

- **桌面端**: 1200px+
- **平板端**: 768px - 1200px
- **移动端**: < 768px

## 🔒 安全特性

- HTTPS 支持
- XSS 防护
- CSRF 防护
- 输入验证
- 错误处理

## 🚀 性能优化

- 代码分割
- 懒加载
- 图片优化
- 缓存策略
- Gzip 压缩

## 📊 项目统计

- **总文件数**: 1737个模块
- **构建时间**: 9.74秒
- **主包大小**: 1.8MB (gzip: 610KB)
- **TypeScript错误**: 0个

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

- 项目主页: [GitHub Repository]
- 问题反馈: [Issues]
- 功能建议: [Discussions]

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者和用户！

---

*最后更新: 2024年12月* 