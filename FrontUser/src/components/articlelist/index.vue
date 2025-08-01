<template>
  <div class="articles-container">
    <div class="articles-header">
      <h2 class="articles-title">
        <svg class="title-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
          <polyline points="14,2 14,8 20,8"></polyline>
          <line x1="16" y1="13" x2="8" y2="13"></line>
          <line x1="16" y1="17" x2="8" y2="17"></line>
          <polyline points="10,9 9,9 8,9"></polyline>
        </svg>
        文章列表
      </h2>
      <div class="articles-stats">
        <span class="stat-item">
          <svg class="stat-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
            <polyline points="14,2 14,8 20,8"></polyline>
            <line x1="16" y1="13" x2="8" y2="13"></line>
            <line x1="16" y1="17" x2="8" y2="17"></line>
            <polyline points="10,9 9,9 8,9"></polyline>
          </svg>
          <span class="stat-text">{{ articles.length }} 篇文章</span>
        </span>
      </div>
    </div>
    
    <div class="articles-grid">
      <article
        v-for="(article, index) in articles"
        :key="article.id"
        class="article-card"
        @click="goToDetail(article.id)"
        :style="{ animationDelay: `${index * 0.1}s` }"
      >
        <div class="article-header">
          <div class="article-meta">
            <span class="article-category">{{ article.category?.categoryName || '未分类' }}</span>
            <span class="article-date">{{ formatDate(article.createDate) }}</span>
          </div>
          <div class="article-priority" v-if="article.weight > 0">
            <span class="priority-badge">
              <svg class="priority-icon" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
              </svg>
              热门
            </span>
          </div>
        </div>
        
        <div class="article-content">
          <h3 class="article-title">{{ article.title }}</h3>
          <p class="article-summary">{{ article.summary || '暂无摘要' }}</p>
        </div>
        
        <div class="article-footer">
          <div class="article-author">
            <div class="author-avatar">
              <span class="avatar-text">{{ article.author?.charAt(0) || 'U' }}</span>
            </div>
            <span class="author-name">{{ article.author || '匿名用户' }}</span>
          </div>
          
          <div class="article-stats">
            <div class="stat-group">
              <svg class="stat-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                <circle cx="12" cy="12" r="3"></circle>
              </svg>
              <span class="stat-value">{{ article.viewCounts || 0 }}</span>
            </div>
            <div class="stat-group">
              <svg class="stat-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
              </svg>
              <span class="stat-value">{{ article.commentCounts || 0 }}</span>
            </div>
            <div class="stat-group">
              <svg class="stat-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
              </svg>
              <span class="stat-value">{{ article.weight || 0 }}</span>
            </div>
          </div>
        </div>
        
        <div class="article-tags" v-if="article.tags && article.tags.length > 0">
          <span 
            v-for="tag in article.tags.slice(0, 3)" 
            :key="tag.id" 
            class="tag-item"
          >
            #{{ tag.tagName }}
          </span>
          <span v-if="article.tags.length > 3" class="tag-more">
            +{{ article.tags.length - 3 }}
          </span>
        </div>
      </article>
    </div>
    
    <div v-if="articles.length === 0" class="empty-state">
      <svg class="empty-icon" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
        <polyline points="14,2 14,8 20,8"></polyline>
        <line x1="16" y1="13" x2="8" y2="13"></line>
        <line x1="16" y1="17" x2="8" y2="17"></line>
        <polyline points="10,9 9,9 8,9"></polyline>
      </svg>
      <h3 class="empty-title">暂无文章</h3>
      <p class="empty-description">这个分类下还没有文章，快来写第一篇吧！</p>
      <button class="empty-action" @click="goToWriteArticle">
        <svg class="action-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
          <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
        </svg>
        <span>写文章</span>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import axios from '@/axios';
import { useRouter } from 'vue-router';

interface Article {
  id: number;
  title: string;
  summary: string;
  author: string;
  createDate: string;
  weight: number; 
  viewCounts: number; 
  commentCounts: number;
  category?: {
    categoryName: string;
  };
  tags?: Array<{
    id: number;
    tagName: string;
  }>;
}

const props = defineProps({
  categoryId: {
    type: Number,
    required: true
  },
  page: {
    type: Number,
    default: 1
  },
  pageSize: {
    type: Number,
    default: 15
  }
});

const articles = ref<Article[]>([]);
const router = useRouter();

const goToDetail = (id: number) => {
  router.push({ name: 'ArticleDetail', params: { id: id.toString() } });
};

const goToWriteArticle = () => {
  router.push('/write');
};

const fetchArticles = async () => {
  try {
    const response = await axios.post('http://localhost:8888/articles', {
      page: props.page,
      pageSize: props.pageSize,
      categoryId: props.categoryId,
      tagId: null
    });
    articles.value = response.data.data.sort((a:any, b:any) => b.weight - a.weight);
  } catch (error) {
    console.error('Failed to fetch articles:', error);
  }
};

const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  const now = new Date();
  const diffTime = Math.abs(now.getTime() - date.getTime());
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  
  if (diffDays === 1) {
    return '今天';
  } else if (diffDays === 2) {
    return '昨天';
  } else if (diffDays <= 7) {
    return `${diffDays - 1}天前`;
  } else {
    return date.toLocaleDateString('zh-CN');
  }
};

onMounted(() => {
  fetchArticles();
});

// 监听categoryId变化，重新获取文章
watch(() => props.categoryId, (newCategoryId) => {
  if (newCategoryId) {
    fetchArticles();
  }
});
</script>

<style scoped>
.articles-container {
  width: 100%;
  animation: fadeIn 0.5s ease-out;
}

.articles-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-6);
  padding-bottom: var(--space-4);
  border-bottom: 1px solid var(--border-light);
}

.articles-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.title-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

.articles-stats {
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
  color: var(--text-tertiary);
  flex-shrink: 0;
}

.articles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: var(--space-6);
}

.article-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
  padding: var(--space-6);
  cursor: pointer;
  transition: all var(--transition-normal);
  border: 1px solid var(--border-light);
  position: relative;
  overflow: hidden;
  animation: slideInUp 0.5s ease-out forwards;
  opacity: 0;
}

.article-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(135deg, var(--primary-color), var(--primary-hover));
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.article-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--primary-color);
}

.article-card:hover::before {
  opacity: 1;
}

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-4);
}

.article-meta {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.article-category {
  font-size: 0.75rem;
  color: var(--primary-color);
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.article-date {
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.article-priority {
  flex-shrink: 0;
}

.priority-badge {
  background: var(--warning-color);
  color: white;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-sm);
  font-size: 0.75rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: var(--space-1);
}

.priority-icon {
  color: white;
  flex-shrink: 0;
}

.article-content {
  margin-bottom: var(--space-4);
}

.article-title {
  margin: 0 0 var(--space-3) 0;
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.article-summary {
  margin: 0;
  font-size: 0.875rem;
  color: var(--text-secondary);
  line-height: 1.6;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-4);
  padding-top: var(--space-4);
  border-top: 1px solid var(--border-light);
}

.article-author {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.author-avatar {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, var(--primary-color), var(--primary-hover));
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-text {
  color: white;
  font-weight: 600;
  font-size: 0.875rem;
}

.author-name {
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--text-primary);
}

.article-stats {
  display: flex;
  gap: var(--space-4);
}

.stat-group {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.stat-icon {
  color: var(--text-tertiary);
  flex-shrink: 0;
}

.stat-value {
  font-weight: 500;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
  padding-top: var(--space-3);
  border-top: 1px solid var(--border-light);
}

.tag-item {
  background: var(--bg-secondary);
  color: var(--text-secondary);
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-sm);
  font-size: 0.75rem;
  font-weight: 500;
  transition: all var(--transition-fast);
}

.tag-item:hover {
  background: var(--primary-light);
  color: var(--primary-color);
}

.tag-more {
  background: var(--bg-tertiary);
  color: var(--text-tertiary);
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-sm);
  font-size: 0.75rem;
  font-weight: 500;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-12);
  text-align: center;
  color: var(--text-secondary);
}

.empty-icon {
  color: var(--text-tertiary);
  margin-bottom: var(--space-4);
  opacity: 0.5;
}

.empty-title {
  margin: 0 0 var(--space-2) 0;
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary);
}

.empty-description {
  margin: 0 0 var(--space-6) 0;
  font-size: 0.875rem;
  line-height: 1.6;
  max-width: 400px;
}

.empty-action {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-6);
  background: linear-gradient(135deg, var(--primary-color), var(--primary-hover));
  color: white;
  border: none;
  border-radius: var(--radius-lg);
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-sm);
}

.empty-action:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
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

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .articles-header {
    flex-direction: column;
    gap: var(--space-3);
    align-items: flex-start;
  }
  
  .articles-grid {
    grid-template-columns: 1fr;
    gap: var(--space-4);
  }
  
  .article-card {
    padding: var(--space-4);
  }
  
  .article-footer {
    flex-direction: column;
    gap: var(--space-3);
    align-items: flex-start;
  }
  
  .article-stats {
    gap: var(--space-3);
  }
}

@media (max-width: 480px) {
  .articles-title {
    font-size: 1.25rem;
  }
  
  .article-title {
    font-size: 1.125rem;
  }
  
  .empty-state {
    padding: var(--space-8);
  }
  
  .empty-icon {
    font-size: 3rem;
  }
}
</style>