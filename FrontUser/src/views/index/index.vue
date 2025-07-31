<template>
  <div class="container">
    <aside class="navigation">
      <nav>
        <ul>
          <li v-for="category in categories" :key="category.id">
            <button @click="navigateToCategory(category)" class="nav-item">
              <img :src="category.avatar" alt="" class="category-avatar" />
              {{ category.categoryName }}
            </button>
          </li>
        </ul>
      </nav>
    </aside>

    <main class="content">
      <router-view></router-view>
    </main>

    <div class="sidebar-widget">
      <h3>最热文章</h3>
      <ul>
        <li v-for="article in hotArticles" :key="article.id" @click="goToDetail(article.id)" class="article-item">
          <div class="article-preview">
            <h4>{{ article.title }}</h4>
            <p class="article-summary">{{ article.summary }}</p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from '@/axios';
import { useRouter } from 'vue-router';

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
const router = useRouter();

onMounted(async () => {
  try {
    const categoryResponse = await axios.get('http://localhost:8888/categorys/detail');
    categories.value = categoryResponse.data.data;
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

function navigateToCategory(category: Category) {
  router.push(`/home/index/${category.categoryName.toLowerCase()}`);
}

const goToDetail = (id: string) => {
  router.push({ name: 'ArticleDetail', params: { id: id } });
};
</script>



<style scoped>
.container {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  height: 100vh;
  margin-top: 20px;
  margin-left: 100px;
}

.navigation {
  background-color: #ffffff;
  padding: 20px;
  flex-shrink: 0;
  min-width: 200px;
  max-width: 250px;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.2);
}

.navigation nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-item {
  display: flex;
  align-items: center;
  width: 100%;
  height: 60px;
  color: #000000;
  text-decoration: none;
  transition: background-color 0.3s, color 0.3s, transform 0.3s, box-shadow 0.3s;
  margin-bottom: 8px;
  background-image: linear-gradient(45deg, rgb(255, 255, 255) 0%, rgb(255, 255, 255) 100%);
  background-size: 200% 200%;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  position: relative;
  text-align: left;
  padding-left: 20px;
}

.nav-item img.category-avatar {
  height: 30px;
  width: 30px;
  margin-right: 10px;
}

.nav-item:hover,
.nav-item:focus {
  background-position: 100% 50%;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
  transform: translateY(-4px);
}

.nav-item.is-active {
  background-color: #f3eeee;
  color: #ffffff;
  text-shadow: 0 0 5px #ffffff;
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(34, 34, 34, 0.2);
  transform: scale(0.98);
  transition: transform 0.1s;
}

.nav-item:not(:last-child) {
  border-bottom: 1px solid #eaeaea;
}

.content {
  flex-grow: 1;
  display: flex;
  flex-direction: row;
  background-color: #f0f0f0;
  padding: 0;
  margin-left: 20px;
  margin-right: 20px;
}

.sidebar-widget {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-left: 10px;
  margin-right: 120px;
  width: 300px;
  max-height: 500px;
  overflow: auto;
}

.article-item {
  border-bottom: 1px solid #eaeaea;
  padding: 10px 0;
  cursor: pointer;
}

.article-item:last-child {
  border-bottom: none;
}

.article-item:hover {
  background-color: #f3eeee;
}

.article-preview {
  display: flex;
  flex-direction: column;
}

.article-preview h4 {
  margin: 0;
  font-size: 14px;
  color: #333;
  white-space: normal;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.article-summary {
  margin-top: 5px;
  font-size: 12px;
  color: #666;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
/* 其他样式保持不变 */

.article-item {
  transition: all 0.3s ease-in-out; /* 平滑过渡所有变化 */
  position: relative; /* 为伪元素和层叠效果做准备 */
  overflow: hidden; /* 确保伪元素不超出边界 */
}

.article-item:hover {
  background-color: #eef2f5; /* 更温和的背景色变化 */
  transform: translateY(-4px); /* 增加上移距离，增强浮动感 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1); /* 增加阴影，提升层次感 */
}

.article-item:hover h4 {
  color: #06bac7; /* 标题颜色变为更鲜艳的蓝色 */
}

.article-item:hover .article-summary {
  color: #555; /* 摘要颜色加深，提高对比度 */
}

/* 添加悬浮时的背景渐变效果 */
.article-item:hover::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: linear-gradient(to right, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0));
  animation: shimmer 1.5s infinite; /* 渐变动画 */
}

/* 渐变动画 */
@keyframes shimmer {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100%);
  }
}
</style>