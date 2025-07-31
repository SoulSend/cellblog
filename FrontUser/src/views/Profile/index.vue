<template>
  <div class="user-profile-container">
    <!-- 个人信息部分 -->
    <div class="user-profile-info" v-if="user">
      <img :src="user.avatar" alt="User Avatar" class="avatar" />
      <div class="user-details">
        <h2>{{ user.nickname }}</h2>
        <p>ID: {{ user.id }}</p>
        <p>账户: {{ user.account }}</p>
        <p>邮箱: {{ user.email || '未提供' }}</p>
        <p>手机号: {{ user.mobilePhone || '未提供' }}</p>
        <p>创建日期: {{ formatDate(user.createDate) }}</p>
      </div>
      <button @click="editUserInfo">修改个人信息</button>
    </div>

    <!-- 文章列表部分 -->
    <div class="article-list-container">
      <div class="article-list">
        <h3>文章列表</h3>
        <div v-if="articleList.length === 0" class="empty-list">没有文章</div>
        <div v-for="article in articleList" :key="article.id" class="article-card" @click="goToDetail(article.id)">
          <div class="article-header">
            <h4>{{ article.title }}</h4>
            <p>{{ article.summary }}</p>
          </div>
          <div class="article-footer">
            <p>评论数: {{ article.commentCounts }}</p>
            <p>浏览量: {{ article.viewCounts }}</p>
            <p>创建日期: {{ formatDate(article.createDate) }}</p>
            <p v-if="article.category">分类: {{ article.category }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑个人信息表单遮罩层 -->
    <div v-if="showEditForm" class="edit-form-overlay" @click="cancelEdit" :class="{'show': showEditForm}">
      <div class="edit-form" @click.stop :class="{'show': showEditForm}">
        <h3>编辑个人信息</h3>
        <form @submit.prevent="submitEdit">
          <input type="text" v-model="editForm.nickname" placeholder="昵称" required />
          <input type="email" v-model="editForm.email" placeholder="邮箱" required />
          <input type="tel" v-model="editForm.mobilePhone" placeholder="手机号" />
          <button type="submit">确认修改</button>
          <button type="button" @click="cancelEdit">取消</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from '@/axios';
import { useRouter } from 'vue-router';

// 定义文章的数据结构
interface Article {
  id: string;
  title: string;
  summary: string;
  commentCounts: number;
  viewCounts: number;
  createDate: string;
  category?: string;
}

// 定义用户的数据结构
interface User {
  id: string;
  nickname: string;
  avatar: string;
  account: string;
  createDate: string;
  email: string;
  mobilePhone?: string | null;
}

const user = ref<User | null>(null);
const articleList = ref<Article[]>([]);
const router = useRouter();
const showEditForm = ref(false);
const editForm = ref({
  nickname: '',
  email: '',
  mobilePhone: ''
});

const fetchUserInfo = async () => {
  try {
    const response = await axios.get<{ data: User }>('/users/userInfo');
    if (response.status === 200 && response.data.data) {
      user.value = response.data.data;
    }
  } catch (error) {
    console.error('Failed to fetch user info:', error);
  }
};

const fetchArticleList = async () => {
  try {
    const response = await axios.get<{ data: Article[] }>('/articles/userInfo/articleList');
    if (response.status === 200 && response.data.data) {
      articleList.value = response.data.data;
    }
  } catch (error) {
    console.error('Failed to fetch article list:', error);
  }
};

const goToDetail = (id: string) => {
  router.push({ name: 'ArticleDetail', params: { id } });
};

const editUserInfo = () => {
  showEditForm.value = true;
  editForm.value.nickname = user.value?.nickname || '';
  editForm.value.email = user.value?.email || '';
  editForm.value.mobilePhone = user.value?.mobilePhone || '';
};

const submitEdit = async () => {
  const userData = {
    id: user.value?.id,
    nickname: editForm.value.nickname || null,
    email: editForm.value.email || null,
    mobilePhone: editForm.value.mobilePhone || null
  };

  try {
    const response = await axios.post('/users/changeUserInfo', userData);
    if (response.status === 200) {
      alert('修改成功');
      window.location.reload();
    } else {
      alert('修改失败');
    }
  } catch (error) {
    console.error('Failed to update user info:', error);
  }
};

const cancelEdit = () => {
  showEditForm.value = false;
};

const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
};


onMounted(() => {
  fetchUserInfo();
  fetchArticleList();
});
</script>

<style scoped>
/* 容器样式 */
.user-profile-container {
  display: flex;
  justify-content: space-between;
  align-items: stretch;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background: linear-gradient(to right, #ffffff, #f0f0f0);
}

/* 用户信息部分样式 */
.user-profile-info {
  flex-basis: 300px;
  margin-right: 20px;
  padding: 20px;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.8);
  transition: transform 0.3s ease-in-out;
}

.user-profile-info:hover {
  transform: translateY(-5px);
}

.avatar {
  width: 100%;
  border-radius: 50%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.avatar:hover {
  transform: scale(1.05);
}

.user-details h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.user-details p {
  margin: 5px 0;
  color: #666;
}

/* 文章列表部分样式 */
.article-list-container {
  flex-grow: 1;
  padding: 20px;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.8);
  overflow-y: auto;
}

/* 文章卡片样式 */
.article-card {
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.article-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 编辑表单遮罩层样式 */
.edit-form-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
  opacity: 0;
  visibility: hidden;
  transition: opacity 0.3s, visibility 0.3s;
}

.edit-form-overlay.show {
  opacity: 1;
  visibility: visible;
}

/* 编辑表单样式 */
.edit-form {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 400px;
  max-width: 90%;
  position: relative;
  opacity: 0;
  transform: scale(0.9);
  transition: opacity 0.3s, transform 0.3s;
}

.edit-form.show {
  opacity: 1;
  transform: scale(1);
}

/* 表单输入框和按钮样式 */
.edit-form input,
.edit-form button {
  width: 100%;
  padding: 12px;
  margin: 8px 0;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  box-sizing: border-box; /* 添加这个属性以确保padding不影响宽度 */
}

.edit-form input {
  background-color: #f8f8f8;
  border: 1px solid #ddd;
  transition: border-color 0.3s, background-color 0.3s;
}

.edit-form input:focus {
  border-color: #007bff;
  background-color: #fff;
  outline: none;
}

.edit-form button {
  background-color: #17b88a;
  color: white;
  cursor: pointer;
  text-transform: uppercase; /* 使按钮文本大写 */
  letter-spacing: 0.5px; /* 字母间距 */
  transition: background-color 0.3s, transform 0.3s;
}

.edit-form button:hover {
  background-color: #17b88a;
  transform: translateY(-2px);
}

.edit-form button:active {
  background-color: #17b88a;
  transform: translateY(1px);
}

.edit-form button[type="button"] {
  background-color: #6c757d;
}

/* 按钮悬停效果 */
.user-profile-info button {
  background-color: #388E03;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.3s;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.user-profile-info button:hover {
  background-color: #388E03;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.user-profile-info button:active {
  background-color: #388E03;
  transform: translateY(-1px);
}
</style>