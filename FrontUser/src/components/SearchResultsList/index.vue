<template>
  <div class="search-results">
    <div v-if="loading">Loading...</div>
    <div v-else-if="error">Error: {{ error }}</div>
    <div v-else-if="!articles || articles.length === 0">No articles found.</div>
    <ul v-else>
      <li v-for="article in articles" :key="article.id" class="article-card" @click="goToDetail(article.id)">
        <div class="article-title">{{ article.title }}</div>
        <div class="article-summary">{{ article.summary }}</div>
        <div class="article-meta">
          <span class="article-author">作者：{{ article.author }}</span>
          <span class="article-date">发布日期：{{ article.createDate }}</span>
          <span class="article-category">文章类型：{{ article.category.categoryName }}</span>
          <span class="article-views">👀浏览量： {{ article.viewCounts }}</span>
          <span class="article-comments">💬评论数： {{ article.commentCounts }}</span>
        </div>
      </li>
    </ul>
    <div class="pagination">
      <button @click="changePage(page - 1)" :disabled="page <= 1">Prev</button>
      <span>Page {{ page }} of {{ totalPage }}</span>
      <button @click="changePage(page + 1)" :disabled="page >= totalPage">Next</button>
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

onMounted(() => {
  fetchArticles();
});
</script>

<style scoped>



.search-results {
  max-width: 1200px;
  margin: 2rem auto;
  padding: 2rem;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  font-family: 'Arial', sans-serif;
  color: #333;
  overflow: hidden;
}

.search-results div {
  text-align: center;
  padding: 1rem;
  color: #555;
  font-size: 1rem;
}

.search-results div.error {
  color: #d9534f;
  background-color: #f2dede;
  border-radius: 4px;
  padding: 1rem;
  margin-bottom: 1rem;
}

.search-results ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  grid-gap: 20px;
  justify-content: center;
}

.search-results li {
  background-color: #fafafa;
  border: 1px solid #ececec;
  padding: 1.5rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
  flex-direction: column;
}

.search-results li:hover {
  border-color: #007bff;
  transform: translateY(-3px);
}

.search-results .article-title {
  font-size: 1.25rem;
  color: #333;
  font-weight: bold;
  margin-bottom: 1rem;
}

.search-results .article-summary {
  font-size: 1rem;
  color: #666;
  line-height: 1.5;
  flex-grow: 1;
  margin-bottom: 1rem;
}

.search-results .article-meta {
  display: flex;
  flex-wrap: wrap; /* 允许换行 */
  justify-content: space-between; /* 均匀分布 */
  align-items: center; /* 垂直居中对齐 */
  width: 100%;
  padding-top: 0.5rem;
  padding-bottom: 1rem;
  gap: 0.3rem; /* 减小属性之间的间距 */
}

.search-results .article-author,
.search-results .article-date,
.search-results .article-category,
.search-results .article-views,
.search-results .article-comments {
  font-size: 0.875rem;
  color: #888;
  white-space: nowrap;
  flex: 0 0 auto; /* 防止元素伸缩，保持固定宽度 */
}

.search-results .pagination {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
  gap: 1rem;
}

.search-results .pagination button {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  background-color: #007bff;
  color: #ffffff;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.search-results .pagination button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.search-results .pagination button:not(:disabled):hover {
  background-color: #0056b3;
  transform: scale(1.1);
}

.search-results .pagination span {
  font-weight: bold;
  color: #333;
  font-size: 1rem;
}

@media (max-width: 768px) {
  .search-results {
    padding: 2rem;
    margin: 2rem;
  }

  .search-results ul {
    grid-template-columns: repeat(auto-fill, minmax(100%, 1fr));
  }

  .search-results .pagination {
    flex-direction: column;
    align-items: center;
  }

  .search-results .pagination button {
    margin-bottom: 1rem;
    padding: 0.75rem 1.5rem;
  }
}

@media (max-width: 480px) {
  .search-results {
    padding: 1.5rem;
    margin: 1rem;
  }

  .search-results .pagination button {
    padding: 0.75rem 1.5rem;
    font-size: 1rem;
  }
}
</style>