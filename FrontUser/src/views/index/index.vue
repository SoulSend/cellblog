<template>
  <div class="container">
    <aside class="navigation">
      <nav>
        <ul>
          <li v-for="category in categories" :key="category.id">
            <button @click="navigateToCategory(category)" class="nav-item" :class="{ 'is-active': isCurrentCategory(category) }">
              <img :src="category.avatar" alt="" class="category-avatar" />
              {{ category.categoryName }}
            </button>
          </li>
        </ul>
      </nav>
    </aside>

    <main class="content">
      <router-view v-if="currentCategory" :category="currentCategory.categoryName"></router-view>
      <div v-else class="loading">加载中...</div>
    </main>

    <div class="sidebar-widget">
      <h3 class="widget-title">🔥 最热文章</h3>
      <div class="hot-articles-list">
        <div v-for="article in hotArticles" :key="article.id" 
             @click="goToDetail(article.id)" 
             class="hot-article-item">
          <h4 class="article-title">{{ article.title }}</h4>
          <p class="article-summary">{{ article.summary }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import axios from '@/axios';
import { useRouter, useRoute } from 'vue-router';

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
const route = useRoute();

// 计算当前分类
const currentCategory = computed(() => {
  const categoryParam = route.params.category as string;
  console.log('Current category param:', categoryParam);
  console.log('Available categories:', categories.value);
  
  const foundCategory = categories.value.find(cat => 
    cat.categoryName.toLowerCase() === categoryParam?.toLowerCase()
  );
  console.log('Found category:', foundCategory);
  
  // 如果没有找到分类，返回第一个分类，但要确保categories不为空
  if (!foundCategory && categories.value.length > 0) {
    console.log('Category not found, using first category:', categories.value[0]);
    return categories.value[0];
  }
  
  return foundCategory || null;
});

// 检查是否为当前分类
const isCurrentCategory = (category: Category) => {
  return currentCategory.value?.id === category.id;
};

onMounted(async () => {
  try {
    const categoryResponse = await axios.get('http://localhost:8888/categorys/detail');
    categories.value = categoryResponse.data.data;
    console.log('Categories loaded:', categories.value);
    
    // 如果没有默认路由，跳转到第一个分类
    if (!route.params.category && categories.value.length > 0) {
      const firstCategory = categories.value[0];
      console.log('Redirecting to first category:', firstCategory.categoryName);
      router.push(`/home/index/${firstCategory.categoryName}`);
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

function navigateToCategory(category: Category) {
  console.log('Navigating to category:', category.categoryName);
  router.push(`/home/index/${category.categoryName}`);
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
  background-color: #06bac7;
  color: #ffffff;
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(6, 186, 199, 0.3);
  transition: all 0.3s ease;
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
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-left: 10px;
  margin-right: 120px;
  width: 300px;
  max-height: 500px;
  overflow: auto;
}

.widget-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px 0;
}

.hot-articles-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.hot-article-item {
  padding: 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  border-bottom: 1px solid #f0f0f0;
}

.hot-article-item:last-child {
  border-bottom: none;
}

.hot-article-item:hover {
  background: #f8f9fa;
}

.article-title {
  margin: 0 0 6px 0;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.article-summary {
  margin: 0;
  font-size: 12px;
  color: #666;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}


.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  font-size: 16px;
  color: #666;
}
</style>