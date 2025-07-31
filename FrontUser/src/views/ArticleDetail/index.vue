<template>
  <div class="article-detail-container">
    <div v-if="article" class="article-detail">
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
      <aside class="article-aside">
        <div class="article-metadata">
          <div class="article-tags">
            <span v-for="tag in article.tags" :key="tag.id" class="article-tag">
              <img :src="tag.avatar" :alt="tag.tagName" />
              {{ tag.tagName }}
            </span>
          </div>
          <div class="article-category">
            <img :src="article.category.avatar" :alt="article.category.categoryName" />
            <span>{{ article.category.categoryName }}</span>
          </div>
        </div>
      </aside>
      <div class="article-comments-wrapper">
        <Comments :article-id="article.id "  />
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

onMounted(async () => {
  try {
    const response = await axios.post(`http://localhost:8888/articles/view/${route.params.id}`);
    article.value = response.data.data;
  } catch (error) {
    console.error('Failed to fetch article detail:', error);
  }
});

const formatDate = (date: string) => {
  const dateObj = new Date(date);
  return dateObj.toLocaleDateString();
};
</script>

<style scoped>
.article-detail-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  flex-wrap: wrap;
  max-width: 1200px;
  margin: 40px auto;
  padding: 40px;
}

.article-detail {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 40px; /* Increased gap for better spacing */
}

.article-content-wrapper {
  max-width: 720px;
  text-align: center; /* Center align text */
}

.article-header {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin-bottom: 40px;
}

.article-title {
  font-size: 2.5rem;
  margin: 0;
  color: #333;
}

.article-meta {
  font-size: 1rem;
  color: #666;
  margin-top: 20px;
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
  margin-top: 20px;
  font-size: 1rem;
  color: #666;
  display: flex;
  justify-content: center;
  gap: 24px;
}

.view-counts, .comment-counts {
  margin-right: 1rem;
}

.article-body {
  font-size: 1.25rem;
  color: #444;
  line-height: 1.6;
  margin-top: 40px;
}

.article-summary {
  margin-bottom: 40px;
}

.article-content {
  margin-bottom: 40px;
}

.article-aside {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 20px; /* Space between tags and category */
  padding-left: 40px; /* Space between content and aside */
  width: 240px; /* Fixed width for the aside */
}

.article-metadata {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.article-tag {
  display: flex;
  align-items: center;
  background-color: #f0f0f0;
  border-radius: 4px;
  padding: 0.25rem 0.5rem;
}

.article-tag img {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  margin-right: 5px;
}

.article-category {
  display: flex;
  align-items: center;
  background-color: #f0f0f0;
  border-radius: 4px;
  padding: 0.25rem 0.5rem;
}

.article-category img {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  margin-right: 5px;
}

.article-comments-wrapper {
  width: 1000px;
  margin-top: 40px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

/* Responsive design */
@media (max-width: 768px) {
  .article-detail-container {
    padding: 20px;
    margin: 20px auto;
  }

  .article-header {
    flex-direction: column;
    align-items: center;
  }

  .article-meta {
    text-align: center;
  }

  .article-aside {
    justify-content: center;
    padding-left: 0;
  }

  .article-metadata {
    flex-direction: column;
    align-items: center;
  }

  .article-tags,
  .article-category {
    justify-content: center;
  }
}
</style>