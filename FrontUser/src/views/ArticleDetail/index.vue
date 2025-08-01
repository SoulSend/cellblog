<template>
  <div class="article-detail-container">
    <div v-if="article" class="article-detail">
      <!-- 主要内容区域 -->
      <div class="main-content">
        <!-- 文章内容区域 -->
        <div class="article-content-wrapper">
          <header class="article-header">
            <h1 class="article-title">{{ article.title }}</h1>
            <div class="article-meta">
              <span class="article-author">{{ article.author }}</span>
              <time class="article-date" :datetime="article.createDate">{{ formatDate(article.createDate) }}</time>
              <div class="article-stats">
                <span class="view-counts">{{ article.viewCounts }} views</span>
                <span class="comment-counts">{{ article.commentCounts }} comments</span>
              </div>
            </div>
          </header>
          <section class="article-body">
            <p class="article-summary">{{ article.summary }}</p>
            <!-- 根据 contentHtml 是否为 null 来决定渲染哪个内容 -->
            <div v-html="article.body.contentHtml || article.body.content" class="article-content"></div>
          </section>
        </div>
        
        <!-- 侧边栏 -->
        <aside class="article-sidebar">
          <!-- 文章信息卡片 -->
          <div class="sidebar-card">
            <h3 class="card-title">文章信息</h3>
            <div class="info-item">
              <span class="info-label">分类</span>
              <div class="info-value">
                <img :src="article.category.avatar" :alt="article.category.categoryName" class="category-icon" />
                <span>{{ article.category.categoryName }}</span>
              </div>
            </div>
            <div class="info-item">
              <span class="info-label">发布时间</span>
              <span class="info-value">{{ formatDate(article.createDate) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">浏览量</span>
              <span class="info-value">{{ article.viewCounts }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">评论数</span>
              <span class="info-value">{{ article.commentCounts }}</span>
            </div>
          </div>
          
          <!-- 文章标签 -->
          <div class="sidebar-card">
            <h3 class="card-title">文章标签</h3>
            <div class="tags-container">
              <span v-for="tag in article.tags" :key="tag.id" class="tag-item">
                <img :src="tag.avatar" :alt="tag.tagName" class="tag-icon" />
                <span>{{ tag.tagName }}</span>
              </span>
            </div>
          </div>
          
          <!-- 评论区切换按钮 -->
          <div class="sidebar-card">
            <div class="comments-toggle">
              <button class="toggle-btn" @click="toggleComments" :class="{ 'active': showComments }">
                <span class="toggle-icon">{{ showComments ? '−' : '+' }}</span>
                评论 ({{ article.commentCounts }})
              </button>
            </div>
          </div>
        </aside>
      </div>
      
      <!-- 评论区 (可展开收起) -->
      <div v-if="showComments" class="comments-section">
        <div class="comments-header">
          <h3 class="comments-title">评论 ({{ article.commentCounts }})</h3>
          <button class="close-comments-btn" @click="toggleComments">×</button>
        </div>
        <Comments :article-id="article.id" :article-author="article.author" />
      </div>
    </div>
    <div v-else>
      <p>文章详情加载中...</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from '@/axios';
import { useRoute } from 'vue-router';
import Comments from "@/components/comments/index.vue"

interface Article {
  id: string;
  title: string;
  summary: string;
  author: string;
  createDate: string;
  viewCounts: number;
  commentCounts: number;
  body: {
    content: string;
    contentHtml?: string;
  };
  tags: Array<{
    id: number;
    avatar: string;
    tagName: string;
  }>;
  category: {
    id: number;
    avatar: string;
    categoryName: string;
  };
}

const route = useRoute();
const article = ref<Article | null>(null);
const showComments = ref(true);

onMounted(async () => {
  try {
    const response = await axios.post(`http://localhost:8888/articles/view/${route.params.id}`);
    article.value = response.data.data;
  } catch (error) {
    console.error('Failed to fetch article detail:', error);
  }
});

const toggleComments = () => {
  showComments.value = !showComments.value;
};

const formatDate = (date: string) => {
  const dateObj = new Date(date);
  return dateObj.toLocaleDateString();
};
</script>

<style scoped>
.article-detail-container {
  max-width: 1400px;
  margin: 40px auto;
  padding: 40px;
}

.article-detail {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

.main-content {
  display: flex;
  gap: 40px;
  align-items: flex-start;
}

.article-content-wrapper {
  flex: 1;
  background: white;
  border-radius: 12px;
  padding: 40px;
  border: 1px solid #e1e5e9;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.article-header {
  margin-bottom: 40px;
}

.article-title {
  font-size: 2.5rem;
  margin: 0 0 20px 0;
  color: #333;
  line-height: 1.2;
}

.article-meta {
  font-size: 1rem;
  color: #666;
  margin-bottom: 20px;
}

.article-author::after {
  content: " •";
  display: inline-block;
  margin: 0 0.25rem;
}

.article-date {
  font-style: italic;
}

.article-stats {
  margin-top: 10px;
  font-size: 1rem;
  color: #666;
  display: flex;
  gap: 24px;
}

.view-counts, .comment-counts {
  margin-right: 1rem;
}

.article-body {
  font-size: 1.1rem;
  color: #444;
  line-height: 1.7;
}

.article-summary {
  margin-bottom: 30px;
  font-size: 1.2rem;
  color: #666;
  font-style: italic;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border-left: 4px solid #06bac7;
}

.article-content {
  margin-bottom: 40px;
}

/* 侧边栏样式 */
.article-sidebar {
  width: 320px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.sidebar-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e1e5e9;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px 0;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 14px;
  color: #666;
}

.info-value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
}

.category-icon, .tag-icon {
  width: 20px;
  height: 20px;
  border-radius: 50%;
}

.tags-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: #f8f9fa;
  border: 1px solid #e1e5e9;
  border-radius: 6px;
  font-size: 14px;
  color: #666;
}

.comments-toggle {
  text-align: center;
}

.toggle-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  padding: 12px 16px;
  background: #06bac7;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.toggle-btn:hover {
  background: #05a3b0;
}

.toggle-btn.active {
  background: #05a3b0;
}

.toggle-icon {
  font-size: 16px;
  font-weight: bold;
}

/* 评论区样式 */
.comments-section {
  background: white;
  border-radius: 12px;
  border: 1px solid #e1e5e9;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.comments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 32px;
  border-bottom: 1px solid #e1e5e9;
  background: #f8f9fa;
}

.comments-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.close-comments-btn {
  background: none;
  border: none;
  font-size: 20px;
  color: #666;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.close-comments-btn:hover {
  background: #e9ecef;
  color: #333;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
  }
  
  .article-sidebar {
    width: 100%;
    order: -1;
  }
  
  .article-sidebar {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
  }
}

@media (max-width: 768px) {
  .article-detail-container {
    padding: 20px;
    margin: 20px auto;
  }

  .article-content-wrapper {
    padding: 24px;
  }

  .article-title {
    font-size: 2rem;
  }

  .article-body {
    font-size: 1rem;
  }

  .article-summary {
    font-size: 1.1rem;
    padding: 16px;
  }

  .comments-header {
    padding: 20px 24px;
  }
  
  .article-sidebar {
    grid-template-columns: 1fr;
  }
}


</style>