<template>
  <div class="search-results">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <p class="loading-text">正在搜索...</p>
    </div>
    
    <!-- 错误状态 -->
    <div v-else-if="error" class="error-state">
      <svg class="error-icon" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <circle cx="12" cy="12" r="10"></circle>
        <line x1="15" y1="9" x2="9" y2="15"></line>
        <line x1="9" y1="9" x2="15" y2="15"></line>
      </svg>
      <h3 class="error-title">搜索失败</h3>
      <p class="error-message">{{ error }}</p>
    </div>
    
    <!-- 空状态 -->
    <div v-else-if="!articles || articles.length === 0" class="empty-state">
      <svg class="empty-icon" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <circle cx="11" cy="11" r="8"></circle>
        <path d="M21 21l-4.35-4.35"></path>
      </svg>
      <h3 class="empty-title">未找到相关文章</h3>
      <p class="empty-message">尝试使用不同的关键词重新搜索</p>
    </div>
    
    <!-- 搜索结果 -->
    <div v-else class="results-content">
      <div class="results-header">
        <h2 class="results-title">
          <svg class="results-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"></circle>
            <path d="M21 21l-4.35-4.35"></path>
          </svg>
          搜索结果
        </h2>
        <p class="results-subtitle">找到 {{ allArticles.length }} 篇相关文章</p>
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
            <div class="article-priority">
              <span class="priority-badge">
                <svg class="priority-icon" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
                </svg>
                搜索结果
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
                <span class="avatar-text">{{ (article.author || 'U').charAt(0) }}</span>
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
            </div>
          </div>
        </article>
      </div>
      
      <!-- 分页 -->
      <div v-if="totalPage > 1" class="pagination">
        <button 
          @click="changePage(page - 1)" 
          :disabled="page <= 1"
          class="pagination-btn prev"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="15,18 9,12 15,6"></polyline>
          </svg>
          上一页
        </button>
        
        <div class="page-info">
          <span class="current-page">{{ page }}</span>
          <span class="page-separator">/</span>
          <span class="total-pages">{{ totalPage }}</span>
        </div>
        
        <button 
          @click="changePage(page + 1)" 
          :disabled="page >= totalPage"
          class="pagination-btn next"
        >
          下一页
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9,18 15,12 9,6"></polyline>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits, watch, onMounted } from 'vue';
import axios from '@/axios';
import { useRouter } from 'vue-router';

interface Category {
  categoryName: string; // 分类的名称
}

interface Article {
  id: number;
  title: string;
  summary: string;
  author: string;
  createDate: string;
  category: Category;
  viewCounts: number;  
  commentCounts: number;  
}

const props = defineProps<{
  searchQuery: string;
  page: number;
}>();

const emits = defineEmits(['update:page']);

const articles = ref<Article[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);
const page = ref(props.page);
const allArticles = ref<Article[]>([]);
const totalPage = ref(1);
const pageSize = 9; // 每页显示的文章数量

const router = useRouter(); // 使用 vue-router

// 监听 searchQuery 和 page 的变化
watch(() => props.searchQuery, async (newQuery) => {
  if (newQuery !== '') {
    page.value = 1; // 重置页码为1
    await fetchArticles();
  }
}, { immediate: true });

watch(() => props.page, async (newPage) => {
  page.value = newPage;
  calculateArticlesForCurrentPage();
});

const fetchArticles = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await axios.get(`http://localhost:8888/search?title=${props.searchQuery}`);
    allArticles.value = response.data.data; // 存储所有文章数据
    calculateArticlesForCurrentPage();
  } catch (error: any) {
    console.error('Failed to fetch articles:', error);
    error.value = error.message;
  } finally {
    loading.value = false;
  }
};

const calculateArticlesForCurrentPage = () => {
  const startIndex = (page.value - 1) * pageSize;
  const endIndex = Math.min(startIndex + pageSize, allArticles.value.length);
  articles.value = allArticles.value.slice(startIndex, endIndex);
  totalPage.value = Math.ceil(allArticles.value.length / pageSize);
};

const changePage = (newPage: number) => {
  if (newPage >= 1 && newPage <= totalPage.value) {
    page.value = newPage;
    emits('update:page', newPage);
  }
};

const goToDetail = (id: number) => {
  router.push({ name: 'ArticleDetail', params: { id: id.toString() } });
};

const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

onMounted(() => {
  fetchArticles();
});
</script>

<style scoped>
.search-results {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--space-6);
}

/* 加载状态 */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-8);
  text-align: center;
}

.loading-spinner {
  border: 4px solid var(--border-light);
  border-top: 4px solid var(--primary-color);
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
  margin-bottom: var(--space-4);
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  color: var(--text-secondary);
  font-size: 1rem;
}

/* 错误状态 */
.error-state, .empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-8);
  text-align: center;
}

.error-icon, .empty-icon {
  color: var(--text-tertiary);
  margin-bottom: var(--space-4);
}

.error-title, .empty-title {
  color: var(--text-primary);
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: var(--space-2);
}

.error-message, .empty-message {
  color: var(--text-secondary);
  font-size: 1rem;
}

/* 搜索结果内容 */
.results-content {
  animation: fadeIn 0.5s ease-out;
}

.results-header {
  text-align: center;
  margin-bottom: var(--space-6);
  padding-bottom: var(--space-4);
  border-bottom: 1px solid var(--border-light);
}

.results-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 var(--space-2) 0;
}

.results-subtitle {
  color: var(--text-secondary);
  font-size: 1rem;
  margin: 0;
}

.results-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

/* 文章网格 */
.articles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: var(--space-6);
  margin-bottom: var(--space-8);
}

/* 文章卡片 */
.article-card {
  background: linear-gradient(135deg, var(--bg-primary), var(--bg-secondary));
  border: 2px solid var(--border-color);
  border-radius: var(--radius-xl);
  padding: var(--space-5);
  cursor: pointer;
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-sm);
  position: relative;
  overflow: hidden;
  animation: fadeInUp 0.6s ease-out forwards;
  opacity: 0;
  transform: translateY(20px);
}

.article-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, var(--primary-light), transparent);
  opacity: 0;
  transition: opacity var(--transition-fast);
  pointer-events: none;
}

.article-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-lg);
  border-color: var(--primary-color);
}

.article-card:hover::before {
  opacity: 0.05;
}

@keyframes fadeInUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 文章头部 */
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
  background: linear-gradient(135deg, var(--primary-light), var(--bg-tertiary));
  color: var(--primary-color);
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-md);
  font-size: 0.75rem;
  font-weight: 600;
  border: 1px solid var(--primary-light);
  display: inline-block;
  width: fit-content;
}

.article-date {
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.article-priority {
  flex-shrink: 0;
}

.priority-badge {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-hover));
  color: white;
  padding: var(--space-1) var(--space-2);
  border-radius: var(--radius-md);
  font-size: 0.75rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: var(--space-1);
  box-shadow: var(--shadow-sm);
}

.priority-icon {
  flex-shrink: 0;
}

/* 文章内容 */
.article-content {
  margin-bottom: var(--space-4);
}

.article-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 var(--space-3) 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-summary {
  font-size: 0.875rem;
  color: var(--text-secondary);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin: 0;
}

/* 文章底部 */
.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: var(--space-3);
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
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-color), var(--primary-hover));
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.avatar-text {
  color: white;
  font-size: 0.875rem;
  font-weight: 600;
}

.author-name {
  font-size: 0.875rem;
  color: var(--text-primary);
  font-weight: 500;
}

.article-stats {
  display: flex;
  gap: var(--space-3);
}

.stat-group {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.stat-icon {
  flex-shrink: 0;
}

.stat-value {
  font-weight: 600;
}





/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--space-4);
  margin-top: var(--space-6);
  padding-top: var(--space-4);
  border-top: 1px solid var(--border-light);
}

.pagination-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  background: linear-gradient(135deg, var(--primary-color), var(--primary-hover));
  color: white;
  border: none;
  padding: var(--space-3) var(--space-4);
  border-radius: var(--radius-lg);
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-sm);
}

.pagination-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.pagination-btn:disabled {
  background: var(--text-tertiary);
  cursor: not-allowed;
  transform: none;
}

.pagination-btn svg {
  width: 16px;
  height: 16px;
}

.page-info {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
}

.current-page {
  color: var(--primary-color);
}

.page-separator {
  color: var(--text-tertiary);
}

.total-pages {
  color: var(--text-secondary);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-results {
    padding: var(--space-4);
  }
  
  .results-header {
    margin-bottom: var(--space-4);
  }
  
  .results-title {
    font-size: 1.25rem;
  }
  
  .articles-grid {
    grid-template-columns: 1fr;
    gap: var(--space-4);
  }
  
  .article-card {
    padding: var(--space-4);
  }
  
  .article-title {
    font-size: 1.125rem;
  }
  
  .article-summary {
    font-size: 0.875rem;
  }
  
  .article-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--space-2);
  }
  
  .article-author {
    width: 100%;
  }
  
  .article-stats {
    width: 100%;
    justify-content: flex-start;
  }
  
  .pagination {
    flex-direction: column;
    gap: var(--space-3);
  }
  
  .pagination-btn {
    width: 100%;
    justify-content: center;
  }
}

@media (max-width: 576px) {
  .search-results {
    padding: var(--space-3);
  }
  
  .article-card {
    padding: var(--space-3);
  }
  
  .article-title {
    font-size: 1rem;
  }
  
  .article-summary {
    font-size: 0.75rem;
  }
  
  .author-avatar {
    width: 28px;
    height: 28px;
  }
  
  .author-name {
    font-size: 0.75rem;
  }
  
  .stat-group {
    font-size: 0.625rem;
  }
}

@media (max-width: 480px) {
  .search-results {
    padding: var(--space-3);
  }
  
  .results-title {
    font-size: 1.25rem;
  }
  
  .card-content {
    padding: var(--space-3);
  }
  
  .card-title {
    font-size: 1rem;
  }
  
  .card-summary {
    font-size: 0.8rem;
  }
}
</style>