<template>
  <div class="home-container">
    <!-- 左侧导航 -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2 class="sidebar-title">
          <svg class="title-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path>
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path>
          </svg>
          分类导航
        </h2>
      </div>
      <nav class="category-nav">
        <ul class="category-list">
          <li v-for="category in categories" :key="category.id" class="category-item">
            <button 
              @click="switchCategory(category)" 
              class="category-btn" 
              :class="{ 'active': isCurrentCategory(category) }"
            >
              <div class="category-icon">
                <img :src="category.avatar" alt="" class="category-avatar" />
              </div>
              <div class="category-info">
                <span class="category-name">{{ category.categoryName }}</span>
                <span class="category-desc">探索更多内容</span>
              </div>
              <div class="category-arrow">→</div>
            </button>
          </li>
        </ul>
      </nav>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <div class="content-header">
        <div class="breadcrumb">
          <span class="breadcrumb-item">首页</span>
          <span class="breadcrumb-separator">/</span>
          <span class="breadcrumb-item current">{{ currentCategory?.categoryName || '加载中...' }}</span>
        </div>
        <div class="content-stats">
          <span class="stat-item">
            <svg class="stat-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M3 3v18h18"></path>
              <path d="M18.7 8l-5.1 5.2-2.8-2.7L7 14.3"></path>
            </svg>
            <span class="stat-text">分类统计</span>
          </span>
        </div>
      </div>
      
      <div class="content-body">
        <div v-if="currentCategory" class="content-wrapper">
          <List :categoryId="currentCategory.id" :key="currentCategory.id"></List>
        </div>
        <div v-else class="loading-container">
          <div class="loading-spinner"></div>
          <p class="loading-text">正在加载内容...</p>
        </div>
      </div>
    </main>

    <!-- 右侧边栏 -->
    <aside class="widget-sidebar">
      <!-- 热门文章卡片 -->
      <div class="widget-card">
        <div class="widget-header">
          <h3 class="widget-title">
            <svg class="widget-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
            </svg>
            热门文章
          </h3>
          <span class="widget-badge">{{ hotArticles.length }}</span>
        </div>
        <div class="widget-content">
          <div class="hot-articles">
            <div 
              v-for="(article, index) in hotArticles" 
              :key="article.id" 
              @click="goToDetail(article.id)" 
              class="hot-article-item"
              :style="{ animationDelay: `${index * 0.1}s` }"
            >
              <div class="article-rank">{{ index + 1 }}</div>
              <div class="article-content">
                <h4 class="article-title">{{ article.title }}</h4>
                <p class="article-summary">{{ article.summary }}</p>
                <div class="article-meta">
                  <span class="meta-item">
                    <svg class="meta-icon" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                      <circle cx="12" cy="12" r="3"></circle>
                    </svg>
                    {{ article.viewCounts || 0 }}
                  </span>
                  <span class="meta-item">
                    <svg class="meta-icon" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                    </svg>
                    {{ article.commentCounts || 0 }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 快速操作卡片 -->
      <div class="widget-card">
        <div class="widget-header">
          <h3 class="widget-title">
            <svg class="widget-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"></path>
            </svg>
            快速操作
          </h3>
        </div>
        <div class="widget-content">
          <div class="quick-actions">
            <button class="action-btn primary" @click="goToWriteArticle">
              <svg class="action-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
              </svg>
              <span class="action-text">写文章</span>
            </button>
            <button class="action-btn secondary" @click="goToSearch">
              <svg class="action-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"></circle>
                <path d="M21 21l-4.35-4.35"></path>
              </svg>
              <span class="action-text">搜索</span>
            </button>
            <button class="action-btn secondary" @click="goToActivity">
              <svg class="action-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                <polyline points="22,4 12,14.01 9,11.01"></polyline>
              </svg>
              <span class="action-text">活动</span>
            </button>
          </div>
        </div>
      </div>
    </aside>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import axios from '@/axios';
import { useRouter } from 'vue-router';
import List from '@/components/articlelist/index.vue';

interface Category {
  id: number;
  avatar: string;
  categoryName: string;
}

interface Tag {
  id: number;
  tagName: string;
}

interface Article {
  id: string;
  title: string;
  summary: string | null;
  commentCounts: number | null;
  viewCounts: number | null;
  weight: number | null;
  createDate: string;
  author: string | null;
  body: string | null;
  tags: any[];
  category: any;
}

const categories = ref<Category[]>([]);
const hotTags = ref<Tag[]>([]);
const hotArticles = ref<Article[]>([]);
const currentCategoryId = ref<number>(1); // 默认选择前端分类
const router = useRouter();

// 计算当前分类
const currentCategory = computed(() => {
  return categories.value.find(cat => cat.id === currentCategoryId.value) || categories.value[0];
});

// 检查是否为当前分类
const isCurrentCategory = (category: Category) => {
  return category.id === currentCategoryId.value;
};

// 切换分类
const switchCategory = (category: Category) => {
  currentCategoryId.value = category.id;
};

onMounted(async () => {
  try {
    const categoryResponse = await axios.get('http://localhost:8888/categorys/detail');
    categories.value = categoryResponse.data.data;
    
    // 默认选择第一个分类
    if (categories.value.length > 0) {
      currentCategoryId.value = categories.value[0].id;
    }
  } catch (error) {
    console.error('Failed to fetch categories:', error);
  }
  
  try {
    const articleResponse = await axios.get('http://localhost:8888/articles/hot');
    hotArticles.value = articleResponse.data.data;
  } catch (error) {
    console.error('Failed to fetch hot articles:', error);
  }
});

const goToDetail = (id: string) => {
  router.push({ name: 'ArticleDetail', params: { id: id } });
};

const goToWriteArticle = () => {
  router.push('/write');
};

const goToSearch = () => {
  router.push('/home/search');
};

const goToActivity = () => {
  router.push('/home/activity');
};
</script>

<style scoped>
.home-container {
  display: grid;
  grid-template-columns: 280px 1fr 320px;
  gap: var(--space-6);
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 var(--space-6);
  min-height: calc(100vh - 140px);
}

/* 左侧边栏 */
.sidebar {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
  height: fit-content;
  position: sticky;
  top: 90px;
}

.sidebar-header {
  padding: var(--space-6);
  border-bottom: 1px solid var(--border-light);
  background: linear-gradient(135deg, var(--primary-light), var(--bg-primary));
}

.sidebar-title {
  margin: 0;
  font-size: 1.125rem;
  font-weight: 600;
  color: var(--text-primary);
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.title-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

.category-nav {
  padding: var(--space-4);
}

.category-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.category-item {
  width: 100%;
}

.category-btn {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  width: 100%;
  padding: var(--space-4);
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-normal);
  text-align: left;
  position: relative;
  overflow: hidden;
}

.category-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, var(--primary-light), transparent);
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.category-btn:hover::before {
  opacity: 1;
}

.category-btn:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
  border-color: var(--primary-color);
}

.category-btn.active {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-hover));
  color: white;
  border-color: var(--primary-color);
  box-shadow: var(--shadow-md);
}

.category-btn.active::before {
  opacity: 0;
}

.category-icon {
  flex-shrink: 0;
}

.category-avatar {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-md);
  object-fit: cover;
}

.category-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.category-name {
  font-weight: 600;
  font-size: 0.875rem;
}

.category-desc {
  font-size: 0.75rem;
  color: var(--text-secondary);
  opacity: 0.8;
}

.category-btn.active .category-desc {
  color: rgba(255, 255, 255, 0.8);
}

.category-arrow {
  font-size: 0.875rem;
  color: var(--text-tertiary);
  transition: transform var(--transition-fast);
}

.category-btn:hover .category-arrow {
  transform: translateX(4px);
}

.category-btn.active .category-arrow {
  color: rgba(255, 255, 255, 0.8);
}

/* 主内容区 */
.main-content {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.content-header {
  padding: var(--space-6);
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-primary);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: 0.875rem;
}

.breadcrumb-item {
  color: var(--text-secondary);
  transition: color var(--transition-fast);
}

.breadcrumb-item.current {
  color: var(--primary-color);
  font-weight: 600;
}

.breadcrumb-separator {
  color: var(--text-tertiary);
}

.content-stats {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.stat-icon {
  font-size: 1rem;
}

.content-body {
  flex: 1;
  padding: var(--space-6);
  background: var(--bg-secondary);
}

.content-wrapper {
  width: 100%;
  animation: fadeIn 0.5s ease-out;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-12);
  color: var(--text-secondary);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--border-light);
  border-top: 3px solid var(--primary-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: var(--space-4);
}

.loading-text {
  font-size: 0.875rem;
  margin: 0;
}

/* 右侧边栏 */
.widget-sidebar {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
  height: fit-content;
  position: sticky;
  top: 90px;
}

.widget-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
  transition: all var(--transition-normal);
}

.widget-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.widget-header {
  padding: var(--space-4) var(--space-6);
  border-bottom: 1px solid var(--border-light);
  background: linear-gradient(135deg, var(--bg-secondary), var(--bg-primary));
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.widget-title {
  margin: 0;
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.widget-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

.widget-badge {
  background: var(--primary-color);
  color: white;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-md);
  font-size: 0.75rem;
  font-weight: 600;
}

.widget-content {
  padding: var(--space-4);
}

/* 热门文章 */
.hot-articles {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.hot-article-item {
  display: flex;
  align-items: flex-start;
  gap: var(--space-3);
  padding: var(--space-3);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
  animation: slideInUp 0.5s ease-out forwards;
  opacity: 0;
}

.hot-article-item:hover {
  background: var(--bg-secondary);
  transform: translateX(4px);
}

.article-rank {
  flex-shrink: 0;
  width: 24px;
  height: 24px;
  background: var(--primary-color);
  color: white;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: 600;
}

.article-content {
  flex: 1;
  min-width: 0;
}

.article-title {
  margin: 0 0 var(--space-2) 0;
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-primary);
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.article-summary {
  margin: 0 0 var(--space-2) 0;
  font-size: 0.75rem;
  color: var(--text-secondary);
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.article-meta {
  display: flex;
  gap: var(--space-3);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.meta-icon {
  color: var(--text-tertiary);
  flex-shrink: 0;
}

/* 快速操作 */
.quick-actions {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.action-btn {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-4);
  border: none;
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
  font-weight: 500;
  font-size: 0.875rem;
}

.action-btn.primary {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-hover));
  color: white;
}

.action-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.action-btn.secondary {
  background: var(--bg-secondary);
  color: var(--text-primary);
  border: 1px solid var(--border-light);
}

.action-btn.secondary:hover {
  background: var(--bg-tertiary);
  border-color: var(--primary-color);
  color: var(--primary-color);
}

.action-icon {
  color: white;
  flex-shrink: 0;
}

/* 动画 */
@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .home-container {
    grid-template-columns: 250px 1fr 280px;
    gap: var(--space-4);
    padding: 0 var(--space-4);
  }
}

@media (max-width: 1024px) {
  .home-container {
    grid-template-columns: 1fr;
    gap: var(--space-4);
  }
  
  .sidebar,
  .widget-sidebar {
    position: static;
  }
  
  .widget-sidebar {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: var(--space-4);
  }
}

@media (max-width: 768px) {
  .home-container {
    padding: 0 var(--space-3);
    gap: var(--space-3);
  }
  
  .content-header {
    padding: var(--space-4);
    flex-direction: column;
    gap: var(--space-3);
    align-items: flex-start;
  }
  
  .content-body {
    padding: var(--space-4);
  }
  
  .widget-sidebar {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .home-container {
    padding: 0 var(--space-2);
  }
  
  .sidebar-header,
  .content-header {
    padding: var(--space-3);
  }
  
  .content-body {
    padding: var(--space-3);
  }
  
  .widget-content {
    padding: var(--space-3);
  }
}
</style>