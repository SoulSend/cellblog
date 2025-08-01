<template>
  <div class="articles-container">
    <article
      v-for="article in articles"
      :key="article.id"
      class="article-card"
      @click="goToDetail(article.id)"
    >
      <header class="article-header">
        <h1 class="article-title">{{ article.title }}</h1>
      </header>
      <section class="article-body">
        <p class="article-summary">{{ article.summary }}</p>
      </section>
      <footer class="article-footer">
        <div class="article-meta-interactions">
          <span class="article-author">作者：{{ article.author }}</span>
          <time class="article-date" :datetime="article.createDate">发布日期：{{ formatDate(article.createDate) }}</time>
          <span class="article-views">👀 浏览量：{{ article.viewCounts }}</span>
          <span class="article-comments">💬 评论数：{{ article.commentCounts }}</span>
        </div>
      </footer>
    </article>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineProps } from 'vue';
import axios from '@/axios';
import { useRouter } from 'vue-router';

// 定义接口
interface Article {
  id: number;
  title: string;
  summary: string;
  author: string;
  createDate: string;
  weight: number; 
  viewCounts: number; 
  commentCounts: number; 
  
}

const props = defineProps({
  categoryId: Number,
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

const fetchArticles = async () => {
  try {
    const response = await axios.post('http://localhost:8888/articles', {
      page: props.page,
      pageSize: props.pageSize,
      categoryId: props.categoryId,
      tagId: null
    });
    articles.value = response.data.data.sort((a:any, b:any) => b.weight - a.weight); // 根据 weight 属性排序
  } catch (error) {
    console.error('Failed to fetch articles:', error);
  }
};

onMounted(fetchArticles);

// 监听 categoryId 变化
import { watch } from 'vue';
watch(() => props.categoryId, (newCategoryId) => {
  console.log('CategoryId changed to:', newCategoryId);
  fetchArticles();
});
const formatDate = (date: string) => {
  const timestamp = Date.parse(date);
  if (!isNaN(timestamp)) {
    const dateObj = new Date(timestamp);
    return dateObj.toLocaleDateString();
  } else {
    return 'Invalid Date';
  }
};
</script>

<style scoped>
.articles-container {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  padding: 20px;
  width: 100%;
  margin: 0 auto;
  max-width: 1200px;
}

.article-card {
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
  overflow: hidden;
  opacity: 0;
  animation: fadeIn 1s ease-in-out forwards;
  will-change: opacity, box-shadow;
  flex: 1 1 calc(90% - 20px); /* Adjust flex basis to account for gaps */
  max-width: calc(90% - 20px); /* Adjust max width to account for gaps */
  min-height: 0;
  margin: 0 5px;
  cursor: pointer; /* Set default cursor to pointer */
  display: flex;
  flex-direction: column; /* 使用列布局 */
}

.article-card:hover {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  transform: translateY(-5px);
  cursor: pointer; /* Enhance interactivity visual feedback */
}

.article-header,
.article-body {
  cursor: pointer; /* Change cursor on hover over text */
}

.article-title {
  font-size: 1.5rem;
  margin: 0;
  color: #333;
}

.article-meta {
  font-size: 0.875rem;
  color: #666;
  text-align: right;
}

.article-author::after {
  content: " •";
  display: inline-block;
  margin: 0 0.25rem;
}

.article-date {
  font-style: italic;
}

.article-body {
  font-size: 1rem;
  color: #444;
  line-height: 1.6;
  margin-top: 1rem;
}

.article-summary {
  margin: 0;
}
/** */

.article-header,
.article-body,
.article-footer {
  margin-bottom: 5px; /* 为每个部分添加底部外边距 */
}

.article-title {
  font-size: 1.5rem;
  margin: 0;
  color: #333;
}

.article-body {
  font-size: 1rem;
  color: #444;
  line-height: 1.6;
  flex-grow: 1; /* 使概述部分占据剩余空间 */
}

.article-footer {
  display: flex;
  flex-direction: column; /* 使底部内容垂直排列 */
  align-items: flex-end; /* 将交互数据向右对齐 */
}

.article-meta-interactions {
  display: flex;
  justify-content: space-between; /* 均匀分布所有元素 */
  align-items: center; /* 垂直居中对齐 */
  width: 100%; /* 使容器宽度充满父元素 */
  padding-top: 10px; /* 添加顶部内边距 */
  padding-bottom: 10px; /* 添加底部内边距 */
}

.article-meta-interactions > * {
  flex: 1; /* 使每个元素占据相同的空间 */
  text-align: left; /* 文本居中对齐 */
}

/* 淡入动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>