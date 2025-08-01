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

    <!-- 热门搜索 -->
    <div v-else class="trending-section">
      <div class="trending-header">
        <h2 class="trending-title">热门搜索</h2>
        <p class="trending-subtitle">大家都在搜索什么</p>
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
          {{ tag }}
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import SearchResultsList from '@/components/SearchResultsList/index.vue';

const searchQuery = ref('');
const page = ref(1);
const hasSearched = ref(false);
const isSearchFocused = ref(false);
const activeTab = ref('all');

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
const trendingTags = [
  'Vue.js', 'React', 'TypeScript', 'JavaScript', 
  'Python', 'Java', 'Go', 'Docker', 'Kubernetes',
  '机器学习', '人工智能', '区块链', '云计算'
];

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

const showSuggestions = computed(() => {
  return searchQuery.value && searchSuggestions.value.length > 0;
});

const filteredSuggestions = computed(() => {
  if (!searchQuery.value) return [];
  return searchSuggestions.value.filter(suggestion => 
    suggestion.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
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
  background: var(--bg-primary);
  border: 2px solid var(--border-color);
  border-radius: var(--radius-xl);
  padding: var(--space-4);
  transition: all var(--transition-fast);
  box-shadow: var(--shadow-sm);
}

.search-input-wrapper:focus-within {
  border-color: var(--primary-color);
  box-shadow: var(--shadow-md);
}

.search-input-wrapper.focused {
  border-color: var(--primary-color);
  box-shadow: var(--shadow-md);
}

.search-icon {
  color: var(--text-tertiary);
  margin-right: var(--space-3);
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 1rem;
  color: var(--text-primary);
  background: transparent;
}

.search-input::placeholder {
  color: var(--text-tertiary);
}

.search-btn {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-3) var(--space-4);
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--radius-lg);
  font-weight: 600;
  cursor: pointer;
  transition: all var(--transition-fast);
  margin-left: var(--space-3);
}

.search-btn:hover:not(:disabled) {
  background: var(--primary-hover);
  transform: translateY(-1px);
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
  animation: fadeIn 0.5s ease-out;
}

.trending-header {
  text-align: center;
  margin-bottom: var(--space-8);
}

.trending-title {
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
  background: var(--bg-primary);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  color: var(--text-primary);
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.trending-tag:hover {
  background: var(--primary-light);
  border-color: var(--primary-color);
  color: var(--primary-color);
  transform: translateY(-1px);
}

.tag-icon {
  flex-shrink: 0;
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