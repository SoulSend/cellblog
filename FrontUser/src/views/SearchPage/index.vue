<template>
  <div class="search-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">发现内容</h1>
        <p class="page-subtitle">搜索文章、用户和标签，发现更多精彩内容</p>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-container">
        <div class="search-input-wrapper" :class="{ 'focused': isSearchFocused }">
          <svg class="search-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"></circle>
            <path d="M21 21l-4.35-4.35"></path>
          </svg>
          <input 
            ref="searchInput"
            type="text" 
            placeholder="搜索文章、用户或标签..." 
            v-model="searchQuery" 
            @keyup.enter="handleSearch"
            @focus="isSearchFocused = true"
            @blur="handleSearchBlur"
            @input="handleSearchInput"
            class="search-input"
          />
          <button @click="handleSearch" class="search-btn" :disabled="!searchQuery.trim()">
            <svg class="btn-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="5" y1="12" x2="19" y2="12"></line>
              <polyline points="12,5 19,12 12,19"></polyline>
            </svg>
            搜索
          </button>
        </div>
        
        <!-- 搜索建议 -->
        <div v-if="showSuggestions && searchQuery && !hasSearched" class="search-suggestions">
          <div class="suggestion-header">
            <span class="suggestion-title">搜索建议</span>
            <button class="clear-suggestions" @click="clearSuggestions">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"></line>
                <line x1="6" y1="6" x2="18" y2="18"></line>
              </svg>
            </button>
          </div>
          <div class="suggestion-list">
            <div 
              v-for="suggestion in filteredSuggestions" 
              :key="suggestion"
              @click="selectSuggestion(suggestion)"
              class="suggestion-item"
            >
              <svg class="suggestion-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"></circle>
                <path d="M21 21l-4.35-4.35"></path>
              </svg>
              <span class="suggestion-text">{{ suggestion }}</span>
            </div>
            <div v-if="filteredSuggestions.length === 0" class="no-suggestions">
              <svg class="no-suggestions-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"></circle>
                <path d="M21 21l-4.35-4.35"></path>
              </svg>
              <span>没有找到相关建议</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 搜索结果 -->
    <div v-if="hasSearched" class="search-results">
      <div class="results-header">
        <h2 class="results-title">
          <svg class="results-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8"></circle>
            <path d="M21 21l-4.35-4.35"></path>
          </svg>
          搜索结果
        </h2>
        <div class="results-stats">
          <span class="stat-text">找到相关结果</span>
          <button class="clear-search" @click="clearSearch">
            <svg class="clear-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
            清除搜索
          </button>
        </div>
      </div>
      
      <div class="search-tabs">
        <button 
          v-for="tab in searchTabs" 
          :key="tab.key"
          @click="activeTab = tab.key"
          class="search-tab"
          :class="{ 'active': activeTab === tab.key }"
        >
          <svg class="tab-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path v-if="tab.key === 'all'" d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
            <polyline v-if="tab.key === 'all'" points="14,2 14,8 20,8"></polyline>
            <line v-if="tab.key === 'all'" x1="16" y1="13" x2="8" y2="13"></line>
            <line v-if="tab.key === 'all'" x1="16" y1="17" x2="8" y2="17"></line>
            <polyline v-if="tab.key === 'all'" points="10,9 9,9 8,9"></polyline>
            <path v-if="tab.key === 'articles'" d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
            <polyline v-if="tab.key === 'articles'" points="14,2 14,8 20,8"></polyline>
            <line v-if="tab.key === 'articles'" x1="16" y1="13" x2="8" y2="13"></line>
            <line v-if="tab.key === 'articles'" x1="16" y1="17" x2="8" y2="17"></line>
            <polyline v-if="tab.key === 'articles'" points="10,9 9,9 8,9"></polyline>
            <path v-if="tab.key === 'users'" d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
            <circle v-if="tab.key === 'users'" cx="12" cy="7" r="4"></circle>
            <path v-if="tab.key === 'tags'" d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 14V2h12l5.59 5.59a2 2 0 0 1 0 2.82z"></path>
            <line v-if="tab.key === 'tags'" x1="7" y1="7" x2="7.01" y2="7"></line>
          </svg>
          {{ tab.label }}
        </button>
      </div>
      
      <SearchResultsList 
        :searchQuery="searchQuery" 
        :page="page" 
        :activeTab="activeTab"
        @update:page="handlePageChange" 
      />
    </div>

    <!-- 发现内容 -->
    <div v-else class="discover-content">
      <!-- 热门标签 -->
      <div class="trending-section">
        <div class="trending-header">
          <h2 class="trending-title">
            <svg class="trending-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 14V2h12l5.59 5.59a2 2 0 0 1 0 2.82z"></path>
              <line x1="7" y1="7" x2="7.01" y2="7"></line>
            </svg>
            热门标签
          </h2>
          <p class="trending-subtitle">大家都在关注什么</p>
        </div>
        
        <div class="trending-tags">
          <button 
            v-for="tag in trendingTags" 
            :key="tag"
            @click="selectSuggestion(tag)"
            class="trending-tag"
          >
            <svg class="tag-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 14V2h12l5.59 5.59a2 2 0 0 1 0 2.82z"></path>
              <line x1="7" y1="7" x2="7.01" y2="7"></line>
            </svg>
            {{ typeof tag === 'string' ? tag : (tag as any).tagName || (tag as any).name || tag }}
          </button>
        </div>
      </div>

      <!-- 最新文章 -->
      <div class="recommended-section">
        <div class="section-header">
          <h2 class="section-title">
            <svg class="section-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
            </svg>
            最新文章
          </h2>
          <p class="section-subtitle">发现最新发布的内容</p>
        </div>
        
        <div class="articles-grid">
          <article
            v-for="(article, index) in recommendedArticles"
            :key="article.id"
            class="article-card"
            @click="goToArticle(article.id)"
            :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <div class="article-header">
              <div class="article-meta">
                <span class="article-category">最新文章</span>
                <span class="article-date">{{ formatDate(article.createDate || new Date()) }}</span>
              </div>
              <div class="article-priority">
                <span class="priority-badge">
                  <svg class="priority-icon" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
                  </svg>
                  最新
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
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import SearchResultsList from '@/components/SearchResultsList/index.vue';
import { useRouter } from 'vue-router';
import axios from '@/axios';

const searchQuery = ref('');
const page = ref(1);
const hasSearched = ref(false);
const isSearchFocused = ref(false);
const activeTab = ref('all');
const router = useRouter();
const searchInput = ref<HTMLInputElement | null>(null);

// 搜索标签页
const searchTabs = [
  { key: 'all', label: '全部' },
  { key: 'articles', label: '文章' },
  { key: 'users', label: '用户' },
  { key: 'tags', label: '标签' }
];

// 搜索建议
const searchSuggestions = computed(() => {
  if (!searchQuery.value) return [];
  const suggestions = [
    `${searchQuery.value} 教程`,
    `${searchQuery.value} 开发`,
    `${searchQuery.value} 技术`,
    `${searchQuery.value} 前端`,
    `${searchQuery.value} 后端`
  ];
  return suggestions.slice(0, 5);
});

// 热门标签
const trendingTags = ref<string[]>([]);

// 获取热门标签
const fetchHotTags = async () => {
  try {
    const response = await axios.get('http://localhost:8888/tags/hot');
    trendingTags.value = response.data.data || [];
  } catch (error) {
    console.error('Failed to fetch hot tags:', error);
    // 如果获取失败，使用默认数据
    trendingTags.value = [
      'Vue.js', 'React', 'TypeScript', 'JavaScript', 
      'Python', 'Java', 'Go', 'Docker', 'Kubernetes',
      '机器学习', '人工智能', '区块链', '云计算'
    ];
  }
};

// 最新文章
interface Article {
  id: number;
  title: string;
  summary: string;
  author?: string;
  createDate?: string;
  viewCounts: number;
  commentCounts: number;
}

const recommendedArticles = ref<Article[]>([]);

// 获取最新文章
const fetchLatestArticles = async () => {
  try {
    const response = await axios.get('http://localhost:8888/articles/new');
    recommendedArticles.value = response.data.data || [];
  } catch (error) {
    console.error('Failed to fetch latest articles:', error);
    // 如果获取失败，使用默认数据
    recommendedArticles.value = [
      {
        id: 1,
        title: 'Vue.js 3.0 新特性详解',
        summary: '深入解析Vue.js 3.0的Composition API、响应式系统等核心新特性，帮助你快速上手最新版本。',
        viewCounts: 1234,
        commentCounts: 56
      },
      {
        id: 2,
        title: 'TypeScript 高级类型技巧',
        summary: '掌握TypeScript的高级类型系统，包括条件类型、映射类型、模板字面量类型等实用技巧。',
        viewCounts: 987,
        commentCounts: 43
      },
      {
        id: 3,
        title: '现代前端工程化实践',
        summary: '从构建工具到部署流程，全面介绍现代前端工程化的最佳实践和工具链配置。',
        viewCounts: 756,
        commentCounts: 32
      },
      {
        id: 4,
        title: 'React Hooks 深度解析',
        summary: '深入理解React Hooks的工作原理，掌握自定义Hook的设计模式和最佳实践。',
        viewCounts: 654,
        commentCounts: 28
      }
    ];
  }
};

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    hasSearched.value = true;
    page.value = 1;
    activeTab.value = 'all';
  }
};

const handlePageChange = (newPage: number) => {
  page.value = newPage;
};

const selectSuggestion = (suggestion: string) => {
  searchQuery.value = suggestion;
  handleSearch();
};

const clearSuggestions = () => {
  searchQuery.value = '';
  hasSearched.value = false;
  isSearchFocused.value = false;
};

const clearSearch = () => {
  searchQuery.value = '';
  hasSearched.value = false;
  activeTab.value = 'all';
  page.value = 1;
  if (searchInput.value) {
    searchInput.value.focus();
  }
};

const handleSearchBlur = () => {
  setTimeout(() => {
    if (!searchQuery.value) {
      isSearchFocused.value = false;
    }
  }, 100);
};

const handleSearchInput = () => {
  hasSearched.value = false; // Clear search results when typing
};

const goToArticle = (id: number) => {
  router.push({ name: 'ArticleDetail', params: { id: id.toString() } });
};

const formatDate = (dateString: string | Date) => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

const showSuggestions = computed(() => {
  return searchQuery.value && searchSuggestions.value.length > 0;
});

const filteredSuggestions = computed(() => {
  if (!searchQuery.value) return [];
  return searchSuggestions.value.filter(suggestion => 
    suggestion.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 组件挂载时获取最新文章和热门标签
onMounted(() => {
  fetchLatestArticles();
  fetchHotTags();
});
</script>

<style scoped>
.search-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--space-6);
  animation: fadeIn 0.5s ease-out;
}

/* 页面头部 */
.page-header {
  text-align: center;
  margin-bottom: var(--space-8);
  padding-bottom: var(--space-6);
  border-bottom: 1px solid var(--border-light);
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 var(--space-3) 0;
}

.page-subtitle {
  font-size: 1.125rem;
  color: var(--text-secondary);
  margin: 0;
  line-height: 1.6;
}

/* 搜索区域 */
.search-section {
  margin-bottom: var(--space-8);
}

.search-container {
  max-width: 600px;
  margin: 0 auto;
  position: relative;
}

.search-input-wrapper {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, var(--bg-primary), var(--bg-secondary));
  border: 2px solid var(--border-color);
  border-radius: var(--radius-xl);
  padding: var(--space-4);
  transition: all var(--transition-normal);
  box-shadow: var(--shadow-sm);
  position: relative;
  overflow: hidden;
}

.search-input-wrapper::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, var(--primary-light), transparent);
  opacity: 0;
  transition: opacity var(--transition-normal);
  pointer-events: none;
}

.search-input-wrapper:hover::before {
  opacity: 0.1;
}

.search-input-wrapper.focused {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px var(--primary-light);
  background: linear-gradient(135deg, var(--bg-primary), var(--primary-light));
}

.search-icon {
  color: var(--text-tertiary);
  margin-right: var(--space-3);
  flex-shrink: 0;
  transition: color var(--transition-fast);
}

.search-input-wrapper.focused .search-icon {
  color: var(--primary-color);
}

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  font-size: 1rem;
  color: var(--text-primary);
  outline: none;
  padding: 0;
}

.search-input::placeholder {
  color: var(--text-tertiary);
}

.search-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  background: linear-gradient(135deg, var(--primary-color), var(--primary-hover));
  color: white;
  border: none;
  padding: var(--space-3) var(--space-4);
  border-radius: var(--radius-lg);
  cursor: pointer;
  font-weight: 600;
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-sm);
}

.search-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
  background: linear-gradient(135deg, var(--primary-hover), var(--primary-active));
}

.search-btn:disabled {
  background: var(--text-tertiary);
  cursor: not-allowed;
  transform: none;
}

.btn-icon {
  flex-shrink: 0;
}

/* 搜索建议 */
.search-suggestions {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  margin-top: var(--space-2);
  z-index: 100;
  overflow: hidden;
}

.suggestion-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--space-3) var(--space-4);
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-secondary);
}

.suggestion-title {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-secondary);
}

.clear-suggestions {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  color: var(--text-tertiary);
  transition: color var(--transition-fast);
}

.clear-suggestions:hover {
  color: var(--text-primary);
}

.suggestion-list {
  max-height: 300px;
  overflow-y: auto;
}

.suggestion-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3) var(--space-4);
  cursor: pointer;
  transition: background-color var(--transition-fast);
}

.suggestion-item:hover {
  background: var(--bg-secondary);
}

.suggestion-icon {
  color: var(--text-tertiary);
  flex-shrink: 0;
}

.suggestion-text {
  font-size: 0.875rem;
  color: var(--text-primary);
}

.no-suggestions {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3) var(--space-4);
  color: var(--text-secondary);
  font-size: 0.875rem;
}

.no-suggestions-icon {
  color: var(--text-tertiary);
  flex-shrink: 0;
}

/* 搜索结果 */
.search-results {
  animation: slideUp 0.3s ease-out;
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-6);
  padding-bottom: var(--space-4);
  border-bottom: 1px solid var(--border-light);
}

.results-title {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.results-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

.results-stats {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.stat-text {
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.clear-search {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  background: none;
  border: none;
  color: var(--text-secondary);
  cursor: pointer;
  font-size: 0.875rem;
  transition: color var(--transition-fast);
}

.clear-search:hover {
  color: var(--text-primary);
}

.clear-icon {
  flex-shrink: 0;
}

.search-tabs {
  display: flex;
  gap: var(--space-2);
  margin-bottom: var(--space-6);
  border-bottom: 1px solid var(--border-light);
}

.search-tab {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-4);
  background: none;
  border: none;
  color: var(--text-secondary);
  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 500;
  transition: all var(--transition-fast);
  border-bottom: 2px solid transparent;
}

.search-tab:hover {
  color: var(--text-primary);
}

.search-tab.active {
  color: var(--primary-color);
  border-bottom-color: var(--primary-color);
}

.tab-icon {
  flex-shrink: 0;
}

/* 热门搜索 */
.trending-section {
  margin-bottom: var(--space-8);
}

.trending-header {
  text-align: center;
  margin-bottom: var(--space-6);
  padding-bottom: var(--space-4);
  border-bottom: 1px solid var(--border-light);
}

.trending-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 var(--space-2) 0;
}

.trending-subtitle {
  font-size: 1rem;
  color: var(--text-secondary);
  margin: 0;
}

.trending-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

.trending-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-3);
  justify-content: center;
}

.trending-tag {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-4);
  background: linear-gradient(135deg, var(--bg-primary), var(--bg-secondary));
  border: 2px solid var(--border-color);
  border-radius: var(--radius-lg);
  color: var(--text-primary);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-sm);
  position: relative;
  overflow: hidden;
}

.trending-tag::before {
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

.trending-tag:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
  border-color: var(--primary-color);
  color: var(--primary-color);
}

.trending-tag:hover::before {
  opacity: 0.1;
}

.trending-tag:hover .tag-icon {
  color: var(--primary-color);
}

.tag-icon {
  color: var(--text-tertiary);
  flex-shrink: 0;
  transition: color var(--transition-fast);
}

/* 推荐内容 */
.recommended-section {
  margin-bottom: var(--space-8);
}

.section-header {
  text-align: center;
  margin-bottom: var(--space-6);
  padding-bottom: var(--space-4);
  border-bottom: 1px solid var(--border-light);
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 var(--space-2) 0;
}

.section-subtitle {
  font-size: 1rem;
  color: var(--text-secondary);
  margin: 0;
}

.section-icon {
  color: var(--primary-color);
  flex-shrink: 0;
}

/* 文章网格 */
.articles-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: var(--space-6);
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

/* 热门标签 */
.popular-tags-section {
  margin-bottom: var(--space-8);
}

.tags-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-3);
  justify-content: center;
}

.tag-cloud-item {
  padding: var(--space-2) var(--space-4);
  background: var(--bg-primary);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  color: var(--text-primary);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.tag-cloud-item:hover {
  background: var(--primary-light);
  border-color: var(--primary-color);
  color: var(--primary-color);
  transform: translateY(-2px);
}

/* 动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-page {
    padding: var(--space-4);
  }
  
  .page-title {
    font-size: 2rem;
  }
  
  .search-input-wrapper {
    flex-direction: column;
    gap: var(--space-3);
  }
  
  .search-btn {
    width: 100%;
    margin-left: 0;
    justify-content: center;
  }
  
  .results-header {
    flex-direction: column;
    gap: var(--space-3);
    align-items: flex-start;
  }
  
  .trending-tags {
    gap: var(--space-2);
  }
  
  .trending-tag {
    padding: var(--space-2) var(--space-3);
    font-size: 0.75rem;
  }
}

@media (max-width: 480px) {
  .search-page {
    padding: var(--space-3);
  }
  
  .page-title {
    font-size: 1.75rem;
  }
  
  .page-subtitle {
    font-size: 1rem;
  }
  
  .search-input-wrapper {
    padding: var(--space-3);
  }
  
  .search-input {
    font-size: 0.875rem;
  }
}
</style>