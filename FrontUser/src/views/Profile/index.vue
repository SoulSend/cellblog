<template>
  <div class="profile-page">
    <!-- 顶部导航栏 -->
    <div class="profile-nav">
      <button class="back-home-btn" @click="goHome">
        <span class="back-icon">←</span>
        返回首页
      </button>
      <h2 class="nav-title">个人信息</h2>
    </div>

    <!-- 顶部用户信息卡片 -->
    <div class="profile-header">
      <div class="profile-card">
        <div class="profile-avatar-section">
          <div class="avatar-container">
            <img :src="user?.avatar || '/src/assets/image/userAvatar.png'" alt="User Avatar" class="profile-avatar" />
            <div class="avatar-overlay">
              <button class="change-avatar-btn" @click="changeAvatar">
                <span class="camera-icon">📷</span>
              </button>
            </div>
          </div>
          <div class="profile-info">
            <h1 class="profile-name">{{ user?.nickname || '用户昵称' }}</h1>
            <p class="profile-account">@{{ user?.account }}</p>
            <div class="profile-stats">
              <div class="stat-item">
                <span class="stat-number">{{ articleList.length }}</span>
                <span class="stat-label">文章</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ totalViews }}</span>
                <span class="stat-label">浏览量</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ totalComments }}</span>
                <span class="stat-label">评论</span>
              </div>
            </div>
          </div>
        </div>
        <div class="profile-actions">
          <button class="edit-profile-btn" @click="editUserInfo">
            <span class="edit-icon">✏️</span>
            编辑资料
          </button>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="profile-content">
      <!-- 左侧：详细信息 -->
      <div class="profile-details">
        <div class="detail-card">
          <h3 class="card-title">个人信息</h3>
          <div class="detail-list">
            <div class="detail-item">
              <span class="detail-label">用户ID</span>
              <span class="detail-value">{{ user?.id || '未知' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">邮箱</span>
              <span class="detail-value">{{ user?.email || '未设置' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">手机号</span>
              <span class="detail-value">{{ user?.mobilePhone || '未设置' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">注册时间</span>
              <span class="detail-value">{{ formatDate(user?.createDate) }}</span>
            </div>
          </div>
        </div>

        <div class="detail-card">
          <h3 class="card-title">账户统计</h3>
          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-icon">📝</div>
              <div class="stat-content">
                <div class="stat-value">{{ articleList.length }}</div>
                <div class="stat-name">发布文章</div>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">👁️</div>
              <div class="stat-content">
                <div class="stat-value">{{ totalViews }}</div>
                <div class="stat-name">总浏览量</div>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">💬</div>
              <div class="stat-content">
                <div class="stat-value">{{ totalComments }}</div>
                <div class="stat-name">总评论数</div>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">⭐</div>
              <div class="stat-content">
                <div class="stat-value">{{ averageRating }}</div>
                <div class="stat-name">平均评分</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：文章列表 -->
      <div class="articles-section">
        <div class="articles-header">
          <h3 class="section-title">我的文章</h3>
          <div class="articles-filter">
            <select v-model="sortBy" class="filter-select">
              <option value="date">按时间排序</option>
              <option value="views">按浏览量排序</option>
              <option value="comments">按评论数排序</option>
            </select>
          </div>
        </div>

        <div class="articles-list">
          <div v-if="articleList.length === 0" class="empty-state">
            <div class="empty-icon">📝</div>
            <h4>还没有发布文章</h4>
            <p>开始创作你的第一篇文章吧！</p>
            <button class="create-article-btn" @click="createArticle">写文章</button>
          </div>

          <div v-else class="article-item" v-for="article in sortedArticles" :key="article.id" @click="goToDetail(article.id)">
            <div class="article-main">
              <h4 class="article-title">{{ article.title }}</h4>
              <p class="article-summary">{{ article.summary }}</p>
              <div class="article-meta">
                <span class="article-category" v-if="article.category">{{ article.category }}</span>
                <span class="article-date">{{ formatDate(article.createDate) }}</span>
              </div>
            </div>
            <div class="article-stats">
              <div class="stat">
                <span class="stat-icon">👁️</span>
                <span class="stat-value">{{ article.viewCounts }}</span>
              </div>
              <div class="stat">
                <span class="stat-icon">💬</span>
                <span class="stat-value">{{ article.commentCounts }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑个人信息模态框 -->
    <div v-if="showEditForm" class="modal-overlay" @click="cancelEdit">
      <div class="edit-modal" @click.stop>
        <div class="modal-header">
          <h3>编辑个人信息</h3>
          <button class="close-btn" @click="cancelEdit">×</button>
        </div>
        <form @submit.prevent="submitEdit" class="edit-form">
          <div class="form-group">
            <label for="nickname">昵称</label>
            <input 
              id="nickname"
              type="text" 
              v-model="editForm.nickname" 
              placeholder="请输入昵称" 
              required 
            />
          </div>
          <div class="form-group">
            <label for="email">邮箱</label>
            <input 
              id="email"
              type="email" 
              v-model="editForm.email" 
              placeholder="请输入邮箱" 
              required 
            />
          </div>
          <div class="form-group">
            <label for="mobilePhone">手机号</label>
            <input 
              id="mobilePhone"
              type="tel" 
              v-model="editForm.mobilePhone" 
              placeholder="请输入手机号" 
            />
          </div>
          <div class="form-actions">
            <button type="button" class="cancel-btn" @click="cancelEdit">取消</button>
            <button type="submit" class="save-btn">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
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
const sortBy = ref('date');
const editForm = ref({
  nickname: '',
  email: '',
  mobilePhone: ''
});

// 计算属性
const totalViews = computed(() => {
  return articleList.value.reduce((sum, article) => sum + article.viewCounts, 0);
});

const totalComments = computed(() => {
  return articleList.value.reduce((sum, article) => sum + article.commentCounts, 0);
});

const averageRating = computed(() => {
  if (articleList.value.length === 0) return 0;
  const totalRating = articleList.value.reduce((sum, article) => sum + (article.viewCounts / 100), 0);
  return (totalRating / articleList.value.length).toFixed(1);
});

const sortedArticles = computed(() => {
  const articles = [...articleList.value];
  switch (sortBy.value) {
    case 'views':
      return articles.sort((a, b) => b.viewCounts - a.viewCounts);
    case 'comments':
      return articles.sort((a, b) => b.commentCounts - a.commentCounts);
    case 'date':
    default:
      return articles.sort((a, b) => new Date(b.createDate).getTime() - new Date(a.createDate).getTime());
  }
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
      await fetchUserInfo();
      showEditForm.value = false;
    } else {
      alert('修改失败');
    }
  } catch (error) {
    console.error('Failed to update user info:', error);
    alert('修改失败，请重试');
  }
};

const cancelEdit = () => {
  showEditForm.value = false;
};

const changeAvatar = () => {
  // 头像更换功能
  console.log('更换头像');
};

const createArticle = () => {
  router.push('/WriteArticle');
};

const goHome = () => {
  router.push('/home/index');
};

const formatDate = (dateString: string) => {
  if (!dateString) return '未知';
  const date = new Date(dateString);
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
};

onMounted(() => {
  fetchUserInfo();
  fetchArticleList();
});
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: #f8f9fa;
  padding: 0;
}

/* 顶部导航栏 */
.profile-nav {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px 40px;
  background: white;
  border-bottom: 1px solid #e1e5e9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.back-home-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #f8f9fa;
  border: 1px solid #e1e5e9;
  color: #666;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.back-home-btn:hover {
  background: #e9ecef;
  color: #333;
  border-color: #adb5bd;
}

.back-icon {
  font-size: 16px;
  font-weight: bold;
}

.nav-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

/* 顶部用户信息卡片 */
.profile-header {
  padding: 40px;
  background: white;
  border-bottom: 1px solid #e1e5e9;
}

.profile-card {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.profile-avatar-section {
  display: flex;
  align-items: center;
  gap: 24px;
}

.avatar-container {
  position: relative;
  width: 120px;
  height: 120px;
}

.profile-avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #f0f0f0;
  transition: all 0.3s ease;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-container:hover .avatar-overlay {
  opacity: 1;
}

.change-avatar-btn {
  background: none;
  border: none;
  color: white;
  font-size: 24px;
  cursor: pointer;
}

.profile-info {
  flex: 1;
}

.profile-name {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin: 0 0 8px 0;
}

.profile-account {
  font-size: 16px;
  color: #666;
  margin: 0 0 20px 0;
}

.profile-stats {
  display: flex;
  gap: 30px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 24px;
  font-weight: 700;
  color: #06bac7;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.profile-actions {
  display: flex;
  gap: 12px;
}

.edit-profile-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #06bac7;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.edit-profile-btn:hover {
  background: #05a3b0;
}

/* 主要内容区域 */
.profile-content {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px;
}

/* 左侧详细信息 */
.profile-details {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.detail-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e1e5e9;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0 0 20px 0;
}

.detail-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-label {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.detail-value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  background: #e9ecef;
  transform: translateY(-2px);
}

.stat-icon {
  font-size: 24px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 20px;
  font-weight: 700;
  color: #06bac7;
  line-height: 1;
}

.stat-name {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

/* 右侧文章列表 */
.articles-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e1e5e9;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.articles-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.filter-select {
  padding: 8px 12px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s ease;
}

.filter-select:focus {
  border-color: #06bac7;
}

.articles-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #666;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-state h4 {
  font-size: 18px;
  margin: 0 0 8px 0;
  color: #333;
}

.empty-state p {
  margin: 0 0 24px 0;
}

.create-article-btn {
  background: linear-gradient(135deg, #06bac7 0%, #008db2 100%);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.create-article-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(6, 186, 199, 0.3);
}

.article-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border: 1px solid #e1e5e9;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.article-item:hover {
  border-color: #06bac7;
  box-shadow: 0 4px 12px rgba(6, 186, 199, 0.1);
  transform: translateY(-2px);
}

.article-main {
  flex: 1;
}

.article-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
  line-height: 1.4;
}

.article-summary {
  font-size: 14px;
  color: #666;
  margin: 0 0 12px 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #999;
}

.article-category {
  background: #f0f0f0;
  padding: 4px 8px;
  border-radius: 4px;
  color: #666;
}

.article-stats {
  display: flex;
  gap: 16px;
}

.stat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #666;
}

.stat-icon {
  font-size: 14px;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.edit-modal {
  background: white;
  border-radius: 16px;
  width: 500px;
  max-width: 90vw;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 24px 0;
}

.modal-header h3 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background-color: #f0f0f0;
  color: #333;
}

.edit-form {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #555;
  margin-bottom: 8px;
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s ease;
  box-sizing: border-box;
}

.form-group input:focus {
  border-color: #06bac7;
}

.form-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 24px;
}

.cancel-btn, .save-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cancel-btn {
  background-color: #f0f0f0;
  color: #666;
}

.cancel-btn:hover {
  background-color: #e0e0e0;
}

.save-btn {
  background: linear-gradient(135deg, #06bac7 0%, #008db2 100%);
  color: white;
}

.save-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(6, 186, 199, 0.3);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .profile-content {
    grid-template-columns: 1fr;
  }
  
  .profile-card {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }
  
  .profile-avatar-section {
    flex-direction: column;
  }
  
  .profile-stats {
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .profile-page {
    padding: 16px;
  }
  
  .profile-card {
    padding: 20px;
  }
  
  .avatar-container {
    width: 100px;
    height: 100px;
  }
  
  .profile-name {
    font-size: 24px;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .article-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .article-stats {
    align-self: flex-end;
  }
}
</style>